/*
 * This file is part of AndroidIDE.
 *
 * AndroidIDE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroidIDE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroidIDE.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package com.itsaky.androidide;

import static com.itsaky.androidide.models.prefs.GeneralPreferencesKt.getUseSystemShell;
import static com.itsaky.androidide.utils.Environment.BIN_DIR;
import static com.itsaky.androidide.utils.Environment.HOME;
import static com.itsaky.androidide.utils.Environment.LOGIN_SHELL;
import static com.itsaky.androidide.utils.Environment.PREFIX;
import static com.itsaky.androidide.utils.Environment.getEnvironment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ClipboardUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ThrowableUtils;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityTerminalBinding;
import com.itsaky.androidide.fragments.CrashReportFragment;
import com.itsaky.androidide.fragments.sheets.ProgressSheet;
import com.itsaky.androidide.models.Constants;
import com.itsaky.androidide.models.prefs.GeneralPreferencesKt;
import com.itsaky.androidide.utils.BootstrapInstaller;
import com.itsaky.androidide.utils.ILogger;
import com.itsaky.androidide.utils.TypefaceUtils;
import com.itsaky.androidide.views.virtualkeys.SpecialButton;
import com.itsaky.androidide.views.virtualkeys.VirtualKeyButton;
import com.itsaky.androidide.views.virtualkeys.VirtualKeysConstants;
import com.itsaky.androidide.views.virtualkeys.VirtualKeysInfo;
import com.itsaky.androidide.views.virtualkeys.VirtualKeysView;
import com.itsaky.terminal.TerminalEmulator;
import com.itsaky.terminal.TerminalSession;
import com.itsaky.terminal.TerminalSessionClient;
import com.itsaky.terminal.TextStyle;
import com.itsaky.terminal.view.TerminalView;
import com.itsaky.terminal.view.TerminalViewClient;

import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Map;

public class TerminalActivity extends StudioActivity
    implements TerminalViewClient, TerminalSessionClient {

  public static final String KEY_WORKING_DIRECTORY = "terminal_workingDirectory";
  private static final ILogger LOG = ILogger.newInstance("TerminalActivity");
  private static final byte[] SOURCES_LIST_CONTENT =
      "deb https://androidide.com/packages/apt/termux-main/ stable main".getBytes();
  private static final String KEY_FONT_SIZE = "terminal_fontSize";
  private ActivityTerminalBinding binding;
  private TerminalView terminal;
  private TerminalSession session;
  private boolean isVisible = false;
  private KeyListener listener;
  private int MIN_FONT_SIZE;
  private int MAX_FONT_SIZE;
  private int DEFAULT_FONT_SIZE;

  public boolean isVisible() {
    return isVisible;
  }

  @Override
  public boolean onKeyUp(int keyCode, KeyEvent e) {
    return false;
  }

  @Override
  public void onTextChanged(TerminalSession changedSession) {
    terminal.onScreenUpdated();
  }

  @Override
  public void onTitleChanged(TerminalSession changedSession) {}

  @Override
  public void onSessionFinished(TerminalSession finishedSession) {
    finish();
  }

  @Override
  public void onCopyTextToClipboard(TerminalSession session, String text) {
    ClipboardUtils.copyText("AndroidIDE Terminal", text);
  }

  @Override
  public void onPasteTextFromClipboard(TerminalSession session) {
    String clip = ClipboardUtils.getText().toString();
    if (clip.trim().length() > 0 && terminal != null && terminal.mEmulator != null) {
      terminal.mEmulator.paste(clip);
    }
  }

  @Override
  public void onBell(TerminalSession session) {}

  @Override
  public void onColorsChanged(TerminalSession session) {}

  @Override
  public void onTerminalCursorStateChange(boolean state) {}

  @Override
  public Integer getTerminalCursorStyle() {
    return TerminalEmulator.DEFAULT_TERMINAL_CURSOR_STYLE;
  }

  @Override
  public float onScale(float scale) {
    if (scale < 0.9f || scale > 1.1f) {
      boolean increase = scale > 1.f;
      changeFontSize(increase);
      return 1.0f;
    }
    return scale;
  }

  private void changeFontSize(final boolean increase) {
    int fontSize = getFontSize();
    fontSize += (increase ? 1 : -1) * 2;
    fontSize = Math.max(MIN_FONT_SIZE, Math.min(fontSize, MAX_FONT_SIZE));
    setFontSize(fontSize, true);
  }

  public void setFontSize(int value, boolean apply) {
    getApp().getPrefManager().putString(KEY_FONT_SIZE, String.valueOf(value));

    if (apply) {
      terminal.setTextSize(getFontSize());
    }
  }

  public int getFontSize() {
    int fontSize;
    try {
      fontSize =
          Integer.parseInt(
              getApp()
                  .getPrefManager()
                  .getString(KEY_FONT_SIZE, String.valueOf(DEFAULT_FONT_SIZE)));
    } catch (NumberFormatException | ClassCastException e) {
      fontSize = DEFAULT_FONT_SIZE;
    }

    return Math.min(Math.max(fontSize, MIN_FONT_SIZE), MAX_FONT_SIZE);
  }

  @Override
  public void onSingleTapUp(MotionEvent e) {
    KeyboardUtils.showSoftInput(terminal);
  }

  @Override
  public boolean shouldBackButtonBeMappedToEscape() {
    return false;
  }

  @Override
  public boolean shouldEnforceCharBasedInput() {
    return true;
  }

  @Override
  public boolean shouldUseCtrlSpaceWorkaround() {
    return false;
  }

  @Override
  public boolean isTerminalViewSelected() {
    return true;
  }

  @Override
  public void copyModeChanged(boolean copyMode) {}

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent e, TerminalSession session) {
    if (keyCode == KeyEvent.KEYCODE_ENTER && !session.isRunning()) {
      finish();
      return true;
    }
    return false;
  }

  @Override
  public boolean onLongPress(MotionEvent event) {
    return false;
  }

  @Override
  public boolean readControlKey() {
    Boolean state = binding.virtualKeyTable.readSpecialButton(SpecialButton.CTRL, true);
    return state != null && state;
  }

  @Override
  public boolean readAltKey() {
    Boolean state = binding.virtualKeyTable.readSpecialButton(SpecialButton.ALT, true);
    return state != null && state;
  }

  @Override
  public boolean readShiftKey() {
    return false;
  }

  @Override
  public boolean readFnKey() {
    return false;
  }

  @Override
  public boolean onCodePoint(int codePoint, boolean ctrlDown, TerminalSession session) {
    return false;
  }

  @Override
  public void onEmulatorSet() {
    setTerminalCursorBlinkingState(true);

    if (session != null) {
      binding
          .getRoot()
          .setBackgroundColor(
              session.getEmulator().mColors.mCurrentColors[TextStyle.COLOR_INDEX_BACKGROUND]);
    }
  }

  @Override
  public void logError(String tag, String message) {
    LOG.error(tag + ":", message);
  }

  @Override
  public void logWarn(String tag, String message) {
    LOG.warn(tag + ":", message);
  }

  @Override
  public void logInfo(String tag, String message) {
    LOG.info(tag + ":", message);
  }

  @Override
  public void logDebug(String tag, String message) {
    LOG.debug(tag + ":", message);
  }

  @Override
  public void logVerbose(String tag, String message) {
    LOG.verbose(tag + ":", message);
  }

  @Override
  public void logStackTraceWithMessage(String tag, String message, Exception e) {
    LOG.error(tag + ":", message, e);
  }

  @Override
  public void logStackTrace(String tag, Exception e) {
    LOG.error(tag + ":", e);
  }

  private void setTerminalCursorBlinkingState(boolean start) {
    if (terminal != null && terminal.mEmulator != null) {
      terminal.setTerminalCursorBlinkerState(start, true);
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    final var bash = new File(BIN_DIR, "bash");
    final var useSystemShell = getUseSystemShell();
    if ((PREFIX.exists()
            && PREFIX.isDirectory()
            && bash.exists()
            && bash.isFile()
            && bash.canExecute())
        || useSystemShell) {
      setupTerminalView();
    } else {
      LOG.info("Bootstrap is not installed.");

      // Show the progress sheet
      final var progress = new ProgressSheet();
      progress.setShowShadow(false);
      progress.setSubMessageEnabled(true);
      progress.setShowTitle(false);
      progress.setMessage(getString(R.string.please_wait));
      progress.setSubMessage(getString(R.string.msg_reading_bootstrap));
      progress.setCancelable(false);
      progress.show(getSupportFragmentManager(), "extract_bootstrap_progress");

      LOG.debug("Starting installation...");
      // Install bootstrap asynchronously
      final var future =
          BootstrapInstaller.doInstall(
              this, message -> runOnUiThread(() -> progress.setSubMessage(message)));

      future.whenComplete(
          (voidResult, throwable) -> {
            LOG.debug("Completable future has been complete.", throwable);

            runOnUiThread(
                () -> {
                  progress.dismissAllowingStateLoss();

                  if (future.isCompletedExceptionally() || throwable != null) {
                    LOG.error("Future has been completed exceptionally.");
                    showInstallationError(throwable);
                    return;
                  }

                  setupTerminalView();
                });
          });
    }
  }

  @Override
  protected View bindLayout() {
    binding = ActivityTerminalBinding.inflate(getLayoutInflater());
    return binding.getRoot();
  }

  private void showInstallationError(Throwable throwable) {

    binding.virtualKeyTable.setVisibility(View.GONE);

    final var frag =
        CrashReportFragment.newInstance(
            getString(R.string.title_installation_failed),
            getString(R.string.msg_bootstrap_installation_error),
            ThrowableUtils.getFullStackTrace(throwable),
            false);

    getSupportFragmentManager()
        .beginTransaction()
        .replace(binding.root.getId(), frag, "bootstrap_install_error_fragment")
        .addToBackStack(null)
        .commit();
  }

  private void setupTerminalView() {
    setFontVariables();
    terminal = new TerminalView(this, null);
    terminal.setTerminalViewClient(this);
    terminal.attachSession(createSession(getWorkingDirectory()));
    terminal.setKeepScreenOn(true);
    terminal.setTextSize(getFontSize());
    terminal.setTypeface(TypefaceUtils.jetbrainsMono());

    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, 0);
    params.weight = 1f;

    binding.getRoot().addView(terminal, 0, params);
    try {
      binding.virtualKeyTable.setVirtualKeysViewClient(getKeyListener());
      binding.virtualKeyTable.reload(
          new VirtualKeysInfo(
              Constants.VIRTUAL_KEYS, "", VirtualKeysConstants.CONTROL_CHARS_ALIASES));
    } catch (JSONException e) {
      LOG.error("Unable to parse terminal virtual keys json data", e);
    }
  }

  @NonNull
  private String getWorkingDirectory() {
    final Bundle extras = getIntent().getExtras();
    if (extras != null && extras.containsKey(KEY_WORKING_DIRECTORY)) {
      String directory = extras.getString(KEY_WORKING_DIRECTORY, null);
      if (directory == null || directory.trim().length() <= 0) {
        directory = HOME.getAbsolutePath();
      }
      return directory;
    }
    return HOME.getAbsolutePath();
  }

  private KeyListener getKeyListener() {
    return listener == null ? listener = new KeyListener(terminal) : listener;
  }

  private TerminalSession createSession(final String workingDirectory) {
    final Map<String, String> environment = getEnvironment();
    final String[] env = new String[environment.size()];
    int i = 0;
    for (Map.Entry<String, String> entry : environment.entrySet()) {
      env[i] = entry.getKey() + "=" + entry.getValue();
      i++;
    }

    session =
        new TerminalSession(
            getShellPath(), // Shell command
            workingDirectory, // Working directory
            new String[] {}, // Arguments
            env, // Environment variables
            TerminalEmulator.DEFAULT_TERMINAL_TRANSCRIPT_ROWS, // Transcript rows
            this // TerminalSessionClient
            );

    try {
      final var file = new File(PREFIX, "etc/apt/sources.list");
      final var out = new FileOutputStream(file);
      out.write(SOURCES_LIST_CONTENT);
      out.flush();
      out.close();
    } catch (Throwable th) {
      LOG.error("Unable to update sources.list", th);
    }

    return session;
  }

  @NonNull
  private String getShellPath() {
    final var useSystemShell = GeneralPreferencesKt.getUseSystemShell();
    if (!useSystemShell && LOGIN_SHELL.exists() && LOGIN_SHELL.isFile()) {
      return LOGIN_SHELL.getAbsolutePath();
    }

    if (!useSystemShell) {
      LOG.error(
          "Default shell does not exist. Falling back to '/system/bin/sh'.",
          "This should not happen in normal circumstances.");
    }

    return "/system/bin/sh";
  }

  public void setFontVariables() {
    int[] sizes = getDefaultFontSizes();

    DEFAULT_FONT_SIZE = sizes[0];
    MIN_FONT_SIZE = sizes[1];
    MAX_FONT_SIZE = sizes[2];
  }

  // https://github.com/termux/termux-app/blob/82b15803126138eef8899e0c7b582713f872cd09/termux-shared/src/main/java/com/termux/shared/termux/settings/preferences/TermuxAppSharedPreferences.java
  private int[] getDefaultFontSizes() {
    float dipInPixels =
        TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, 1, getResources().getDisplayMetrics());

    int[] sizes = new int[3];

    // This is a bit arbitrary and sub-optimal. We want to give a sensible default for minimum font
    // size
    // to prevent invisible text due to zoom be mistake:
    sizes[1] = (int) (4f * dipInPixels); // min

    // http://www.google.com/design/spec/style/typography.html#typography-line-height
    int defaultFontSize = Math.round(9 * dipInPixels);
    // Make it divisible by 2 since that is the minimal adjustment step:
    if (defaultFontSize % 2 == 1) defaultFontSize--;

    sizes[0] = defaultFontSize; // default

    sizes[2] = 256; // max

    if (getApp()
        .getPrefManager()
        .getString(KEY_FONT_SIZE, "<not_available>")
        .equals("<not_available>")) {
      setFontSize(defaultFontSize, false);
    }

    return sizes;
  }

  @Override
  protected void onResume() {
    super.onResume();
    setTerminalCursorBlinkingState(true);
  }

  @Override
  protected void onStart() {
    super.onStart();
    isVisible = true;
  }

  @Override
  protected void onStop() {
    super.onStop();
    isVisible = false;
    setTerminalCursorBlinkingState(false);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    binding = null;
  }

  private static final class KeyListener implements VirtualKeysView.IVirtualKeysView {

    private final TerminalView terminal;

    public KeyListener(TerminalView terminal) {
      this.terminal = terminal;
    }

    @Override
    public void onVirtualKeyButtonClick(View view, VirtualKeyButton buttonInfo, Button button) {
      if (terminal == null) {
        return;
      }
      if (buttonInfo.isMacro()) {
        String[] keys = buttonInfo.getKey().split(" ");
        boolean ctrlDown = false;
        boolean altDown = false;
        boolean shiftDown = false;
        boolean fnDown = false;
        for (String key : keys) {
          if (SpecialButton.CTRL.getKey().equals(key)) {
            ctrlDown = true;
          } else if (SpecialButton.ALT.getKey().equals(key)) {
            altDown = true;
          } else if (SpecialButton.SHIFT.getKey().equals(key)) {
            shiftDown = true;
          } else if (SpecialButton.FN.getKey().equals(key)) {
            fnDown = true;
          } else {
            onTerminalExtraKeyButtonClick(key, ctrlDown, altDown, shiftDown, fnDown);
            ctrlDown = false;
            altDown = false;
            shiftDown = false;
            fnDown = false;
          }
        }
      } else {
        onTerminalExtraKeyButtonClick(buttonInfo.getKey(), false, false, false, false);
      }
    }

    private void onTerminalExtraKeyButtonClick(
        String key, boolean ctrlDown, boolean altDown, boolean shiftDown, boolean fnDown) {
      if (VirtualKeysConstants.PRIMARY_KEY_CODES_FOR_STRINGS.containsKey(key)) {
        Integer keyCode = VirtualKeysConstants.PRIMARY_KEY_CODES_FOR_STRINGS.get(key);
        if (keyCode == null) {
          return;
        }
        int metaState = 0;
        if (ctrlDown) {
          metaState |= KeyEvent.META_CTRL_ON | KeyEvent.META_CTRL_LEFT_ON;
        }
        if (altDown) {
          metaState |= KeyEvent.META_ALT_ON | KeyEvent.META_ALT_LEFT_ON;
        }
        if (shiftDown) {
          metaState |= KeyEvent.META_SHIFT_ON | KeyEvent.META_SHIFT_LEFT_ON;
        }
        if (fnDown) {
          metaState |= KeyEvent.META_FUNCTION_ON;
        }

        KeyEvent keyEvent = new KeyEvent(0, 0, KeyEvent.ACTION_UP, keyCode, 0, metaState);
        terminal.onKeyDown(keyCode, keyEvent);
      } else {
        // not a control char
        for (int off = 0; off < key.length(); ) {
          int codePoint = key.codePointAt(off);
          terminal.inputCodePoint(codePoint, ctrlDown, altDown);
          off += Character.charCount(codePoint);
        }
      }
    }

    @Override
    public boolean performVirtualKeyButtonHapticFeedback(
        View view, VirtualKeyButton buttonInfo, Button button) {
      // No need to handle this
      // VirtualKeysView will take care of performing haptic feedback
      return false;
    }
  }
}
