package com.cst.im.UI.main.me;

/**
 * Created by HuangYiXin on 2017/4/22.
 */

public class Settings {

    private String name;
    private int ImageId;

    private int index;

    public Settings(String name,int imageId,int index){
        this.name = name;
        this.ImageId = imageId;
        this.index = index;
    }

    public String getName(){
        return name;
    }

    public int getImageId(){
        return ImageId;
    }

    public int getIndex(){
        return index;
    }
}
