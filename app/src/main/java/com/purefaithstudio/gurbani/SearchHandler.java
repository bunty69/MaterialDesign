package com.purefaithstudio.gurbani;

import android.util.Log;

import com.shephertz.app42.paas.sdk.android.upload.Upload;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
/**
 * Created by harsimran singh on 03-12-2015.
 */
public class SearchHandler {
    StringBuffer keyList;
    HashMap<String, Upload.File> fileHashMap;

    public SearchHandler() {
        keyList = MainActivity.apm.loadMap();
        this.fileHashMap = MainActivity.apm.getMap();
        Log.i("Harsim", "" + keyList);
    }

    public ArrayList<Upload.File> search(String search) {
        ArrayList<Upload.File> fileArrayList = new ArrayList<>();
        String pat = "[^,]*" + search + "[^,]*";
        Pattern p = Pattern.compile(pat, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(keyList);
        while (m.find()) {
            Log.i("Harsim", "" + m.group());
            fileArrayList.add(fileHashMap.get(m.group()));

        }
        return fileArrayList;
    }
}
