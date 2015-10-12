package com.purefaithstudio.gurbani;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class Navigationdrawer extends Fragment {
  public static final String PREF_FILE="testpref";
    public static final String KEY_USER_LEARNED_DRAWER="user_learned_drawer";
 private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mdrawerLayout;
    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View contextView;
    ListView listView;
    ArrayList<String> arr;

    public Navigationdrawer() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     mUserLearnedDrawer=Boolean.valueOf(readfromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));
        if(savedInstanceState!=null)
            mFromSavedInstanceState=true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_navigationdrawer, container, false);
    }


    public void setUp(int fragment,DrawerLayout drawerLayout,Toolbar toolbar) {
        contextView=getActivity().findViewById(fragment);
        mdrawerLayout=drawerLayout;
        mDrawerToggle=new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if(!mUserLearnedDrawer){
                    mUserLearnedDrawer=true;
                    saveToPreferences(getActivity(),KEY_USER_LEARNED_DRAWER,mUserLearnedDrawer+"");
                }
                    getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };
        if(!mUserLearnedDrawer && !mFromSavedInstanceState)
        {
            mdrawerLayout.openDrawer(contextView);
        }
        mdrawerLayout.setDrawerListener(mDrawerToggle);
        mdrawerLayout.post(new Runnable() {
            @Override
            public void run() {
               mDrawerToggle.syncState();
            }
        });
    }

    public static void saveToPreferences(Context context,String preferenceName,String preferenceValue)
    {
        SharedPreferences sharedpreferences=context.getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedpreferences.edit();
        editor.putString(preferenceName,preferenceValue);
        editor.apply();

    }
    public static String readfromPreferences(Context context,String preferenceName,String defaultValue)
    {
        SharedPreferences sharedpreferences=context.getSharedPreferences(PREF_FILE,Context.MODE_PRIVATE);
        return sharedpreferences.getString(preferenceName,defaultValue);

    }
}
