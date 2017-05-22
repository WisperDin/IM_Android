package com.cst.im.model;

/**
 * Created by jijinping on 2017/5/18.
 */

public class FriendgroupModel {
    String name;   //组名
    int number;    //组人数

    public FriendgroupModel(String name, int number)
    {
        this.name=name;
        this.number=number;
    }
    public FriendgroupModel(String name)
    {
        this.name=name;
        this.number=0;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String GetTextContent()
    {
        return name+'('+number+')';
    }

}
