package com.cst.im.NetWork.proto;

import com.cst.im.model.IMsg;
import com.cst.im.model.IUser;

import protocol.Protocol.*;

public class BuildFrame {

    private Frame.Builder frame;
    public static final int Login  = 0;
    public static final int Register  = 1;
    public static final int FeedBack  = 2;
    public static final int ChatMsg  = 3;
    public  BuildFrame(int msgType) {
        frame = Frame.newBuilder();

        frame.setProtoSign(1234);
        frame.setMsgLength(1);
        frame.setMsgType(msgType);
        frame.setSenderTime(10000);

    }
    public Frame GetLoginFrame(IUser userToLogin){
        User.Builder src = User.newBuilder();
        src.setUserName(userToLogin.getName());
        src.setUserPwd(userToLogin.getPasswd());
        frame.setSrc(src.build());
        return frame.build();
    }
    public Frame GetChatMsgFrame(IMsg chatMsg){
        //发送源
        User.Builder src = User.newBuilder();
        src.setUserName(chatMsg.getLeft_name());
        //接收者
        User.Builder dst = User.newBuilder();
        dst.setUserName(chatMsg.getRight_name());

        DstUser.Builder dstGroup = DstUser.newBuilder();
        dstGroup.addDst(dst);
        //要发送的信息
        Msg.Builder msg = Msg.newBuilder();
        msg.setMsg(chatMsg.getMessage());
        frame.setSrc(src.build());
        frame.setDst(dstGroup.build());
        frame.setMsg(msg);
        return frame.build();
    }
}