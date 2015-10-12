package com.purefaithstudio.gurbani;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

/**
 * Created by MY System on 4/1/2015.
 */
public class Fragment2 extends Fragment implements View.OnClickListener {

    View rootView;

    //"http://192.69.212.61:8020/stream"-harimandir sahib
    String[] string = new String[]{"Select", "Sri Harimandir Sahib", "Simran","AKHAND PATH-SRI GURU GRANTH SAHIB JI",
    "TAKHAT HAZUR SAHIB","THE CLASSICS","GURBANI KATHA","STORIES","DASMESH DARBAR","GURDWARA DUKH NIWARAN SAHIB","GURDWARA SAN JOSE"};
        String[] channel_link = new String[]{"http://192.69.212.61:8020/stream",
            "http://192.69.212.61:8016/stream","http://192.69.212.61:8018/stream","http://192.69.212.61:8038/stream",
        "http://192.69.212.61:8501/stream","http://192.69.212.61:8013/stream","http://192.69.212.61:8017/stream",
        "http://192.69.212.61:8036/stream","http://192.69.212.61:8037/stream","http://192.69.212.61:8031/stream"};
    private Bundle b1;
    private Intent i1;

    public Fragment2() {
    }

    Spinner spinner;
    int check = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment2, container, false);
        spinner = (Spinner) rootView.findViewById(R.id.spinner1);
        i1 = new Intent(getActivity(), Audio_one.class);
        b1 = new Bundle();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.custom_spinner, string);
        // arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                check++;

                if (check > 1) {

                    switch (position) {
                        case 1:
                            b1.putString("key", channel_link[position - 1]);
                            i1.putExtras(b1);
                            startActivity(i1);
                            break;
                        case 2:

                            b1.putString("key", channel_link[position - 1]);
                            i1.putExtras(b1);
                            startActivity(i1);
                            break;
                        case 3:

                            b1.putString("key", channel_link[position - 1]);
                            i1.putExtras(b1);
                            startActivity(i1);
                            break;
                        case 4:

                            b1.putString("key", channel_link[position - 1]);
                            i1.putExtras(b1);
                            startActivity(i1);
                            break;
                        case 5:

                            b1.putString("key", channel_link[position - 1]);
                            i1.putExtras(b1);
                            startActivity(i1);
                            break;
                        case 6:

                            b1.putString("key", channel_link[position - 1]);
                            i1.putExtras(b1);
                            startActivity(i1);
                            break;
                        case 7:

                            b1.putString("key", channel_link[position - 1]);
                            i1.putExtras(b1);
                            startActivity(i1);
                            break;
                        case 8:

                            b1.putString("key", channel_link[position - 1]);
                            i1.putExtras(b1);
                            startActivity(i1);
                            break;
                        case 9:

                            b1.putString("key", channel_link[position - 1]);
                            i1.putExtras(b1);
                            startActivity(i1);
                            break;
                        case 10:

                            b1.putString("key", channel_link[position - 1]);
                            i1.putExtras(b1);
                            startActivity(i1);
                            break;

                    }
                }
                spinner.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return rootView;
    }

    @Override
    public void onClick(View v) {
        spinner.performClick();
    }
}
