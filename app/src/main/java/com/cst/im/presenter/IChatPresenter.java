package com.cst.im.presenter;

import com.cst.im.model.IMsg;

import java.util.List;

/**
 * Created by ASUS on 2017/4/23.
 */

public interface IChatPresenter {
    //加载历史消息
    List<IMsg>  LoadHisMsg();
    //发送新的消息
    void  SendMsg(String contString);
}
