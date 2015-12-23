package com.purefaithstudio.gurbani;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;


/**
 * Created by MY System on 12/10/2015.
 */
public class Wait extends DialogFragment {


    private ProgressDialog dialog;

    public Wait() {
    }

    @Override
    public void onStart() {
        super.onStart();
        /*Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }*/
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dialog = new ProgressDialog(getActivity());
        this.setStyle(STYLE_NO_TITLE, R.style.MyProgressTheme);
        dialog.setCancelable(false);
        return dialog;
    }
}
