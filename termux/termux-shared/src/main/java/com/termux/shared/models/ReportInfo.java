package com.termux.shared.models;

import com.termux.shared.markdown.MarkdownUtils;
import com.termux.shared.android.AndroidUtils;

import java.io.Serializable;

/**
 * An object that stored info for {@link com.termux.shared.activities.ReportActivity}.
 */
public class ReportInfo implements Serializable {

    /** The user action that was being processed for which the report was generated. */
    public final String userAction;
    /** The internal app component that sent the report. */
    public final String sender;
    /** The report title. */
    public final String reportTitle;
    /** The timestamp for the report. */
    public final String reportTimestamp;

    /** The markdown report text prefix. Will not be part of copy and share operations, etc. */
    public String reportStringPrefix;
    /** The markdown report text. */
    public String reportString;
    /** The markdown report text suffix. Will not be part of copy and share operations, etc. */
    public String reportStringSuffix;

    /** If set to {@code true}, then report header info will be added to the report when markdown is
     * generated. */
    public boolean addReportInfoHeaderToMarkdown = false;

    /** The label for the report file to save if user selects menu_item_save_report_to_file. */
    public String reportSaveFileLabel;
    /** The path for the report file to save if user selects menu_item_save_report_to_file. */
    public String reportSaveFilePath;

    public ReportInfo(String userAction, String sender, String reportTitle) {
        this.userAction = userAction;
        this.sender = sender;
        this.reportTitle = reportTitle;
        this.reportTimestamp = AndroidUtils.getCurrentMilliSecondUTCTimeStamp();
    }

    public void setReportStringPrefix(String reportStringPrefix) {
        this.reportStringPrefix = reportStringPrefix;
    }

    public void setReportString(String reportString) {
        this.reportString = reportString;
    }

    public void setReportStringSuffix(String reportStringSuffix) {
        this.reportStringSuffix = reportStringSuffix;
    }

    public void setAddReportInfoHeaderToMarkdown(boolean addReportInfoHeaderToMarkdown) {
        this.addReportInfoHeaderToMarkdown = addReportInfoHeaderToMarkdown;
    }

    public void setReportSaveFileLabelAndPath(String reportSaveFileLabel, String reportSaveFilePath) {
        setReportSaveFileLabel(reportSaveFileLabel);
        setReportSaveFilePath(reportSaveFilePath);
    }

    public void setReportSaveFileLabel(String reportSaveFileLabel) {
        this.reportSaveFileLabel = reportSaveFileLabel;
    }

    public void setReportSaveFilePath(String reportSaveFilePath) {
        this.reportSaveFilePath = reportSaveFilePath;
    }

    /**
     * Get a markdown {@link String} for {@link ReportInfo}.
     *
     * @param reportInfo The {@link ReportInfo} to convert.
     * @return Returns the markdown {@link String}.
     */
    public static String getReportInfoMarkdownString(final ReportInfo reportInfo) {
        if (reportInfo == null) return "null";

        StringBuilder markdownString = new StringBuilder();

        if (reportInfo.addReportInfoHeaderToMarkdown) {
            markdownString.append("## Report Info\n\n");
            markdownString.append("\n").append(MarkdownUtils.getSingleLineMarkdownStringEntry("User Action", reportInfo.userAction, "-"));
            markdownString.append("\n").append(MarkdownUtils.getSingleLineMarkdownStringEntry("Sender", reportInfo.sender, "-"));
            markdownString.append("\n").append(MarkdownUtils.getSingleLineMarkdownStringEntry("Report Timestamp", reportInfo.reportTimestamp, "-"));
            markdownString.append("\n##\n\n");
        }

        markdownString.append(reportInfo.reportString);

        return markdownString.toString();
    }

}
