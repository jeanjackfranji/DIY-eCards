package com.ISD.slidingmenu.model;

/**
 * Created by Jean-Jack on 5/25/2014.
 */
public class NavDrawerItem {

    private String title;
    private int icon;

    public NavDrawerItem(){}

    public NavDrawerItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }

    public String getTitle(){
        return this.title;
    }

    public int getIcon(){
        return this.icon;
    }
    public void setTitle(String title){
        this.title = title;
    }

    public void setIcon(int icon){
        this.icon = icon;
    }
}