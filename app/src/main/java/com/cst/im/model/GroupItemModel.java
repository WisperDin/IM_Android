package com.cst.im.model;

/**
 * Created by jijinping on 2017/5/21.
 */

public class GroupItemModel {
    private int Icon;
    private String name;
    public GroupItemModel(String name,int Icon)
    {
        this.Icon=Icon;
        this.name=name;
    }
    public GroupItemModel(String name)
    {
        this.name=name;
    }
    public int getIcon() {
        return Icon;
    }

    public void setIcon(int icon) {
        Icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
