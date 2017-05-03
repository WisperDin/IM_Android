package com.cst.im.NetWork.proto;

import android.util.Log;

import com.cst.im.model.IMsg;
import com.cst.im.model.IUser;

import protocol.Protocol.DstUser;
import protocol.Protocol.Frame;
import protocol.Protocol.Msg;
import protocol.Protocol.User;

public class BuildFrame {

    private Frame.Builder frame;
    public static final int Login  = 0;
    public static final int Register  = 1;
    public static final int FeedBack  = 2;
    public static final int ChatMsg  = 4;
    public  BuildFrame(int msgType) {
        frame = Frame.newBuilder();

        frame.setProtoSign(1234);
        frame.setMsgLength(1);
        frame.setMsgType(msgType);
        frame.setSenderTime(10000);
    }
    //获得原始的帧
    public Frame.Builder GetOriginFrameBuilder(){
        return frame;
    }
    //获得登录帧
    public Frame GetLoginFrame(IUser userToLogin){
        if (userToLogin.getName()!=null&&userToLogin.getPasswd()!=null&&userToLogin.getName()!="")
        {
            User.Builder src = User.newBuilder();
            src.setUserName(userToLogin.getName());
            src.setUserPwd(userToLogin.getPasswd());
            src.setUserID(userToLogin.getID());
            frame.setSrc(src.build());
            return frame.build();
        }
        Log.e(" bad value", "BuildFrame,GetLoginFrame");
        System.out.println("BuildFrame,GetLoginFrame bad value");
        return null;
    }
    //获得聊天帧
    public Frame GetChatMsgFrame(IMsg chatMsg){
        if(chatMsg.getLeft_name()!=null&&chatMsg.getRight_name()!=null&&chatMsg.getMessage()!=null&&
                chatMsg.getLeft_name()!=""&&chatMsg.getRight_name()!=""&&chatMsg.getMessage()!=""){
            //发送源
            User.Builder src = User.newBuilder();
            src.setUserName(chatMsg.getRight_name());
            //接收者
            User.Builder dst = User.newBuilder();
            dst.setUserName(chatMsg.getLeft_name());

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
        Log.e(" bad value", "BuildFrame,GetLoginFrame");
        System.out.println("BuildFrame,GetLoginFrame bad value");
        return null;
    }
}