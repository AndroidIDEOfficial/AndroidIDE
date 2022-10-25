package com.itsaky.androidide.fragments;

import static com.itsaky.androidide.resources.R.string;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.transition.TransitionManager;

import com.google.android.material.transition.MaterialFadeThrough;
import com.google.android.material.transition.MaterialSharedAxis;
import com.itsaky.androidide.adapters.WizardTemplateAdapter;
import com.itsaky.androidide.app.IDEApplication;
import com.itsaky.androidide.databinding.FragmentWizardBinding;
import com.itsaky.androidide.databinding.LayoutLoadingWizardBinding;
import com.itsaky.androidide.databinding.SetupFooterBinding;
import com.itsaky.androidide.databinding.WizardDetailsBinding;
import com.itsaky.androidide.databinding.WizardTemplatesBinding;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.managers.PreferenceManager;
import com.itsaky.androidide.models.ProjectTemplate;
import com.itsaky.androidide.utils.AndroidUtils;
import com.itsaky.androidide.utils.DialogUtils;
import com.itsaky.androidide.utils.Environment;
import com.itsaky.androidide.utils.FileUtil;
import com.itsaky.androidide.utils.SingleTextWatcher;
import com.itsaky.androidide.viewmodel.WizardViewModel;
import com.itsaky.toaster.Toaster;
import com.itsaky.toaster.ToastUtilsKt;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

public class WizardFragment extends BaseFragment {

  public static final String TAG = "WizardFragmentTag";
  public static final String PREF_PACKAGE_DOMAIN_KEY = "pref_package_domain";
  public static final String PREF_MIN_SDK_INDEX_KEY = "pref_min_sdk_index";
  public static final String PREF_TERGET_SDK_INDEX_KEY = "pref_target_sdk_index";
  public static final String PREF_SAVE_PROJECT_DIR_KEY = "pref_save_prohect_dir";

  private FragmentWizardBinding binding;
  private WizardTemplatesBinding templatesBinding;
  private WizardDetailsBinding detailsBinding;
  private WizardTemplateAdapter mAdapter;
  private LayoutLoadingWizardBinding loadingLayout;
  private SetupFooterBinding footerBinding;

  private WizardViewModel mViewModel;

  private ProgressSheet mProgressSheet;
  private ProjectTemplate mCurrentTemplate;
  private OnProjectCreatedListener mListener;

