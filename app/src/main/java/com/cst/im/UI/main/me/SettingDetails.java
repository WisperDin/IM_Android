package com.cst.im.UI.main.me;

/**
 * Created by HuangYiXin on 2017/5/2.
 */

public class SettingDetails {
    private String name;
    private String value;

    private int index;

    public SettingDetails(String name,String value,int index){
        this.name = name;
        this.value = value;
        this.index = index;
    }

    public String getName(){return name;}

    public String getValue(){return value;}

    public int getIndex(){return index;}

    public void setValue(String value){ this.value = value;};
}
