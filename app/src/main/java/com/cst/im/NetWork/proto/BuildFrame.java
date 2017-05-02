package com.cst.im.NetWork.proto;

import com.cst.im.model.IUser;

import protocol.Protocol.*;

public class BuildFrame {

    private Frame.Builder frame;
    public static final int Login  = 0;
    public static final int Register  = 1;
    public static final int FeedBack  = 2;

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

    public Frame GetRegisterFrame(IUser userToRegister){
        User.Builder src = User.newBuilder();
        src.setUserName(userToRegister.getName());
        src.setUserPwd(userToRegister.getPasswd());
        frame.setSrc(src.build());
        return frame.build();
    }

}