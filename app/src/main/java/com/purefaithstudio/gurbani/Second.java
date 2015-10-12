package com.purefaithstudio.gurbani;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class Second extends ActionBarActivity {
    private Toolbar toolbar;
    TextView t1, t2;
    String largeText, pathText;
    private Bundle b;
    List<String> l;
    private ListView list;
    private MyListAdapter arrayadapter;
    private int title;
    private String fontUrl = "fonts/AnmolLipi2.ttf";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);


        b = getIntent().getExtras();
        title = b.getInt("key3");
        largeText = b.getString("key1");
        pathText = b.getString("key2");

        String[] strings = pathText.split(",");
        list = (ListView) findViewById(R.id.list_path);
        arrayadapter = new MyListAdapter(this, R.layout.custom_list, strings);
        list.setAdapter(arrayadapter);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        switch (MainActivity.font) {
            case "hindi":
                fontUrl = "fonts/GurbaniHindi.ttf";
                break;
            case "punjabi":
                fontUrl = "fonts/AnmolLipi2.ttf";
                break;
        }
        t2 = (TextView) findViewById(R.id.large_text2);
        if (MainActivity.font != "roman") {
            Typeface typeface = Typeface.createFromAsset(getAssets(), fontUrl);
            t2.setTypeface(typeface);
        }
        // t1 = (TextView) findViewById(R.id.textview2);

        //t1.setTypeface(typeface);

        t2.setText(largeText);
        //t1.setText(pathText);
        setSupportActionBar(toolbar);
        setTitle(title);
        //getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
