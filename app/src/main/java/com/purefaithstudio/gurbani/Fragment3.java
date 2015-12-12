package com.purefaithstudio.gurbani;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by MY System on 4/1/2015.
 */
public class Fragment3 extends Fragment {
    public static Display display;
    View rootView;
    StringBuilder stringBuilder;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private MyArrayAdapter arrayAdapter;
    private WebView webView;
    private String data;
    private TextView textView;
    private Context context;
    private File folder;
    private String FOLDER_PATH;
    private StringBuilder html;
String style="<style type=\"text/css\">\n" +
        "@font-face {\n" +
        "    font-family: WebAkharThick;\n" +
        "    src: url(\"file:///android_asset/css/WebAkharThick.ttf\")\n" +
        "}\n" +
        "body {\n" +
        "    font-family: WebAkharThick;\n" +
        "    font-size: medium;\n" +
        "    text-align: justify;\n" +
        "}\n" +
        "</style>";
    public Fragment3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment3, container, false);
        webView = (WebView) rootView.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        stringBuilder = new StringBuilder();
        html=new StringBuilder();
        context = container.getContext();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                data = getData();
                Log.i("Hukam", data);
                //saveAsHtml(data);

            }
        });
        thread.start();
        try {
            thread.join();
            int size = data.length();
            data = "<html><head>"+style+"</head><body>"+data.substring(23, size - 4)+"</body></html>";
            Log.i("Hukum", data);
            webView.loadData(data,"text/html","utf-8");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        /*Jsoup jsoup;
        int size = data.length();
        data = "<html><head></head><body>" + data.substring(23, size - 4) + "</body></html>";
        Log.i("Hukum", data);

        Document document = Jsoup.parse(data);        // String string=document.text();
        Element elementBody = document.body();
        Elements elements = elementBody.getAllElements();
        ArrayList<String> arrayList = new ArrayList<>();
        for (Element element : elements) {
            String string = element.text();
            arrayList.add(string);
        }
        textView = (TextView) rootView.findViewById(R.id.textView);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/css/WebAkharThick.ttf");
        textView.setTypeface(typeface, Typeface.BOLD);
        textView.setText(arrayList.get(0));
*/
        MainActivity.setTrackerScreenName("News");
        return rootView;
    }

    private void saveAsHtml(String data) {
        FOLDER_PATH = Environment
                .getExternalStorageDirectory().getAbsolutePath()
                + File.separator + "html";

        folder = new File(FOLDER_PATH);
        if (!folder.exists()) {
            folder.mkdir();
        }
        File file = new File(FOLDER_PATH + File.separator + "file.html");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] bytes = data.getBytes();

            fileOutputStream.write(bytes);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    private int getScale() {
        int scale;
        int width = display.getWidth();
        scale = width / 100;//thik hai ya kya karu
        return scale;
    }


    /*private class MyWeViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            float scale = webView.getContentHeight() / 480;
            //webView.loadUrl("javascript:document.body.style.zoom = " + String.valueOf(scale) + ";");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }*/
}
