package com.cst.im.presenter;

import com.cst.im.UI.main.me.SettingDetails;
import com.cst.im.model.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acring on 2017/5/7.
 */

public interface IUserSettingPresenter {


    void pullRemoteUserInfo();//加载远程数据

    void pushRemoteUserInfo (UserModel userModel); //更新远程数据



}
