package com.example.cinna.myapplication;

/**
 * Created by cinna on 2017/2/3.
 */

public class item {
    private int pid;
    private String title;
    private String href;
    private String date;

    public int getId(){
        return pid;
    }
    public String getTitle(){
        return title;
    }
    public String getHref() {
        return href;
    }
    public String getDate(){
        return date;
    }
    public void setId(int pid){
        this.pid = pid;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setHref(String href){
        this.href = href;
    }
    public void setDate(String date){
        this.date = date;
    }
}
