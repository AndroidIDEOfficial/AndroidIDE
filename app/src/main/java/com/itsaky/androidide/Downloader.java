package com.itsaky.androidide;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;



import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;


public class Downloader extends AsyncTask<String, Integer, Boolean> {
    long  downloaded,total_size;
    public static final double SPACE_KB = 1024;
    public static final double SPACE_MB = 1024 * SPACE_KB;
    public static final double SPACE_GB = 1024 * SPACE_MB;
    public static final double SPACE_TB = 1024 * SPACE_GB;


    private ProgressDialog bar;

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    
    @SuppressLint("StaticFieldLeak")
    private final Activity activity;
	private ArrayList<String> DownloadList;
    private String PATH;
	private String FileName;
	private int pos;

    public Downloader(Context context, Activity activity, ProgressDialog bar, ArrayList<String> DownloadList) {
        this.context = context;
        this.activity = activity;
        this.bar = bar;
        this.DownloadList = DownloadList;
        bar.setMax(100);
        bar.setTitle("Downloading...");
        bar.setProgressNumberFormat(null);
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        Log.d("AAAT", "doInBackGround: ");
        boolean flag=false;
		for(String link : DownloadList){
        try {
			pos=DownloadList.indexOf(link);
            URL url = new URL(checkURLRedirection(link));
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(false);
            c.connect();
PATH=context.getApplicationContext().getFilesDir()+"/home/";
            File file = new File(PATH);
            file.mkdirs();
			FileName=c.getHeaderField("Content-Disposition").split("=")[1];
            File outputFile = new File(file, FileName);
            if (outputFile.exists()) {
                outputFile.delete();
            }
            FileOutputStream fos = new FileOutputStream(outputFile);
            InputStream is = c.getInputStream();
            total_size = c.getContentLengthLong();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            int per = 0;
            downloaded  = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
                downloaded += len1;
                per = (int) (downloaded * 100 / total_size);
                publishProgress(per);
            }
            fos.close();
            is.close();
            flag = true;
        } catch (Exception e) {
            Log.e("AAAT", "Download Error: " + e.getMessage());
            e.printStackTrace();
            flag = false;
			break;
        }
	  }
        return flag;
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
        int statusCode = huc.getResponseCode(); //get response code
        if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP || statusCode == HttpURLConnection.HTTP_MOVED_PERM) { // if file is moved, then pick new URL
            url_string = huc.getHeaderField("Location");
            url_string = url_string.replaceFirst("http", "https");
        }
        return url_string;
    }

    @Override
    protected void onPreExecute() {
        Log.d("AAAT", "onPreExecute: ");
        super.onPreExecute();
        bar.setCancelable(false);
        bar.setCanceledOnTouchOutside(false);
        bar.setTitle("Connecting");
        bar.setIndeterminate(true);
        bar.show();

    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        Log.d("AAAT", "onProgressUpdate: " + progress[0]);
        super.onProgressUpdate(progress);
        bar.setProgressNumberFormat((bytes2String(downloaded)) + "/" + (bytes2String(total_size)));
        bar.setIndeterminate(false);
        bar.setProgress(progress[0]);
        String msg;
        if (progress[0] > 99) {
            msg = "Finishing... ";
        } else {
            msg = "Downloading "+pos+"/"+DownloadList.size();
        }
        bar.setTitle(msg);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        Log.d("AAAT", "onPostExecute: ");
        super.onPostExecute(result);
		if(bar.isShowing())bar.dismiss();
        if (result) {
            Toast.makeText(activity.getApplicationContext(), "Done!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(activity.getApplicationContext(),"Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