  private boolean mLast;
  private final OnBackPressedCallback onBackPressedCallback =
      new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
          onNavigateBack();
        }
      };
  private int minSdkIndex;
  private int targetSdkIndex;

  @Nullable
  @Override
  public View onCreateView(
      @NonNull LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    binding = FragmentWizardBinding.inflate(inflater, container, false);
    loadingLayout = binding.loadingLayout;
    templatesBinding = binding.wizardTemplatesLayout;
    detailsBinding = binding.wizardDetailsLayout;
    footerBinding = binding.footerLayout;
    mViewModel = new ViewModelProvider(this).get(WizardViewModel.class);
    return binding.getRoot();
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    requireActivity()
        .getOnBackPressedDispatcher()
        .addCallback(getViewLifecycleOwner(), onBackPressedCallback);

    footerBinding.nextButton.setOnClickListener(v -> onNavigateNext());
    footerBinding.exitButton.setOnClickListener(v -> onNavigateBack());

    templatesBinding.templateRecyclerview.setLayoutManager(
        new GridLayoutManager(requireContext(), 3));

    mAdapter = new WizardTemplateAdapter();
    templatesBinding.templateRecyclerview.setAdapter(mAdapter);

    mViewModel.createTemplatesList();
    mViewModel.getProjects().observe(getViewLifecycleOwner(), mAdapter::submitList);
    mViewModel.getLoadingState().observe(getViewLifecycleOwner(), this::toggleLoadingState);

    mViewModel
        .getStatusMessage()
        .observe(
            getViewLifecycleOwner(),
            (message) -> {
              if (mProgressSheet == null) {
                createProgressSheet();
              }
              setMessage(message);
            });

    mViewModel
        .getErrorState()
        .observe(
            getViewLifecycleOwner(),
            (message) -> {
              if (mProgressSheet != null && mProgressSheet.isShowing()) {
                mProgressSheet.dismiss();
              }
              ToastUtilsKt.toast(message, Toaster.Type.ERROR);
            });

    mViewModel
        .getFileCreatedState()
        .observe(
            getViewLifecycleOwner(),
            (file) -> {
              if (file != null) onSuccess(file);
            });

    mAdapter.setOnItemClickListener(
        (item, pos) -> {
          mCurrentTemplate = item;
          onNavigateNext();
        });

    initDetailsView();
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    mViewModel = null;
    binding = null;
    loadingLayout = null;
    templatesBinding = null;
    detailsBinding = null;
    footerBinding = null;
    mAdapter = null;
    mProgressSheet = null;
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    onBackPressedCallback.setEnabled(false);
  }

  public void onSuccess(File root) {
    if (mProgressSheet != null && mProgressSheet.isShowing()) {
      mProgressSheet.dismiss();
    }
    ToastUtilsKt.toast(string.project_created_successfully, Toaster.Type.SUCCESS);

    if (mListener != null) {
      getParentFragmentManager().popBackStack();
      mListener.openProject(root);
    }
  }

  public void setOnProjectCreatedListener(OnProjectCreatedListener listener) {
    mListener = listener;
  }

  private void toggleLoadingState(boolean loading) {
    if (loading) {
      showLoading();
    } else {
      showTemplatesView();
    }
  }

  private String getSelectedItem(int pos, AutoCompleteTextView view) {
    return view.getAdapter().getItem(pos).toString();
  }

  private void showLoading() {
    TransitionManager.beginDelayedTransition((ViewGroup) requireView(), new MaterialFadeThrough());
    binding.loadingLayout.getRoot().setVisibility(View.VISIBLE);
    binding.wizardTemplatesLayout.getRoot().setVisibility(View.GONE);
    footerBinding.nextButton.setVisibility(View.GONE);
  }

  private int getMinSdk() {
    return getSdkVersion(detailsBinding.etMinSdk);
  }

  private int getTargetSdk() {
    return getSdkVersion(detailsBinding.etTargetSdk);
  }

  private int getSdkVersion(AutoCompleteTextView view) {
    try {
      String sdk =
          view.getText()
              .toString()
              .substring("API".length() + 1, "API".length() + 3); // at least 2 digits
      return Integer.parseInt(sdk);
    } catch (Exception e) {
      ToastUtilsKt.toast(e.getMessage(), Toaster.Type.ERROR);
    }
    return -1;
  }

  private boolean validateDetails() {

    verifyAppName(detailsBinding.tilAppName.getEditText().getText(), false);
    verifyPackageName(detailsBinding.tilPackageName.getEditText().getText());
    if (detailsBinding.tilPackageName.isErrorEnabled()) {
      return false;
    }

    if (detailsBinding.tilAppName.isErrorEnabled()) {
      return false;
    }

    return mCurrentTemplate != null;
  }

  private void createNewProject() {
    if (validateDetails()) {
      final String appName =
          Objects.requireNonNull(detailsBinding.etAppName).getText().toString().trim();
      final String packageName =
          Objects.requireNonNull(detailsBinding.etPackageName).getText().toString().trim();
      final int minSdk = getMinSdk();
      final int targetSdk = getTargetSdk();
      final String savePath = detailsBinding.etSaveLocation.getText().toString().trim();
      final String projectLanguage = detailsBinding.etLanguage.getText().toString();
      final String cppToolChain =
          getCppToolchans().get(detailsBinding.etToolchain.getText().toString());

      PreferenceManager manager = IDEApplication.getInstance().getPrefManager();
      manager.putInt(PREF_MIN_SDK_INDEX_KEY, minSdkIndex);
      manager.putInt(PREF_TERGET_SDK_INDEX_KEY, targetSdkIndex);
      manager.putString(PREF_PACKAGE_DOMAIN_KEY, AndroidUtils.getPackageDomain(packageName));
      mViewModel.createProject(
          mCurrentTemplate,
          appName,
          packageName,
          minSdk,
          targetSdk,
          projectLanguage,
          cppToolChain,
          savePath);
    }
  }

  private List<String> getSdks() {
    return Arrays.asList(
        "API 16: Android 4.0 (Ice Cream Sandwich)",
        "API 17: Android 4.2 (JellyBean)",
        "API 18: Android 4.3 (JellyBean)",
        "API 19: Android 4.4 (KitKat)",
        "API 20: Android 4.4W (KitKat Wear)",
        "API 21: Android 5.0 (Lollipop)",
        "API 22: Android 5.1 (Lollipop)",
        "API 23: Android 6.0 (Marshmallow)",
        "API 24: Android 7.0 (Nougat)",
        "API 25: Android 7.1 (Nougat)",
        "API 26: Android 8.0 (Oreo)",
        "API 27: Android 8.1 (Oreo)",
        "API 28: Android 9.0 (Pie)",
        "API 29: Android 10.0 (Q)",
        "API 30: Android 11.0 (R)",
        "API 31: Android 12.0 (S)",
        "API 32: Android 12.1L (S)");
  }

  private LinkedHashMap<String, String> getCppToolchans() {
    LinkedHashMap<String, String> cppStandartType = new LinkedHashMap<>();
    cppStandartType.put("Toolchain Default", "");
    cppStandartType.put("C++11", "-std=c++11");
    cppStandartType.put("C++14", "-std=c++14");
    cppStandartType.put("C++17", "-std=c++17");
    return cppStandartType;
  }

  private void onNavigateBack() {
    if (!mLast) {
      getParentFragmentManager().popBackStack();
    } else {
      showTemplatesView();
      mLast = false;
    }
  }

  private void onNavigateNext() {
    if (!mLast) {
      showDetailsView();
      mLast = true;
    } else {
      createNewProject();
    }
  }

  private void showTemplatesView() {

    MaterialSharedAxis sharedAxis = new MaterialSharedAxis(MaterialSharedAxis.X, false);

    TransitionManager.beginDelayedTransition((ViewGroup) requireView(), sharedAxis);
    binding.loadingLayout.getRoot().setVisibility(View.GONE);
    templatesBinding.getRoot().setVisibility(View.GONE);
    detailsBinding.getRoot().setVisibility(View.GONE);
    templatesBinding.getRoot().setVisibility(View.VISIBLE);
    detailsBinding.tilToolchain.setVisibility(View.GONE);
    footerBinding.nextButton.setVisibility(View.GONE);
    footerBinding.nextButton.setText(string.next);
    footerBinding.exitButton.setText(string.exit);
    binding.wizardDescriptionId.setText(string.new_project);
  }

  private void showDetailsView() {
    PreferenceManager manager = IDEApplication.getInstance().getPrefManager();

    List<String> languages = new ArrayList<>();
    if (mCurrentTemplate != null) {
      if (mCurrentTemplate.isSupportJava()) {
        languages.add("Java");
      }
      if (mCurrentTemplate.isSupportKotlin()) {
        languages.add("Kotlin");
      }
    }

    final var packageName =
        manager.getString(PREF_PACKAGE_DOMAIN_KEY, "com.example").concat(".myapplication");
    detailsBinding.etAppName.setText(string.template_def_app_name);
    detailsBinding.etPackageName.setText(packageName);
    detailsBinding.tilLanguage.setEnabled(languages.size() != 1);

    detailsBinding.etLanguage.setAdapter(
        new ArrayAdapter<>(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            languages));

    detailsBinding.etLanguage.setListSelection(0);

    if (mCurrentTemplate.isCpp()) {
      detailsBinding.tilToolchain.setVisibility(View.VISIBLE);
      detailsBinding.etToolchain.setAdapter(
          new ArrayAdapter<>(
              requireContext(),
              androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
              getCppToolchans().keySet().toArray(new String[getCppToolchans().size()])));

      detailsBinding.etToolchain.setListSelection(0);
      detailsBinding.etToolchain.setText(getSelectedItem(0, detailsBinding.etToolchain), false);

      showDialogNdkNotSupportedOfficially();
    }

    detailsBinding.etLanguage.setText(getSelectedItem(0, detailsBinding.etLanguage), false);
    minSdkIndex = manager.getInt(PREF_MIN_SDK_INDEX_KEY, 5);

    // google recommended use latest target sdk version
    targetSdkIndex = manager.getInt(PREF_TERGET_SDK_INDEX_KEY, getSdks().size() - 1);
    detailsBinding.etMinSdk.setListSelection(minSdkIndex);
    detailsBinding.etMinSdk.setText(getSelectedItem(minSdkIndex, detailsBinding.etMinSdk), false);

    detailsBinding.etTargetSdk.setListSelection(targetSdkIndex);
    detailsBinding.etTargetSdk.setText(
        getSelectedItem(targetSdkIndex, detailsBinding.etTargetSdk), false);

    setSaveLocation();

    loadingLayout.getRoot().setVisibility(View.GONE);
    detailsBinding.getRoot().setVisibility(View.GONE);

    MaterialSharedAxis sharedAxis = new MaterialSharedAxis(MaterialSharedAxis.X, true);

    TransitionManager.beginDelayedTransition((ViewGroup) requireView(), sharedAxis);

    detailsBinding.getRoot().setVisibility(View.VISIBLE);
    templatesBinding.getRoot().setVisibility(View.GONE);
    binding.wizardDescriptionId.setText(mCurrentTemplate.getName());
    footerBinding.nextButton.setText(string.create_project);
    footerBinding.nextButton.setVisibility(View.VISIBLE);
    footerBinding.exitButton.setText(string.previous);
  }

  private void initDetailsView() {
    PreferenceManager manager = IDEApplication.getInstance().getPrefManager();

    detailsBinding
        .tilAppName
        .getEditText()
        .addTextChangedListener(
            new SingleTextWatcher() {
              @Override
              public void afterTextChanged(Editable editable) {
                verifyAppName(editable, true);
              }
            });

    detailsBinding
        .tilPackageName
        .getEditText()
        .addTextChangedListener(
            new SingleTextWatcher() {
              @Override
              public void afterTextChanged(Editable editable) {
                verifyPackageName(editable);
              }
            });

    detailsBinding.etMinSdk.setAdapter(
        new ArrayAdapter<>(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            getSdks()));

    detailsBinding.etMinSdk.setOnItemClickListener(
        (parent, view, position, id) -> minSdkIndex = position);

    detailsBinding.etTargetSdk.setAdapter(
        new ArrayAdapter<>(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            getSdks()));

    detailsBinding.etTargetSdk.setOnItemClickListener(
        (parent, view, position, id) -> targetSdkIndex = position);

    detailsBinding.tilSaveLocation.setEndIconOnClickListener(
        v -> {
          pickDirectory(
              p -> {
                manager.putString(PREF_SAVE_PROJECT_DIR_KEY, p.getAbsolutePath());
                Environment.setProjectDir(p);
                setSaveLocation();
              });
        });
  }

  private void setPackageName(String appName) {
    final String packageName =
        Objects.requireNonNull(detailsBinding.etPackageName).getText().toString().trim();
    String newPackageName = AndroidUtils.appNameToPackageName(appName, packageName);
    detailsBinding.etPackageName.setText(newPackageName);
  }

  private void setSaveLocation() {
    PreferenceManager manager = IDEApplication.getInstance().getPrefManager();

    String saveProjectPath =
        manager.getString(PREF_SAVE_PROJECT_DIR_KEY, Environment.PROJECTS_DIR.getAbsolutePath())
            + File.separator
            + AndroidUtils.trimWhiteSpace(detailsBinding.etAppName.getText().toString().trim());

    String newPath = FileUtil.getTargetNonExistPath(saveProjectPath, true);
    detailsBinding.etSaveLocation.setText(newPath);
  }

  private void verifyPackageName(Editable editable) {
    String packageName = editable.toString();
    String pkgCheckerMsg = AndroidUtils.validatePackageName(packageName);
    if (!TextUtils.isEmpty(pkgCheckerMsg)) {
      detailsBinding.tilPackageName.setError(pkgCheckerMsg);
    } else {
      detailsBinding.tilPackageName.setErrorEnabled(false);
    }
  }

  private void verifyAppName(Editable editable, boolean isAddToPackage) {
    String name = editable.toString().trim();
    if (TextUtils.isEmpty(name)) {
      detailsBinding.tilAppName.setError(getString(string.wizard_error_name_empty));
      return;
    } else if (!AndroidUtils.validateNameChecker(AndroidUtils.trimWhiteSpace(name))) {
      detailsBinding.tilAppName.setError(getString(string.wizard_error_name_illegal));
      return;
    } else {
      detailsBinding.tilAppName.setErrorEnabled(false);
    }
    if (isAddToPackage) {
      setPackageName(name);
    }
    setSaveLocation();
  }

  private void createProgressSheet() {
    mProgressSheet = new ProgressSheet();
    mProgressSheet.setShowShadow(false);
  }

  private void setMessage(String msg) {
    if (mProgressSheet == null) {
      createProgressSheet();
    }
    mProgressSheet.setMessage(msg);
  }

  private void showDialogNdkNotSupportedOfficially() {
    DialogUtils.newMaterialDialogBuilder(requireContext())
        .setPositiveButton(android.R.string.ok, null)
        .setTitle(string.title_warning)
        .setMessage(string.msg_ndk_currently_unsupported)
        .setCancelable(false)
        .create()
        .show();
  }

  public interface OnProjectCreatedListener {
    void openProject(File project);
  }
}
