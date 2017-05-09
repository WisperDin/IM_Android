package com.cst.im.model;

import com.cst.im.presenter.Status;

import java.util.regex.Pattern;

/**
 * Created by PolluxLee on 2017/4/25.
 */

public class RegisterUserModel implements IRegisterUser {

    @Override
    public short checkTypeOfUsername(String username) {

        Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
        Pattern phonePattern = Pattern.compile("^1(3|4|5|7|8)[0-9]\\d{8}$");
        Pattern usernamePattern = Pattern.compile(".*[a-zA-Z]+.*");

        if(username.length() < 3 || username.length() > 16){ //用户名不合格
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
        if(2 < password.length()&&password.length() < 16){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public int checkUserValidity(String name, String passwd) {
        return 0;
    }
}
