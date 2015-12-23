package com.purefaithstudio.gurbani;

/**
 * Created by MY System on 12/11/2015.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;

public class NetworkConnectionDetector {
    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        }
        NetworkInfo[] allNetworkInfo = connectivityManager.getAllNetworkInfo();
        if (allNetworkInfo == null) {
            return false;
        }
        for (NetworkInfo state : allNetworkInfo) {
            if (state.getState() == State.CONNECTED) {
                return true;
            }
        }
        return false;
    }
}
