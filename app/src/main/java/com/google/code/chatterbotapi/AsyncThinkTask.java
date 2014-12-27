package com.google.code.chatterbotapi;

/**
 * Created by root on 12/21/14.
 */

//private class DownloadFilesTask extends AsyncTask<URL, Integer, Long> {
//    protected Long doInBackground(URL... urls) {
//        int count = urls.length;
//        long totalSize = 0;
//        for (int i = 0; i < count; i++) {
//            totalSize += Downloader.downloadFile(urls[i]);
//            publishProgress((int) ((i / (float) count) * 100));
//            // Escape early if cancel() is called
//            if (isCancelled()) break;
//        }
//        return totalSize;
//    }
//
//    protected void onProgressUpdate(Integer... progress) {
//        setProgressPercent(progress[0]);
//    }
//
//    protected void onPostExecute(Long result) {
//        showDialog("Downloaded " + result + " bytes");
//    }

import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;

import com.example.root.myapplication.R;

public class AsyncThinkTask extends AsyncTask<String,Void,String> {
    @Override
    protected String doInBackground(String... params) {
        createSessions();
        try {
            return SendMessage(params[0]);
        } catch (Exception e) {
            return "ERROR!";
        }
    }

    ChatterBotSession bot1session;
    ChatterBotSession bot2session;
    boolean created=false;
    public void createSessions()
    {
        if (created) return;

        ChatterBotFactory factory = new ChatterBotFactory();

        try {
            ChatterBot bot1 = factory.create(ChatterBotType.CLEVERBOT);
            bot1session = bot1.createSession();

            ChatterBot bot2 = factory.create(ChatterBotType.CLEVERBOT, "b0dafd24ee35a477");
            bot2session = bot2.createSession();
        }
        catch(Exception e)
        {
            System.out.println(e.getMessage());
        }
        created = true;
    }

    boolean alternate= false;
    public String SendMessage(String s) throws Exception {
        createSessions();
        if (alternate)
            s = bot2session.think(s);
        else
            s = bot1session.think(s);
        alternate = !alternate;
        return s;
    }
}
