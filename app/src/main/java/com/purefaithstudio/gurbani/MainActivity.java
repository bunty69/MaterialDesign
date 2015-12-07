package com.purefaithstudio.gurbani;

import android.app.ActivityManager;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.tapjoy.TJPlacement;

public class MainActivity extends ActionBarActivity {
    public static String font = "punjabi";
    public static int height;
    public static App42ManagerService apm;
    static InterstitialAd interstitialAd;
    public TextView text;
    DrawerLayout mdrawerLayout;
    Fragment fragment;
    Fragment1 fragement1temp;
    String[] values = {"pwT", "lweIv gurbwxI", "inaUj","shabad-download"};
    TJPlacement p;
    private Toolbar toolbar;
    private ListView list;
    private TextView t;
    private FragmentManager fragmentManager;
    private Display display;
    private MyApplication myApplication;
    private Tracker tracker;
    private static Tracker globalTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        Log.i("Playercheck", "MainActivity called oncreate");
        display = getWindowManager().getDefaultDisplay();
        //load APM service
        if (!App42ManagerService.flag) {
            apmLoad();
        }
        //fragment setup
        fragmentManager = getFragmentManager();
        fragment = new Fragment1();
        fragement1temp = (Fragment1) fragment;
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        list = (ListView) findViewById(R.id.list_navigation);

        MyListAdapter arrayAdapter = new MyListAdapter(this, R.layout.navigation_list_row, values);
        // list.setCacheColorHint(Color.TRANSPARENT);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                display(position);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Navigationdrawer drawer = (Navigationdrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigationdrawer);
        drawer.setUp(R.id.fragment_navigationdrawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
        myApplication = (MyApplication) getApplication();
        tracker = myApplication.getTracker(MyApplication.TrackerName.APP_TRACKER);
        globalTracker = myApplication.getTracker(MyApplication.TrackerName.GLOBAL_TRACKER);
        globalTracker.enableAdvertisingIdCollection(true);
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(getResources().getString(R.string.interstitial_ad_id));
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
                Log.i("admob", "failed");
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.i("admob", "loaded");
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                Log.i("admob", "closed");
                AdRequest adRequest = new AdRequest.Builder().addTestDevice("ACCD210AA5526186C01EC1A5372676C6")
                        .build();
                Log.i("admob", "requested");
                interstitialAd.loadAd(adRequest);
            }
        });
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("ACCD210AA5526186C01EC1A5372676C6")
                .build();
        Log.i("admob", "requested");
        interstitialAd.loadAd(adRequest);
    }

    private void apmLoad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                apm = new App42ManagerService(MainActivity.this);
            }
        }).start();
    }

    private void display(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:
                setTitle("Paath");
                fragment = new Fragment1();
                break;
            case 1:
                setTitle("Live Gurbani");
                fragment = new Fragment2();
                break;
            case 2:
                fragment = new Fragment3();
                Fragment3.display = display;
                break;
            case 3:
                fragment=new Fragment4();
            default:
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();

        } else {
            Log.e("MainActivity", "Error in creating Fragments....Harjas!!");
        }

        //setTitle(values[position]);//should be in punjabi
        mdrawerLayout.closeDrawers();

    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.punjabi) {
            font = "punjabi";
            setUpAgainLanguageChanged();
        }
        if (id == R.id.hindi) {
            //change language
            font = "hindi";
            setUpAgainLanguageChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpAgainLanguageChanged() {
        setTitle("Paath");
        fragment = new Fragment1();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        MyListAdapter arrayAdapter = new MyListAdapter(this, R.layout.navigation_list_row, values);
        // list.setCacheColorHint(Color.TRANSPARENT);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                display(position);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isMyServiceRunning(Mp3PlayerService.class))
            this.getApplicationContext().stopService(fragement1temp.getIntent());
       /*Intent playService = new Intent(this, MyService.class);
       stopService(playService);
    */

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) this.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

public static void setTrackerScreenName(String path)
{
    globalTracker.setScreenName(path);
    globalTracker.send(new HitBuilders.AppViewBuilder().build());

}

   /* @Override
    public void itemClicked(View view, int position) {
        if (position == 1)
            startActivity(new Intent(this, Second.class));
        if (position == 2)
            //startActivity(new Intent(this, Third.class));
    }*/

}
