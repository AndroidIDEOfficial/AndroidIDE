package com.itsaky.androidide;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import androidx.appcompat.widget.TooltipCompat;
import com.itsaky.androidide.app.StudioActivity;
import com.itsaky.androidide.databinding.ActivityAboutBinding;
import com.itsaky.androidide.databinding.LayoutAboutItemsBinding;
import com.itsaky.androidide.models.License;
import com.itsaky.androidide.tasks.TaskExecutor;
import com.itsaky.androidide.tasks.callables.LicenseReader;
import java.util.List;
import java.util.ArrayList;

public class AboutActivity extends StudioActivity {

    private ActivityAboutBinding binding;
    
    public static final String JDK_SOURCE = "https://github.com/AdoptOpenJDK/openjdk-jdk11u";
    
    @Override
    protected View bindLayout() {
        binding = ActivityAboutBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        binding.items.footerText.setText(getFooter());
        
        new TaskExecutor().executeAsync(new LicenseReader(this), __ -> {
            setupLicenses(__);
        });
        
        LayoutAboutItemsBinding items = binding.items;
        TooltipCompat.setTooltipText(items.discuss, getString(R.string.discussions_on_telegram));
        TooltipCompat.setTooltipText(items.issueTracker, getString(R.string.user_suggestions));
        TooltipCompat.setTooltipText(items.email, getString(R.string.about_option_email));
        TooltipCompat.setTooltipText(items.website, getString(R.string.about_option_website));
        items.issueTracker.setOnClickListener(v -> {
            getApp().openIssueTracker();
        });
        items.discuss.setOnClickListener(v -> {
            getApp().openTelegramGroup();
        });
        items.email.setOnClickListener(v -> {
            getApp().emailUs();
        });
        items.website.setOnClickListener(v -> {
            getApp().openWebsite();
        });
    }
    
    private void setupLicenses(List<License> licenses) {
        if(licenses == null) {
            licenses = new ArrayList<>();
        } else {
            licenses.add(new License("Gradle Build Tool", "Apache License 2.0", "https://github.com/gradle/gradle"));
            StringBuilder sb = new StringBuilder();
            for(License license : licenses) {
                sb.append("\u2022 ");
                sb.append(license.name);
                sb.append("<br>");
                sb.append(getString(R.string.msg_about_licensed_under));
                sb.append(license.license);
                sb.append("<br><a href=\"");
                sb.append(license.url);
                sb.append("\">");
                sb.append(license.url);
                sb.append("</a>");
                sb.append("<br><br>");
            }
            sb.append(getString(R.string.license_jdk));
            sb.append(String.format("<br><a href=\"%1$s\">%1$s</a>", JDK_SOURCE));
            binding.items.licenses.setText(Html.fromHtml(sb.toString()));
            binding.items.licenses.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
    
    private String getFooter() {
        final String arch = android.os.Build.SUPPORTED_ABIS[0];
        try {
            final String version = getPackageManager().getPackageInfo("com.itsaky.androidide", 0).versionName;
            return getString(R.string.about_footer, version, arch);
        } catch (Throwable th) {
            return getString(R.string.about_footer_alternate, arch);
        }
    }
}
