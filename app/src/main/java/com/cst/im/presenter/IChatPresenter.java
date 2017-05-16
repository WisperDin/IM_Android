package com.cst.im.presenter;

import com.cst.im.model.IBaseMsg;

import java.io.File;

public interface IChatPresenter {
    //加载历史消息
    //List<IMsg>  LoadHisMsg();
    //发送新的消息
    void  SendMsg( String contString);

    //发送一般文件
    void SendFile(File file, IBaseMsg.MsgType msgType);

}
