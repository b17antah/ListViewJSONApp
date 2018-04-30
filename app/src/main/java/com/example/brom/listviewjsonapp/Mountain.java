package com.example.brom.listviewjsonapp;

/**
 * Created by b17antah on 2018-04-19.
 */

public class Mountain {
    private String name;
    private String location;
    private int height;

    public Mountain(String inName,String inLocation, int inHeight) {
        name=inName;
        location=inLocation;
        height=inHeight;
    }

    public String toString() { return name; }

    public String mountainInfo() {
        String str=name;
        str+=" is located in ";
        str+=location;
        str+=" and has a height of ";
        str+=Integer.toString(height);
        str+="m.";
        return str;
    }

    public void setHeight(int newHeight) {
        height=newHeight;
    }
}
