package com.purefaithstudio.gurbani;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by MY System on 4/1/2015.
 */
public class Fragment3 extends Fragment implements HukumNamaListAdapter.ClickListener, LastSevenDaysDialogFragment.NoticeDialogListener {
    public static Display display;
    View rootView;
    String[] HukumNamaData = {"Today's Hukumnama", "Last 7 days Hukumnama", "Hukumnama by date(Last 2 months)"};
    String domain = "http://swservice.azurewebsites.net/ItineraryService.svc/";
    String serviceFunc = "GetHukumnamaForDate/";
    String date;
    String callback = "hukumnamadatecallback";
    private RecyclerView recyclerView;
    private Bundle bundle;
    private Intent intent;
    private Fragment fragment;
    private LastSevenDaysDialogFragment lastsevenday;
    private Context context;

    public Fragment3() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity().getApplicationContext();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment3, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_list_hukum);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        HukumNamaListAdapter adapter = new HukumNamaListAdapter(getActivity().getApplicationContext(), HukumNamaData);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        bundle = new Bundle();
        intent = new Intent(container.getContext(), HukamNamaFragment.class);
        fragment = new HukamNamaFragment();
        MainActivity.setTrackerScreenName("News");
        return rootView;
    }


    @Override
    public void itemClicked(View view, int position) {
        String link = "";
        switch (position) {
            case 0://today's
                String date = getDate();
                this.date = date;
                link = domain + serviceFunc + date + "?callback=" + callback;
                showFragmentHukumnama(link);
                break;
            case 1://last 7 days
                lastsevenday = new LastSevenDaysDialogFragment();
                lastsevenday.setClicklistener(this);
                lastsevenday.show(getFragmentManager(), "tagdate");
                break;
            case 2:
                break;
        }

    }

    private void showFragmentHukumnama(String link) {
        bundle.putString("link", link);
        bundle.putString("date", date);
        intent.putExtras(bundle);
        fragment.setArguments(bundle);
        getFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("fragment3").commit();
    }

    private String getDate() {
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    @Override
    public void onDateClick(String date) {
        lastsevenday.dismiss();
        String link = domain + serviceFunc + date + "?callback=" + callback;
        this.date = date;
        showFragmentHukumnama(link);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (MainActivity.isMyServiceRunning(Mp3PlayerService.class, context))
            context.stopService(intent);

    }

}
