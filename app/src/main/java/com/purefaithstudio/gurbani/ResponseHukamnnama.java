package com.purefaithstudio.gurbani;

import android.os.AsyncTask;
import android.webkit.WebView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by MY System on 12/12/2015.
 */
public class ResponseHukamnnama extends AsyncTask<URL, WebView, Long> {


    WebView webView;
    private String data;

    public ResponseHukamnnama(WebView webView) {
        this.webView = webView;
    }

    @Override
    protected Long doInBackground(URL... params) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                data = getData();
            }
        });
        thread.start();
        return null;
    }

    @Override
    protected void onPostExecute(Long aLong) {
        super.onPostExecute(aLong);
        //loadView();
    }

    private String getData() {
        String link = "http://swservice.azurewebsites.net/ItineraryService.svc/GetHukumnamaForDate/05-12-15?callback=hukumnamadatecallback";
        StringBuilder data = new StringBuilder();
        InputStream inputStream = null;
        try {
            URL url = new URL(link);
            inputStream = url.openStream();
            int c;
            while ((c = inputStream.read()) != -1) {
                data.append((char) c);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data.toString();
    }
}
