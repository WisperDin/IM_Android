package com.cst.im.presenter;

/**
 * Created by ASUS on 2017/4/23.
 */

public interface ILoginPresenter {
    void saveLoginInf(); //保存用户信息，下次直接登录

    void doLogin(String name, String passwd); // 进行用户名操作
    boolean doOtherLogin(); // 其他登录
    boolean doRegister(); // 注册
    void doForgetPassword(); // 忘记密码
    short judgeUsername(String username); // 判断用户名类型
    boolean judgePassword(String password); // 判断密码可用
    boolean canLogin(String username, String password); //判断用户名和密码都符合规范
}