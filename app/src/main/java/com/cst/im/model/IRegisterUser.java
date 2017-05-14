package com.cst.im.model;

/**
 * Created by PolluxLee on 2017/4/25.
 */

public interface IRegisterUser {

    short checkTypeOfUsername(String username);
    boolean checkPasswordValidity(String password);
    int checkUserValidity(String name, String passwd);

}
