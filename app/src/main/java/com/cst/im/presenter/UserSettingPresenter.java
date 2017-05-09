package com.cst.im.presenter;

import com.cst.im.UI.main.me.SettingDetails;

import java.util.List;

/**
 * Created by Acring on 2017/5/7.
 */

public class UserSettingPresenter implements IUserSettingPresenter {
    @Override
    public List<SettingDetails> loadLocalUserInfo(int id) {
        return null;
    }

    @Override
    public List<SettingDetails> pullRemoteUserInfo(int id) {
        return null;
    }

    @Override
    public void pushRemoteUserInfo(int id, List<SettingDetails> settingDetailsList) {

    }

    @Override
    public void pushLocalUserInfo(int id, List<SettingDetails> settingDetailsList) {

    }
}
