package com.cst.im.model;

/**
 * Created by Acring on 2017/4/24.
 */

public interface ILoginUser {
    short checkTypeOfUsername(String username);
    boolean checkPasswordValidity(String password);
    int checkUserValidity(String name, String passwd);


}
