package com.purefaithstudio.gurbani;

import android.app.Fragment;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by MY System on 4/1/2015.
 */
public class Fragment3 extends Fragment {
    public static Display display;
    View rootView;
    private LinearLayoutManager layoutManager;
    private RecyclerView recyclerView;
    private MyArrayAdapter arrayAdapter;
    private WebView webView;


    public Fragment3() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment3, container, false);
        webView = (WebView) rootView.findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
       // webView.setInitialScale(getScale());
        //webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        /*webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setInitialScale(1);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setScrollbarFadingEnabled(false);
        webView.setWebViewClient(new MyWeViewClient());
        webView.loadUrl("http://www.sikhnet.com/sikhnet/news.nsf/newsscrollerwide");*/
        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=old.sgpc.net/hukumnama/jpeg%20hukamnama/hukamnama.pdf");
        MainActivity.setTrackerScreenName("News");
        return rootView;
    }

    private int getScale() {
        int scale;
        int width=display.getWidth();
        scale=width/100;//thik hai ya kya karu
        return scale;
    }



    private class MyWeViewClient extends WebViewClient {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            float scale=webView.getContentHeight()/480;
            //webView.loadUrl("javascript:document.body.style.zoom = " + String.valueOf(scale) + ";");
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
