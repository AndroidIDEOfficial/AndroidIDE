package com.itsaky.androidide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.blankj.utilcode.util.ThreadUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class DownloaderTest {
    long downloaded, total_size;
    public static final double SPACE_KB = 1024;
    public static final double SPACE_MB = 1024 * SPACE_KB;
    public static final double SPACE_GB = 1024 * SPACE_MB;
    public static final double SPACE_TB = 1024 * SPACE_GB;

    private ProgressDialog bar;

    @SuppressLint("StaticFieldLeak")
    private final Context context;

    @SuppressLint("StaticFieldLeak")
    private final Activity activity;

    private final ArrayList<String> DownloadList;
    private CountDownLatch countDownLatch;
    public DownloaderTest(
        Context context,
        Activity activity,
        ProgressDialog bar,
        ArrayList<String> DownloadList) {
        this.context = context;
        this.activity = activity;
        this.bar = bar;
        this.DownloadList = DownloadList;
        bar.setMax(100);
        bar.setTitle("Downloading...");
        bar.setProgressNumberFormat(null);
        countDownLatch = new CountDownLatch(1);
    }
    public void install() throws InterruptedException {
        new Thread (()->{
boolean result = downloadFiles();
});
        if (bar.isShowing()) bar.dismiss();
        if (result) {
            Toast.makeText(activity.getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity.getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean downloadFiles() throws InterruptedException {
      ThreadUtils.runOnUiThread(()->{
	bar.setCancelable(false);
        bar.setCanceledOnTouchOutside(false);
        bar.setTitle("Connecting");
        bar.setIndeterminate(true);
        bar.show();
});
        final AtomicBoolean flag = new AtomicBoolean(false);
        new Thread(()-> {
            for (String link : DownloadList) {
                try {
                    int pos = DownloadList.indexOf(link);
                    URL url = new URL(checkURLRedirection(link));
                    HttpURLConnection c = (HttpURLConnection) url.openConnection();
                    c.setRequestMethod("GET");
                    c.setDoOutput(false);
                    c.connect();
                    String PATH = context.getFilesDir() + "/home/";
                    File file = new File(PATH);
                    file.mkdirs();
                    String fileName = c.getHeaderField("Content-Disposition").split("=")[1];
                    File outputFile = new File(file, fileName);
                    if (outputFile.exists()) {
                        outputFile.delete();
                    }
                    FileOutputStream fos = new FileOutputStream(outputFile);
                    InputStream is = c.getInputStream();
                    total_size = c.getContentLengthLong();
                    byte[] buffer = new byte[1024];
                    int len1 = 0;
                    int per = 0;
                    downloaded = 0;
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);
                        downloaded += len1;
                        per = (int) (downloaded * 100 / total_size);
                        final int finalPer = per;
                        ThreadUtils.runOnUiThread(()-> {
                            bar.setProgressNumberFormat((bytes2String(downloaded)) + "/" + (bytes2String(total_size)));
                            bar.setIndeterminate(false);
                            bar.setProgress(finalPer);
                            String msg;
                            if (finalPer > 99) {
                                msg = "Finishing... ";
                            } else {
                                msg = "Downloading " + pos + "/" + DownloadList.size();
                            }
                            bar.setTitle(msg);
                        });
                    }
                    fos.close();
                    is.close();
                    flag.set(true);
                } catch (Exception e) {
                    Log.e("AAAT", "Download Error: " + e.getMessage());
                    e.printStackTrace();
                    flag.set(false);
                    break;
                }
            }
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();
        return flag.get();
    }

    public static String bytes2String(long sizeInBytes) {

        NumberFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(2);
        try {
            if (sizeInBytes < SPACE_KB) {
                return nf.format(sizeInBytes) + " Byte(s)";
            } else if (sizeInBytes < SPACE_MB) {
                return nf.format(sizeInBytes / SPACE_KB) + " KB";
            } else if (sizeInBytes < SPACE_GB) {
                return nf.format(sizeInBytes / SPACE_MB) + " MB";
            } else if (sizeInBytes < SPACE_TB) {
                return nf.format(sizeInBytes / SPACE_GB) + " GB";
            } else {
                return nf.format(sizeInBytes / SPACE_TB) + " TB";
            }
        } catch (Exception e) {
            return sizeInBytes + " Byte(s)";
        }
    }

    private String checkURLRedirection(String url_string) throws IOException {
        URL url = new URL(url_string);
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        int statusCode = huc.getResponseCode(); // get response code
        if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP
            || statusCode
            == HttpURLConnection
            .HTTP_MOVED_PERM) { // if file is moved, then pick new URL
            url_string = huc.getHeaderField("Location");
            url_string = url_string.replaceFirst("http", "https");
        }
        return url_string;
    }
}
