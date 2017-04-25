package com.cst.im.presenter;

import android.os.Handler;
import android.os.Looper;

import com.cst.im.model.ILoginUser;
import com.cst.im.model.IUser;
import com.cst.im.model.LoginUserModel;
import com.cst.im.model.UserModel;
import com.cst.im.view.ILoginView;
/**
 */

public class LoginPresenterCompl implements ILoginPresenter {
    ILoginView iLoginView;
    ILoginUser user;
    Handler    handler;

    public LoginPresenterCompl(ILoginView iLoginView) {
        this.iLoginView = iLoginView;
        initUser();
        handler = new Handler(Looper.getMainLooper());
    }
    @Override
    public void saveLoginInf() {

    }
    @Override
    public int doLogin(String name, String passwd) {
        Boolean isLoginSuccess = true;
        final int code = user.checkUserValidity(name,passwd);

        if (code!=0) isLoginSuccess = false;
        final Boolean result = isLoginSuccess;
        handler.post(new Runnable() {
            @Override
            public void run() {
            iLoginView.onLoginResult(result, code);
        }});
        return code;
    }
    private void initUser(){
        user = new LoginUserModel("mvp","mvp");
    }

    @Override
    public boolean doOtherLogin() {
        return false;
    }

    @Override
    public boolean doRegister() {
        return false;
    }

    @Override
    public void doForgetPassword() {

    }

    @Override
    public short judgeUsername(String username) {
        return user.checkTypeOfUsername(username);
    }

    @Override
    public boolean judgePassword(String password) {
        return user.checkPasswordValidity(password);
    }
}