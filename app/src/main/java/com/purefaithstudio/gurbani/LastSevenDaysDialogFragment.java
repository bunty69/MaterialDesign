package com.purefaithstudio.gurbani;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by MY System on 12/13/2015.
 */
public class LastSevenDaysDialogFragment extends DialogFragment {
    private View rootView;
    private String[] dateArray;
    private String date;
    private NoticeDialogListener notifyDate;

    public void setClicklistener(NoticeDialogListener notifyDate) {
        this.notifyDate = notifyDate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dateArray = getDates();
    }

    private String[] getDates() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        String dates[] = new String[7];
        for (int i = 0; i < 7; ++i) {
            c.add(Calendar.DAY_OF_YEAR, -1);
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy");
            String formattedDate = df.format(c.getTime());
            dates[i] = formattedDate;
        }
        return dates;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setItems(dateArray, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                date = dateArray[which];
                System.out.println("date"+date);
                if (notifyDate != null)
                    notifyDate.onDateClick(date);
            }
        });
        return builder.create();

    }

    public interface NoticeDialogListener {
        public void onDateClick(String date);
    }
}
