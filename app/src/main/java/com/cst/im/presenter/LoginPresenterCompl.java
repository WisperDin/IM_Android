package com.cst.im.presenter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cst.im.NetWork.ComService;
import com.cst.im.NetWork.proto.DeEnCode;
import com.cst.im.dataBase.DBManager;
import com.cst.im.model.ILoginUser;
import com.cst.im.model.LoginUserModel;
import com.cst.im.model.UserModel;
import com.cst.im.view.ILoginView;

import java.io.IOException;

/**
 */

public class LoginPresenterCompl implements ILoginPresenter, ComService.MsgHandler {
    ILoginUser loginUser ;
    ILoginView iLoginView;

    Handler handler;

    public LoginPresenterCompl(ILoginView iLoginView) {
        this.iLoginView = iLoginView;

        handler = new Handler(Looper.getMainLooper());
        ComService.setLoginCallback(this);
    }

    /*
    保存登录信息到本地数据库
    用户名则保存登录名和登录密码
     */
    @Override
    public void saveLoginInf() {
        DBManager.saveLoginUser(loginUser);
        DBManager.initLocalUserInfo(new UserModel(loginUser.getUsername(),loginUser.getPassword(),loginUser.getId()));
    }



    //参数为反馈的状态码与状态信息
    @Override
    public void handleFbEvent(final int rslCode,final int id) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                loginUser.setId(id);
                iLoginView.onLoginResult(rslCode,id);
            }
        });
    }

    @Override
    public void doLogin(String name, String passwd) {
        loginUser = new LoginUserModel(name, passwd);
        //编码登录帧
        final byte[] loginFrame = DeEnCode.encodeLoginFrame(loginUser);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ComService.client.SendData(loginFrame);
                } catch (IOException | NullPointerException ioe) {
                    Log.w("send", "send data failed");
                }
            }
        }).start();

        //测试中，现在一启动程序就自动发一条登录帧到服务器
/*        Boolean isLoginSuccess = true;
        final int code = user.checkUserValidity(name,passwd);

        if (code!=0) isLoginSuccess = false;
        final Boolean result = isLoginSuccess;
        handler.post(new Runnable() {
            @Override
            public void run() {
            iLoginView.onLoginResult(result, code);
        }});*/
        //return 1;
    }

//    private void initUser() {
//        loginUser = new LoginUserModel("mvp", "mvp");
//    }

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
        return LoginUserModel.checkTypeOfUsername(username);
    }

    @Override
    public boolean judgePassword(String password) {
        return LoginUserModel.checkPasswordValidity(password);
    }

    @Override
    public boolean canLogin(String username, String password) {
        if(judgeUsername(username) != Status.Login.USERNAME_INVALID && judgePassword(password))
            return true;
        return false;
    }


}