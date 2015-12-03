package com.purefaithstudio.gurbani;

import java.util.ArrayList;

/**
 * Created by harsimran singh on 02-12-2015.
 */
public class Tag {
    String name;
    ArrayList<String> itemidList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getItemidList() {
        return itemidList;
    }

    public void addItemID(String itemID)
    {
        itemidList.add(itemID);
    }

}
