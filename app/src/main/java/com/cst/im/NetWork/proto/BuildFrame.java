package com.cst.im.NetWork.proto;

import android.util.Log;

import com.cst.im.UI.main.chat.ChatMsgViewAdapter;
import com.cst.im.dataBase.DBManager;
import com.cst.im.model.IBaseMsg;
import com.cst.im.model.IFileMsg;
import com.cst.im.model.IFriend;
import com.cst.im.model.ILoginUser;
import com.cst.im.model.ITextMsg;
import com.cst.im.model.UserModel;

import protocol.Protocol;
import protocol.Protocol.DstUser;
import protocol.Protocol.Frame;
import protocol.Protocol.Msg;
import protocol.Protocol.User;

public class BuildFrame {

    private Frame.Builder frame;
    public static final int Login  = 1;
    public static final int Register  = 2;
    public static final int FeedBack  = 3;
    public static final int TextMsg  = 5;
    public static final int FileInfo = 6;
    public static final int GetFriend  = 7;
    public static final int IsFriend = 8;
    public static final int PullUserInfo = 9;//获取远程用户信息
    public static final int PushUserInfo = 10;//上传远程用户信息

    //=====用户信息返回值======//
    public static final int PullUserInfoSuccess = 300;
    public static final int PullUserInfoFail = 301;
    public static final int PushUserInfoSuccess = 302;
    public static final int PushUserInfoFail = 303;


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
        if(chatMsg.getDst_ID()!=null&&chatMsg.getDst_ID().length!=0){
            //chatMsg.getDst_ID()!=null&&chatMsg.getDst_ID().length!=0&&chatMsg.getMsgType()!=null
            //发送源
            User.Builder src = User.newBuilder();
            src.setUserID(chatMsg.getSrc_ID());
            //接收者
            User.Builder dst = User.newBuilder();
            dst.setUserID(chatMsg.getDst_IDAt(0));
            DstUser.Builder dstGroup = DstUser.newBuilder();
            dstGroup.addDst(dst);
            //要发送的信息，先模拟字符串发送
            ITextMsg txtMsg = ((ITextMsg) chatMsg);
            Msg.Builder msg = Msg.newBuilder();
            msg.setMsg(txtMsg.getText());
            frame.setSrc(src.build());
            frame.setDst(dstGroup.build());
            frame.setMsg(msg);
            //设置消息布局类型<文本>
            txtMsg.setType(ChatMsgViewAdapter.TO_USER_MSG);
            return frame.build();
//            switch(chatMsg.getMsgType()){
//                case TEXT:
//                    ITextMsg txtMsg = ((ITextMsg) chatMsg);
//                    Msg.Builder msg = Msg.newBuilder();
//                    msg.setMsg(txtMsg.getText());
//                    frame.setSrc(src.build());
//                    frame.setDst(dstGroup.build());
//                    frame.setMsg(msg);
//                    return frame.build();
//                case FILE:
//                    break;
//                case SOUNDS:
//                    break;
//                case PHOTO:
//                    break;
//            }
        }
        Log.e(" bad value", "BuildFrame,GetLoginFrame");
        System.out.println("BuildFrame,GetLoginFrame bad value");
        return null;
    }
    //获取文件简要消息帧
    public Frame GetFileInfoFrame(IFileMsg fileMsg){
        //参数判断
        if(fileMsg.getSrc_ID()==0||fileMsg.getDst_ID()==null||fileMsg.getDst_ID().length<=0||
                fileMsg.getFile()==null||fileMsg.getFileName()==null||fileMsg.getFileName()==""||
                fileMsg.getFileFeature()==null||fileMsg.getFileParam()==null){
            Log.e(" bad value", "BuildFrame,GetFileInfoFrame");
            System.out.println("BuildFrame,GetFileInfoFrame bad value");
            return null;
        }
        //发送源
        User.Builder src = User.newBuilder();
        src.setUserID(fileMsg.getSrc_ID());
        //接收者
        User.Builder dst = User.newBuilder();
        dst.setUserID(fileMsg.getDst_IDAt(0));
        DstUser.Builder dstGroup = DstUser.newBuilder();
        dstGroup.addDst(dst);
        //文件信息
        Protocol.File.Builder file = Protocol.File.newBuilder();
        file.setFileName(fileMsg.getFileName());
        file.setFileFeature(fileMsg.getFileFeature());
        file.setFileParam(fileMsg.getFileParam());
        Log.d("fileMsg.getMsgType()" , "_______________"+fileMsg.getMsgType()+"_________________________");
        switch (fileMsg.getMsgType()){
            case FILE:
                Log.d("文件" , "_____________简要信息______________");
                break;
            case SOUNDS:
                fileMsg.setType(ChatMsgViewAdapter.TO_USER_VOICE);
                break;
            case PHOTO:
                fileMsg.setType(ChatMsgViewAdapter.TO_USER_IMG);
                break;
        }
        //文件类型
        file.setFileType(fileMsg.getMsgTypeInt());

        frame.setSrc(src.build());
        frame.setDst(dstGroup.build());
        frame.setFileInfo(file);
        return frame.build();

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

    //编码获取用户信息帧
    public Frame GetPullUserInfoFrame(UserModel userModel){
        if(userModel.getId() == 0){
            return null;
        }

        User.Builder user = User.newBuilder();
        user.setUserID(userModel.getId());
        frame.setSrc(user.build());
        frame.setMsgType(PullUserInfo);
        return frame.build();
    }

    //编码获取用户信息帧
    public Frame GetPushUserInfoFrame(UserModel userModel){
        if(userModel.getId() == 0){
            return null;
        }

        User.Builder user = User.newBuilder();
        //TODO 头像先不管
        user.setUserID(userModel.getId());
        user.setUserName(userModel.getName());
        user.setAge(userModel.getAge());
        user.setSex(userModel.getUserSex());
        user.setRealName(userModel.getUserRealName());
        user.setPhone(userModel.getUserPhone());
        user.setEmail(userModel.getUserEmail());
        user.setAddress(userModel.getUserAddress());
        user.setSign(userModel.getUserSign());

        frame.setSrc(user.build());
        frame.setMsgType(PushUserInfo);
        return frame.build();
    }
}