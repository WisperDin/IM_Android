package com.cst.im.model;

/**
 * Created by ASUS on 2017/4/23.
 */

public class UserModel implements IUser{
    String name;
    String passwd;
    int id;

    public UserModel(String name, String passwd,int id) {
        this.name = name;
        this.passwd = passwd;
        this.id=id;
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public String getPasswd() {
        return passwd;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public int checkUserValidity(String name, String passwd){
        if (name==null||passwd==null||!name.equals(getName())||!passwd.equals(getPasswd())){
            return -1;
        }
        return 0;
    }
}
