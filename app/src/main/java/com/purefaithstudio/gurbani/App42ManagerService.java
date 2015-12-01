package com.purefaithstudio.gurbani;

import android.content.Context;
import android.util.Log;

import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.upload.Upload;
import com.shephertz.app42.paas.sdk.android.upload.UploadService;

import java.util.ArrayList;

/**
 * Created by MY System on 11/29/2015.
 */

public class App42ManagerService {
    final String APIKEY = "01a46d45949fc5d814681ed8423b12ca6f6bf43deeb1cae1ecdee7dc105f87e1";
    final String SECRET_KEY = "36ba071d7447b7ab85267d9b7224d6bb1f8e49a88b2cf3b86673919e43f22b48";

    private UploadService uploadService;
    private Upload files;
    private ArrayList<Upload.File> fileList;
    public static boolean flag=false;

    public App42ManagerService(Context context) {
        App42API.initialize(context, APIKEY, SECRET_KEY);
        uploadService = App42API.buildUploadService();
        getFiles();
        // storage=App42API.buildStorageService();
    }

    public void getFiles() {
        String name = "";
        uploadService.getAllFiles(new App42CallBack() {
            @Override
            public void onSuccess(Object response) {
                Upload upload = (Upload) response;
                fileList = upload.getFileList();
                Upload.File file = fileList.get(7);
                Log.i("Playercheck", file.getName() + "   " + file.getUrl());
                flag=true;
            }

            @Override
            public void onException(Exception e) {
                Log.i("Tag", e.toString());
            }
        });
        /*uploadService.getFileByName(name, new App42CallBack() {
            @Override
            public void onSuccess(Object o) {

            }

            @Override
            public void onException(Exception e) {

            }
        });*/


    }

    public ArrayList<Upload.File> getFileArrayList() {
        return fileList;
    }

}
