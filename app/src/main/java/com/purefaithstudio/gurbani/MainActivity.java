package com.purefaithstudio.gurbani;

import android.app.Fragment;
import android.app.FragmentManager;
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
import com.tapjoy.TJPlacement;
import com.tapjoy.Tapjoy;
import com.tapjoy.TapjoyConnectFlag;

import java.util.Hashtable;

public class MainActivity extends ActionBarActivity {
    public static String font = "punjabi";
    public TextView text;
    DrawerLayout mdrawerLayout;
    Fragment fragment;
    String[] values = {"pwT", "lweIv gurbwxI","inaUj"};
    TJPlacement p;
    private Toolbar toolbar;
    private ListView list;
    private TextView t;
    private FragmentManager fragmentManager;
    private java.util.Hashtable<String, Object> connectFlags = new Hashtable();
    private TextView textnew;
    private TextView textnew2;
    private boolean flag = true;
    static InterstitialAd interstitialAd;
    public static int height;
    private Display display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
        display=getWindowManager().getDefaultDisplay();
        //fragment setup
        fragmentManager = getFragmentManager();
        fragment = new Fragment1();
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
                Fragment3.display=display;
                break;
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
       /*Intent playService = new Intent(this, MyService.class);
       stopService(playService);
    */

    }





   /* @Override
    public void itemClicked(View view, int position) {
        if (position == 1)
            startActivity(new Intent(this, Second.class));
        if (position == 2)
            //startActivity(new Intent(this, Third.class));
    }*/

}
