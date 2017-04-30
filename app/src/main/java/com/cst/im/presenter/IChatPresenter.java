package com.cst.im.presenter;

import com.cst.im.model.IMsg;

import java.util.List;

public interface IChatPresenter {
    //加载历史消息
    //List<IMsg>  LoadHisMsg();
    //发送新的消息
    void  SendMsg(String contString);
}
