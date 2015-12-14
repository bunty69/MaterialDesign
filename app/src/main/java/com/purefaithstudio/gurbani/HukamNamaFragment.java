package com.purefaithstudio.gurbani;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by MY System on 12/13/2015.
 */
public class HukamNamaFragment extends Fragment {
    private View rootView;
    private WebView webView;
    private boolean dataLoaded;
    private boolean firstTimeLoad;
    private String data = " ";
    private Wait wait;
    private String link;
    private RelativeLayout controller;
    private ImageView image;
    private String date;
    private String Audio_url;
    private boolean togglePlay;
    private boolean pause;
    private boolean serviceStarted;
    private Wait wait2;
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            wait2.dismiss();
            if (!serviceStarted)
                serviceStarted = true;
            if (Mp3PlayerService.oncomplete)
                serviceStarted = false;
        }
    };
    private Context context;
    private Intent intent;
    private ActionBar toolbar;

    public HukamNamaFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
        try {
            link = this.getArguments().getString("link");
            date = this.getArguments().getString("date");
            date = date.replaceAll("-", "");
            Audio_url = "http://old.sgpc.net/audio/SGPCNET" + date + ".mp3";
            System.out.println(Audio_url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        wait = new Wait();
        wait2 = new Wait();
        intent = new Intent(getActivity().getApplicationContext(), Mp3PlayerService.class);
        if (NetworkConnectionDetector.isConnectingToInternet(context))
            wait.show(getFragmentManager(), "tag3");
        else
            Toast.makeText(context, "No Internet Connection!!", Toast.LENGTH_SHORT).show();
        toolbar = ((MainActivity) getActivity()).getSupportActionBar();
        toolbar.hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.hukamnama_fragment, null);
        controller = (RelativeLayout) rootView.findViewById(R.id.controllerView);
        image = (ImageView) rootView.findViewById(R.id.play);
        webView = (WebView) rootView.findViewById(R.id.webview);
        webView.setWebViewClient(new MyWeViewClient());
        rootView.getContext().registerReceiver(receiver, new IntentFilter("com.purefaithstudio.gurbani.Mp3Player"));
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (NetworkConnectionDetector.isConnectingToInternet(getActivity().getApplicationContext())) {
                    try {
                        if (!togglePlay) {
                            togglePlay = true;
                            if (pause || !serviceStarted)
                                playAudio();
                        } else {
                            togglePlay = false;
                            if (serviceStarted)
                                stopAudio();
                        }

                    } catch (Exception e) {
                        Log.i("AppNitnem", "cannot play or stop");
                        e.printStackTrace();
                    }
                } else
                    Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection!!", Toast.LENGTH_SHORT).show();
            }
        });


        return rootView;
    }

    private void playAudio() {
        if (!pause) {
            Bundle b = new Bundle();
            b.putString("url", Audio_url);
            intent.putExtras(b);
            wait2.show(getFragmentManager(), "tag");
            rootView.getContext().startService(intent);
            image.setImageResource(R.drawable.stop_blue);
        } else {
            image.setImageResource(R.drawable.stop_blue);
            Mp3PlayerService.player.start();
            pause = false;
        }

    }

    private void stopAudio() {
        Mp3PlayerService.player.pause();
        pause = true;
        image.setImageResource(R.drawable.play);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (NetworkConnectionDetector.isConnectingToInternet(context)) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    data = getData();
                    dataLoaded = true;
                }
            });
            thread.start();
            loadView();
        } else Toast.makeText(context, "No Internet Connection!!", Toast.LENGTH_SHORT).show();
    }

    private void loadView() {
        if (data.equals(" ")) {
            Log.i("Hukum", "Data IS empty");
            webView.loadDataWithBaseURL("", " ", "text/html", "utf-8", null);
        } else {
            Log.i("Hukum", "Data is Filled");
            int size = data.length();
            String pish = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "<link rel=\"stylesheet\" type=\"text/css\" href=\"file:///android_asset/css/mobile.css\">\n" +
                    "</head>\n" +
                    "<body>";
            String pas = "</body></html>";
            data = data.substring(23, size - 4);
            data = data.replaceAll("\\\\", "");
            data = data.replaceAll("Æ", "");
            Log.i("Hukum", data);
            firstTimeLoad = true;
            webView.loadDataWithBaseURL("", pish + data + pas, "text/html", "utf-8", null);
            wait.dismiss();
        }
    }

    private String getData() {
        // String link = "http://swservice.azurewebsites.net/ItineraryService.svc/GetHukumnamaForDate/05-12-15?callback=hukumnamadatecallback";
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        rootView.getContext().unregisterReceiver(receiver);
        if (serviceStarted)
            context.stopService(intent);
        toolbar.show();
    }

    private class MyWeViewClient extends WebViewClient {

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.i("Hukum", "OnPageFinished");
            if (!dataLoaded) {
                loadView();
                Log.i("Hukum", "DataLoaded");
            } else {
                if (!firstTimeLoad)
                    loadView();
            }

            //webView.loadUrl("javascript:document.body.style.zoom = " + String.valueOf(scale) + ";");
        }

    }

}
