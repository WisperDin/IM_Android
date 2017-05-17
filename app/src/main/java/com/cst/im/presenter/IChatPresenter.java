package com.cst.im.presenter;

import com.cst.im.model.IBaseMsg;
import com.cst.im.model.IUser;

import java.io.File;

public interface IChatPresenter {
    //加载历史消息
    //List<IMsg>  LoadHisMsg();
    //发送新的消息
    void  SendMsg(IUser[] dstUser, String contString);

    //发送一般文件
    void SendFile(IUser[] dstUser, File file, IBaseMsg.MsgType msgType);

    void SendLocation(double latitude, double longitude,IUser[] dstUser,
                      String imagePath, String locationAddress);

}
