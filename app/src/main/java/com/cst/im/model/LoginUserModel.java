package com.cst.im.model;

import com.cst.im.presenter.Status;

import java.util.regex.Pattern;

/**
 * Created by Acring on 2017/4/24.
 */

public class LoginUserModel implements ILoginUser {


    String username;
    String password;
    public LoginUserModel(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public short checkTypeOfUsername(String username){

        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        Pattern phonePattern = Pattern.compile("^1(3|4|5|7|8)[0-9]\\d{8}$");
        Pattern usernamePattern = Pattern.compile(".*[a-zA-Z]+.*");
        if(username.length() < 4 || username.length() > 22){ //用户名不合格
            return Status.Login.USERNAME_INVALID;
        }
        else if(phonePattern.matcher(username).find()){ //手机号
            return Status.Login.USERNAME_PHONE;
        }
        else if(emailPattern.matcher(username).find()){ // 邮箱
            return Status.Login.USERNAME_EMAIL;
        }
        else if (usernamePattern.matcher(username).find()){ //用户名
            return Status.Login.USERNAME_ACCOUNT;
        }
        else{
            return Status.Login.USERNAME_INVALID;
        }
    }

    @Override
    public boolean checkPasswordValidity(String password) {
        if(4 < password.length()&&password.length() < 22){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public int checkUserValidity(String name, String passwd){
        if (name==null||passwd==null||!name.equals(getUsername())||!passwd.equals(getPassword())){
            return -1;
        }
        return 0;
    }
}
