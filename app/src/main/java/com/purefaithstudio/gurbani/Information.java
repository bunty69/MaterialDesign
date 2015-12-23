package com.purefaithstudio.gurbani;

/**
 * Created by MY System on 3/18/2015.
 */
public class Information {
    String title;
    int icon;


    public Information(String title, int icon) {
        this.title = title;
        this.icon = icon;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImageUrl() {
        return icon;
    }

    public void setImageUrl(int icon) {
        this.icon = icon;
    }
}
