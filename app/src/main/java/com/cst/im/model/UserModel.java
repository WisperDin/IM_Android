package com.cst.im.model;

import com.cst.im.dataBase.DBManager;

/**
 * Created by ASUS on 2017/4/23.
 */

public class UserModel implements IUser{
    private String name;
    private String passwd;
    private int id;

    private String userPicture = "";
    private String userSex = "";
    private String userRealName = "";



    private String userPhone = "";
    private String userEmail = "";
    private String userAddress = "";
    private String userSign = "";
    private int age = 0;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }



    //本地用户
    public static UserModel localUser;
    public static void InitLocalUser(int UserID){//从本地加载用户信息
        localUser = DBManager.queryLocalUserInfo(UserID);
    }
    public static void saveLocalUser(){//把信息保存到本地
        DBManager.pushLocalUserInfo(localUser);
    }




    public UserModel(String name, String passwd,int id) {
        this.name = name;
        this.passwd = passwd;
        this.id=id;
    }
    public UserModel(){

    }
    public  UserModel(int userId){
        this.id = userId;
    }

    @Override
    public int checkUserValidity(String name, String passwd){
        if (name==null||passwd==null||!name.equals(getName())||!passwd.equals(getPasswd())){
            return -1;
        }
        return 0;
    }

    @Override
    public String getName() {
        return name;
    }


    @Override
    public String getPasswd() {
        return passwd;
    }

    public void setName(String name) {
        this.name = name==null?"":name;
    }

    public void setPasswd(String passwd) {

        this.passwd = passwd==null?"":passwd;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserPicture() {
        return userPicture;
    }

    public void setUserPicture(String userPicture) {
        this.userPicture = userPicture==null?"":userPicture;
    }

    public String getUserSex() {
        return userSex;
    }

    public void setUserSex(String userSex) {
        this.userSex = userSex==null?"":userSex;
    }

    public String getUserRealName() {
        return userRealName;
    }

    public void setUserRealName(String userRealName) {
        this.userRealName = userRealName==null?"":userRealName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone==null?"":userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail==null?"":userEmail;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress==null?"":userAddress;
    }

    public String getUserSign() {
        return userSign;
    }

    public void setUserSign(String userSign) {
        this.userSign = userSign==null?"":userSign;
    }
}
