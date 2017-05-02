package com.cst.im.presenter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cst.im.model.IRegisterUser;
import com.cst.im.model.RegisterUserModel;
import com.cst.im.view.IRegisterView;


/**
 * Created by PolluxLee on 2017/4/25.
 */

public class RegisterPresenterCompl implements IRegisterPresenter{

    private IRegisterView iRegisterView;
    private IRegisterUser registerUser;
    private Handler handler;

    public RegisterPresenterCompl(IRegisterView iRegisterView){
        this.iRegisterView = iRegisterView;
        handler = new Handler(Looper.getMainLooper());
        registerUser = new RegisterUserModel();
    }

    @Override
    public int doRegister(String name, String passwd) {

        int returnCode = Status.Register.REGISTER_FAIL;

        final int registerStatus;

        switch (registerUser.checkTypeOfUsername(name)){
            case Status.Register.USERNAME_ACCOUNT:
                if(registerUser.checkPasswordValidity(passwd)){
                    // 密码有效
                    /**
                     * 注册逻辑
                     */
                    returnCode = Status.Register.REGISTER_SUCCESS;
                    registerStatus = Status.Register.REGISTER_SUCCESS;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d("handler","1111111111");
                            iRegisterView.onRegisterResult(registerStatus);
                        }
                    });
                }
                break;
            case Status.Register.USERNAME_PHONE:
                break;
            case Status.Register.USERNAME_EMAIL:
                break;
            case Status.Register.USERNAME_INVALID:
                break;
        }

        return returnCode;
    }

    @Override
    public int judgeUsername(String username) {
        return registerUser.checkTypeOfUsername(username);
    }

    @Override
    public boolean judgePassword(String password) {
        return registerUser.checkPasswordValidity(password);
    }
}
