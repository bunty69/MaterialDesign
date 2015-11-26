package com.purefaithstudio.gurbani;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class Second extends ActionBarActivity {
    TextView t1, t2;
    String largeText, pathText;
    List<String> l;
    private Toolbar toolbar;
    private Bundle b;
    private ListView list;
    private MyListAdapter arrayadapter;
    private int title;
    private String fontUrl = "fonts/AnmolLipi2.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        //Header
        View view = LayoutInflater.from(this).inflate(R.layout.header_view, null);
        t2 = (TextView) view.findViewById(R.id.large_text2);
        b = getIntent().getExtras();
        //bundle get
        title = b.getInt("key3");
        largeText = b.getString("key1");
        pathText = b.getString("key2");
        //arrange of raw text
        String[] strings = pathText.split(",");
        list = (ListView) findViewById(R.id.list_path);
        arrayadapter = new MyListAdapter(this, R.layout.custom_list, strings);
        //set header of list
        setTypeFace();
        t2.setText(largeText);
        list.addHeaderView(t2);
        //set adapter
        list.setAdapter(arrayadapter);
        toolbar = (Toolbar) findViewById(R.id.app_bar);


        setSupportActionBar(toolbar);
        setTitle(title);
        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setTypeFace() {
        switch (MainActivity.font) {
            case "hindi":
                fontUrl = "fonts/GurbaniHindi.ttf";
                break;
            case "punjabi":
                fontUrl = "fonts/AnmolLipi2.ttf";
                break;
        }

        if (MainActivity.font != "roman") {
            Typeface typeface = Typeface.createFromAsset(getAssets(), fontUrl);
            t2.setTypeface(typeface);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        b.clear();

    }
}
