package com.cst.im.presenter;

import com.cst.im.UI.main.me.SettingDetails;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Acring on 2017/5/7.
 */

public interface IUserSettingPresenter {

    List<SettingDetails> loadLocalUserInfo(int id);//加载本地数据

    List<SettingDetails> pullRemoteUserInfo(int id);//加载远程数据

    void pushRemoteUserInfo (int id,List<SettingDetails> settingDetailsList); //更新远程数据

    void pushLocalUserInfo (int id,List<SettingDetails> settingDetailsList); // 更新本地数据


}
