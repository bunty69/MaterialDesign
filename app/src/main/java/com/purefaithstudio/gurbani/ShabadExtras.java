package com.purefaithstudio.gurbani;

/**
 * Created by harsimran singh on 02-12-2015.
 */
public class ShabadExtras {
    String name, ragi, fileUrl, ImageUrl;
    float size;
    //HashMap<String,String> tags;
    //for creation

    public ShabadExtras() {

    }

    public ShabadExtras(String name, String ragi, String url, float size) {
        //tags= new HashMap<>();
        this.name = name;
        this.ragi = ragi;
        this.fileUrl = url;
        this.size = size;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
//for loading from online


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRagi() {
        return ragi;
    }

    public void setRagi(String ragi) {
        this.ragi = ragi;
    }

    public String getUrl() {
        return fileUrl;
    }

    public void setUrl(String url) {
        this.fileUrl = url;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

   /* public HashMap<String, String> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, String> tags) {
        this.tags = tags;
    }

    public String getTagID(String Key)
    {
        String tagID="NULL";
        try{
            tagID=tags.get(Key);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return tagID;
    }

    public void addTagID(String key,String ID)
    {
        tags.put(key,ID);
    }*/
}
