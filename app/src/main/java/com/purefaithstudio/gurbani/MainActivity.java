package com.purefaithstudio.gurbani;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.app.Fragment;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.purefaithstudio.gurbani.MyApplication.TrackerName;

import java.util.Hashtable;
import java.util.Locale;
import java.util.Objects;

import com.tapjoy.TJActionRequest;
import com.tapjoy.TJConnectListener;
import com.tapjoy.TJError;
import com.tapjoy.TJPlacement;
import com.tapjoy.TJPlacementListener;
import com.tapjoy.Tapjoy;
import com.tapjoy.TapjoyConnectFlag;

public class MainActivity extends ActionBarActivity implements TJConnectListener {
    private Toolbar toolbar;
    DrawerLayout mdrawerLayout;
    public TextView text;
    Fragment fragment;
    private ListView list;
   public static String font="punjabi";
    String[] values = {"pwT", "lweIv gurbwxI"};
    private TextView t;
    TJPlacement p;
    private FragmentManager fragmentManager;
    private java.util.Hashtable<String,Object> connectFlags=new Hashtable();
    private TextView textnew;
    private TextView textnew2;
    private boolean flag=true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_app);
//tapjoy connect
        connectFlags.put(TapjoyConnectFlag.ENABLE_LOGGING,"true");
        Tapjoy.connect(this.getApplicationContext(), "tv6hb2_LRXqZNEho2eZhMAECs7iPP1M2rFvgx2TbgJg3ykQvNAz-KeVjYinr", connectFlags, this);


        //fragment setup
       fragmentManager = getFragmentManager();
        fragment=new Fragment1();
       fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();

        mdrawerLayout= (DrawerLayout)findViewById(R.id.drawer_layout);
        list = (ListView) findViewById(R.id.list_navigation);
        textnew=(TextView)findViewById(R.id.request);

        textnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag==true)
                showContent();
                else p.showContent();
            }
        });

       MyListAdapter arrayAdapter = new MyListAdapter(this, R.layout.navigation_list_row,values);
       // list.setCacheColorHint(Color.TRANSPARENT);
        list.setAdapter(arrayAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                display(position);
            }
        });
        Tapjoy.setDebugEnabled(true);
        toolbar = (Toolbar) findViewById(R.id.app_bar);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Navigationdrawer drawer = (Navigationdrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigationdrawer);
        drawer.setUp(R.id.fragment_navigationdrawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar);
    }

    private void display(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:setTitle("Paath");
                fragment=new Fragment1();
                break;
            case 1:setTitle("Live Gurbani");
                fragment=new Fragment2();
                break;
            case 2:
                fragment=new Fragment3();
                break;
            default: break;
        }
        if (fragment!=null)
        {  FragmentManager fragmentManager=getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();

        }
        else{
            Log.e("MainActivity","Error in creating Fragments....Harjas!!");
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
           font="punjabi";
           setUpAgainLanguageChanged();
        }
        if(id==R.id.hindi){
            //change language
            font="hindi";
            setUpAgainLanguageChanged();
        }

        return super.onOptionsItemSelected(item);
    }

    private void setUpAgainLanguageChanged() {
        setTitle("Paath");
        fragment=new Fragment1();
        fragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit();
        MyListAdapter arrayAdapter = new MyListAdapter(this, R.layout.navigation_list_row,values);
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

    // called when Tapjoy connect call succeed
    @Override
    public void onConnectSuccess() {
        Log.d("harjas", "Tapjoy connect Succeeded");

    }
    // called when Tapjoy connect call failed
    @Override
    public void onConnectFailure() {
        Log.d("harjas", "Tapjoy connect Failed");
    }


public void showContent(){

     p = new TJPlacement(getApplicationContext(), "Vedio ad",new TJPlacementListener() {
         @Override
         public void onRequestSuccess(TJPlacement tjPlacement) {
        Log.i("harjas","Requested");
         }

         @Override
         public void onRequestFailure(TJPlacement tjPlacement, TJError tjError) {
             Log.i("harjas","Request fail");
         }

         @Override
         public void onContentReady(TJPlacement tjPlacement) {
             Log.i("harjas","Ready");
             flag=false;
         }

         @Override
         public void onContentShow(TJPlacement tjPlacement) {
             Log.i("harjas","content shown");
         }

         @Override
         public void onContentDismiss(TJPlacement tjPlacement) {

         }

         @Override
         public void onPurchaseRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s) {

         }

         @Override
         public void onRewardRequest(TJPlacement tjPlacement, TJActionRequest tjActionRequest, String s, int i) {
          tjActionRequest.completed();
         }
     });
    p.requestContent();

}
   /* @Override
    public void itemClicked(View view, int position) {
        if (position == 1)
            startActivity(new Intent(this, Second.class));
        if (position == 2)
            //startActivity(new Intent(this, Third.class));
    }*/
   //session start
   @Override
   protected void onStart() {
       super.onStart();
       Tapjoy.onActivityStart(this);
   }

    //session end
    @Override
    protected void onStop() {
        Tapjoy.onActivityStop(this);
        super.onStop();
    }
}
