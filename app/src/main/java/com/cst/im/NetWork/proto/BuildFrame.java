package com.cst.im.NetWork.proto;

import android.util.Log;

import com.cst.im.model.IBaseMsg;
import com.cst.im.model.IFriend;
import com.cst.im.model.ILoginUser;
import com.cst.im.model.ITextMsg;
import com.cst.im.model.TextMsgModel;

import protocol.Protocol.DstUser;
import protocol.Protocol.Frame;
import protocol.Protocol.Msg;
import protocol.Protocol.User;

public class BuildFrame {

    private Frame.Builder frame;
    public static final int Login  = 1;
    public static final int Register  = 2;
    public static final int FeedBack  = 3;
    public static final int ChatMsg  = 5;
    public static final int FileSend = 6;
    public static final int GetFriend  = 7;
    public static final int IsFriend=8;
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

    public Frame GetRegisterFrame(ILoginUser userToRegister){
        User.Builder src = User.newBuilder();
        src.setUserName(userToRegister.getUsername());
        src.setUserPwd(userToRegister.getPassword());
        frame.setSrc(src.build());
        return frame.build();
    }

    //获得聊天帧
    public Frame GetChatMsgFrame(IBaseMsg chatMsg){
        if(chatMsg.getDst_ID()!=null&&chatMsg.getDst_ID().length!=0&&chatMsg.getMsgType()!=null){
            //发送源
            User.Builder src = User.newBuilder();
            src.setUserID(chatMsg.getSrc_ID());
            //接收者
            User.Builder dst = User.newBuilder();
            dst.setUserID(chatMsg.getDst_IDAt(0));

            DstUser.Builder dstGroup = DstUser.newBuilder();
            dstGroup.addDst(dst);
            //TODO 要发送的信息
            switch(chatMsg.getMsgType()){
                case TEXT:
                    ITextMsg txtMsg = ((ITextMsg) chatMsg);
                    Msg.Builder msg = Msg.newBuilder();
                    msg.setMsg(txtMsg.getText());
                    frame.setSrc(src.build());
                    frame.setDst(dstGroup.build());
                    frame.setMsg(msg);
                    return frame.build();
                case FILE:
                    break;
                case SOUNDS:
                    break;
                case PHOTO:
                    break;
            }
        }
        Log.e(" bad value", "BuildFrame,GetLoginFrame");
        System.out.println("BuildFrame,GetLoginFrame bad value");
        return null;
    }

    //获取好友列表帧
    public Frame GetFriendList(IFriend GetFriendList){
        if (GetFriendList.getId()!=0)
        {
            User.Builder src = User.newBuilder();
            src.setUserID(GetFriendList.getId());
            frame.setSrc(src.build());
            return frame.build();
        }
        Log.e(" bad value", "BuildFrame,GetFriendListFrame");
        System.out.println("BuildFrame,GetFriendListFrame bad value");
        return null;
    }

    //获取判断是否为好友帧
    public Frame IsFriend(IFriend IsFriend){
        if (IsFriend.getId()!=0&&IsFriend.SearchId()!=0)
        {
            User.Builder src = User.newBuilder();
            src.setUserID(IsFriend.getId());
            DstUser.Builder dstGroup = DstUser.newBuilder();
            User.Builder dst = User.newBuilder();
            dst.setUserID(IsFriend.SearchId());
            dstGroup.addDst(dst);
            frame.setSrc(src.build());
            frame.setDst(dstGroup);
            return frame.build();
        }
        Log.e(" bad value", "BuildFrame,IsFriendFrame");
        System.out.println("BuildFrame,IsFriendFrame bad value");
        return null;
    }

}