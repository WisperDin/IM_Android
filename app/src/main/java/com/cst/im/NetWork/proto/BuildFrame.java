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
    public static final int FileSend = 5;
    public static final int GetFriend  = 6;
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
    public Frame GetLoginFrame(ILoginUser loginUser){
        if (loginUser.getUsername()!=null&&loginUser.getPassword()!=null&&loginUser.getUsername()!="")
        {
            User.Builder src = User.newBuilder();
            src.setUserName(loginUser.getUsername());
            src.setUserPwd(loginUser.getPassword());
            frame.setSrc(src.build());
            return frame.build();
        }
        Log.e(" bad value", "BuildFrame,GetLoginFrame");
        System.out.println("BuildFrame,GetLoginFrame bad value");
        return null;
    }

    public Frame GetRegisterFrame(IUser userToRegister){
        User.Builder src = User.newBuilder();
        src.setUserName(userToRegister.getName());
        src.setUserPwd(userToRegister.getPasswd());
        frame.setSrc(src.build());
        return frame.build();
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

    //获取好友列表帧
    public Frame GetFriendList(IFriend GetFriendList){
        if (GetFriendList.getname()!=null)
        {
            User.Builder src = User.newBuilder();
            src.setUserName(GetFriendList.getname());
            frame.setSrc(src.build());
            return frame.build();
        }
        Log.e(" bad value", "BuildFrame,GetFriendListFrame");
        System.out.println("BuildFrame,GetLoginFrame bad value");
        return null;
    }

}