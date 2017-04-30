package com.cst.im.presenter;

/**
 * Created by ASUS on 2017/4/23.
 */

public interface ILoginPresenter {
    void saveLoginInf(); //保存用户信息，下次直接登录

    void doLogin(String name, String passwd);
    boolean doOtherLogin();
    boolean doRegister();
    void doForgetPassword();
    short judgeUsername(String username);
    boolean judgePassword(String password);
}