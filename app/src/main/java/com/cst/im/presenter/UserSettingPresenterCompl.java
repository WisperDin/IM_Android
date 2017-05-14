package com.cst.im.presenter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cst.im.NetWork.ComService;
import com.cst.im.NetWork.proto.BuildFrame;
import com.cst.im.NetWork.proto.DeEnCode;
import com.cst.im.UI.main.me.ChangeDetailsActivity;
import com.cst.im.UI.main.me.SettingDetails;
import com.cst.im.UI.main.me.UserInfoActivity;
import com.cst.im.dataBase.Constant;
import com.cst.im.dataBase.DBManager;
import com.cst.im.model.UserModel;

import java.io.IOException;
import java.util.List;

/**
 * Created by Acring on 2017/5/7.
 */

public class UserSettingPresenterCompl implements IUserSettingPresenter ,ComService.UserInfoHandler{

    private ChangeDetailsActivity cda;
    private UserInfoActivity uia;
    private Handler handler;
    public UserSettingPresenterCompl(ChangeDetailsActivity cda){
        this.cda = cda;
        handler = new Handler(Looper.getMainLooper());
        Log.d("User","initPresenter");
        ComService.setUserInfoHandler(this);
    }
    public UserSettingPresenterCompl(UserInfoActivity uia){
        this.uia = uia;
        handler = new Handler(Looper.getMainLooper());
        ComService.setUserInfoHandler(this);
    }
    @Override
    public void pullRemoteUserInfo() {
        final byte[] userInfoFrame = DeEnCode.encodePullUserInfo(UserModel.localUser);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ComService.client.SendData(userInfoFrame);
                } catch (IOException | NullPointerException ioe) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(cda == null){
                                uia.onNetWorkError();
                            }else{
                                cda.onNetWorkError();
                            }
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void pushRemoteUserInfo(UserModel userModel) {
        final byte[] userInfoFrame = DeEnCode.encodePushUserInfo(userModel);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ComService.client.SendData(userInfoFrame);
                } catch (IOException | NullPointerException ioe) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(cda == null){
                                uia.onNetWorkError();
                            }else{
                                cda.onNetWorkError();
                            }
                        }
                    });
                }
            }
        }).start();
    }


    @Override
    public void handlePullUserInfoEvent(int rslCode, UserModel user) {
        Log.d("UserInfo","pull from origin");
        if(rslCode == BuildFrame.PullUserInfoFail){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    uia.onPullFail();
                }
            });
            return;
        }
        UserModel.localUser.setAge(user.getAge());
        UserModel.localUser.setUserRealName(user.getUserRealName());
        UserModel.localUser.setUserSign(user.getUserSign());
        UserModel.localUser.setUserSex(user.getUserSex());
        UserModel.localUser.setUserAddress(user.getUserAddress());
        UserModel.localUser.setUserEmail(user.getUserEmail());
        UserModel.localUser.setUserPhone(user.getUserPhone());
        UserModel.saveLocalUser();
        handler.post(new Runnable() {
            @Override
            public void run() {
                uia.onPullSuccess();
            }
        });
    }

    @Override
    public void handlePushUserInfoEvent(int rslCode) {

        if(rslCode == BuildFrame.PushUserInfoSuccess){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(uia == null)
                        cda.onChangeSuccess();
                    else
                        uia.onChangeSuccess();
                }
            });
        }
        else {

            handler.post(new Runnable() {
                @Override
                public void run() {
                    if(uia == null)
                        cda.onChangeFail();
                    else
                        uia.onChangeFail();
                }
            });
        }
    }
}
