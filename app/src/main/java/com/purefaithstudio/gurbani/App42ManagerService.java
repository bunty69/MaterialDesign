package com.purefaithstudio.gurbani;

import android.content.Context;
import android.util.Log;
import com.shephertz.app42.paas.sdk.android.App42API;
import com.shephertz.app42.paas.sdk.android.App42CallBack;
import com.shephertz.app42.paas.sdk.android.App42Exception;
import com.shephertz.app42.paas.sdk.android.App42Response;
import com.shephertz.app42.paas.sdk.android.storage.Query;
import com.shephertz.app42.paas.sdk.android.storage.QueryBuilder;
import com.shephertz.app42.paas.sdk.android.storage.QueryBuilder.Operator;
import com.shephertz.app42.paas.sdk.android.storage.Storage;
import com.shephertz.app42.paas.sdk.android.storage.StorageService;
import com.shephertz.app42.paas.sdk.android.upload.Upload;
import com.shephertz.app42.paas.sdk.android.upload.UploadService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 import com.shephertz.app42.paas.sdk.android.App42Response;
 * Created by MY System on 11/29/2015.
 */

public class App42ManagerService {
    final String APIKEY = "01a46d45949fc5d814681ed8423b12ca6f6bf43deeb1cae1ecdee7dc105f87e1";
    final String SECRET_KEY = "36ba071d7447b7ab85267d9b7224d6bb1f8e49a88b2cf3b86673919e43f22b48";
    final private String DBName="TAGS";
    final private String Collection="FileExtra";
    private UploadService uploadService;
    private Upload files;
    private StorageService storageService;
    private ArrayList<Upload.File> fileList,shabadList;
    public static boolean flag=false;
    //private ArrayList<ShabadExtras> shabadExtraList;
    private HashMap<String,Upload.File> filemap;
    boolean load=false;

    public App42ManagerService(Context context) {
        App42API.initialize(context, APIKEY, SECRET_KEY);
        uploadService = App42API.buildUploadService();
        //storageService=App42API.buildStorageService();
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
                Log.i("Playercheck", "no of files:" + fileList.size() + ":" + file.getName() + "   " + file.getUrl());
                flag = true;
                load = true;
                shabadList = new ArrayList<Upload.File>();
                for (int i = 8; i < fileList.size(); i++) {
                    shabadList.add(fileList.get(i));
                    fileList.remove(i);
                    i--;
                }
                Log.i("Playercheck", "no of files:" + shabadList.size() + ":");
            }

            @Override
            public void onException(Exception e) {
                Log.i("Tag", e.toString());
            }
        });
       /* uploadService.getAllFilesCount(new App42CallBack() {
            public void onSuccess(Object response) {
                App42Response app42response = (App42Response) response;
                System.out.println("Total Records :  " + app42response.getTotalRecords());
            }

            public void onException(Exception ex) {
                System.out.println("Exception Message" + ex.getMessage());
            }
        });*/


    }
    public StringBuffer loadMap()
    {
        StringBuffer keyList=new StringBuffer();
        filemap=new HashMap<>();
        if(!(fileList==null))
        for(Upload.File f:fileList) {
            keyList.append(f.getName()+",");
            filemap.put(f.getName(), f);
        }
        return keyList;
    }

    public HashMap<String,Upload.File> getMap(){return filemap;}

    public ArrayList<Upload.File> getFileArrayList() {return fileList;  }

    public ArrayList<Upload.File> getShabadFileArrayList() { return shabadList;  }
//was for json search depricated
  /*  public void addExtra(ShabadExtras SE)
    {

        json=new JSONObject();
        try {
            json.put("name",SE.getName());
            json.put("extra",parser.toJson(SE));
            Storage storage = storageService.saveOrUpdateDocumentByKeyValue(DBName, Collection,"name",SE.getName(), json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void getExtra(String searchString)
    {
        Query q1 = QueryBuilder.build("name", searchString, Operator.LIKE);
            storageService.findDocumentsByQuery(DBName, Collection, q1, new App42CallBack() {
                public void onSuccess(Object response) {
                    Storage storage = (Storage) response;
                    try {
                        ArrayList<Storage.JSONDocument> jsonDocList = storage.getJsonDocList();
                        for (Storage.JSONDocument jsonDoc : jsonDocList)
                        {
                            System.out.println("objectId is " + jsonDoc.getDocId());
                            json = new JSONObject(jsonDoc.getJsonDoc());
                            System.out.println(json.get("extras"));
                            ShabadExtras shabadExtras = parser.fromJson(ShabadExtras.class, (String) json.get("extras"));
                            shabadExtraList.add(shabadExtras);
                        }
                        Fragment4.upDownAdapter.updateList(shabadExtraList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void onException(Exception ex) {
                    System.out.println("Exception Message" + ex.getMessage());
                }
            });
    }*/
}
