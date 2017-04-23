package com.cst.im.model;

/**
 * Created by ASUS on 2017/4/23.
 */

public interface IUser {
    String getName();

    String getPasswd();

    int checkUserValidity(String name, String passwd);
}
