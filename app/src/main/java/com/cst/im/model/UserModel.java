package com.cst.im.model;

/**
 * Created by ASUS on 2017/4/23.
 */

public class UserModel implements IUser{
    String name;
    String passwd;
    int id;
    //本地用户
    public static UserModel localUser;
    public static void InitLocalUser(String UserName,String UserPwd,int UserID){
        localUser=new UserModel(UserName,UserPwd,UserID);
        //TODO 用户其他信息的初始
    }



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
