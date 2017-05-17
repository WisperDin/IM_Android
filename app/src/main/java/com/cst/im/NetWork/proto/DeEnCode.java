package com.cst.im.NetWork.proto;

import android.util.Log;

import com.cst.im.UI.main.chat.ChatMsgViewAdapter;
import com.cst.im.model.IBaseMsg;
import com.cst.im.model.IFileMsg;
import com.cst.im.model.IFriend;
import com.cst.im.model.ILoginUser;
import com.cst.im.model.ITextMsg;
import com.cst.im.model.UserModel;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import protocol.Protocol.Frame;
/**
 * Created by ASUS on 2017/4/27.
 */
public class DeEnCode {
    //编码

    //编码-登录帧
    public static byte[] encodeLoginFrame(ILoginUser loginUser) {
        Frame frame = new BuildFrame(BuildFrame.Login).GetLoginFrame(loginUser);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(frame==null){
            Log.w("frame null","encodeLoginFrame");
            return null;
        }
        try {
            frame.writeTo(baos);
            //添加消息长度
            long msglength=baos.toByteArray().length;
            baos.reset();
            Frame.Builder builder=frame.toBuilder();
            builder.setMsgLength(msglength);
            frame=builder.build();
            frame.writeTo(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }
    //编码-聊天消息帧
        public static byte[] encodeChatMsgFrame(IBaseMsg chatMsg) {
//            switch (chatMsg.getMsgType()) {
//                case TEXT:
//                    //强制类型转换
//                    ITextMsg txtMsg = ((ITextMsg) chatMsg);
//                    Frame frame = new BuildFrame(BuildFrame.ChatMsg).GetChatMsgFrame(txtMsg);
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    try {
//                        frame.writeTo(baos);
//                    } catch (IOException e) {
//                    }
//                    return baos.toByteArray();
//            }
            ITextMsg txtMsg = ((ITextMsg) chatMsg);
            txtMsg.setType(ChatMsgViewAdapter.FROM_USER_MSG);
            Frame frame = new BuildFrame(BuildFrame.TextMsg).GetChatMsgFrame(txtMsg);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if(frame==null){
                Log.w("frame null","encodeChatMsgFrame");
                return null;
            }
            try {
                frame.writeTo(baos);
                //添加消息长度
                long msglength=baos.toByteArray().length;
                baos.reset();
                Frame.Builder builder=frame.toBuilder();
                builder.setMsgLength(msglength);
                frame=builder.build();
                frame.writeTo(baos);
            } catch (IOException e) {
            }
            return baos.toByteArray();
            //若各个路径都无信息，返回NULL
            //return null;
        }

    //编码-获取好友列表帧
    public static byte[] encodeGetFriendListFrame(IFriend friendL) {
        Frame frame = new BuildFrame(BuildFrame.GetFriend).GetFriendList(friendL);
        if(frame==null){
            Log.w("frame null","encodeGetFriendListFrame");
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            frame.writeTo(baos);
            //添加消息长度
            long msglength=baos.toByteArray().length;
            baos.reset();
            Frame.Builder builder=frame.toBuilder();
            builder.setMsgLength(msglength);
            frame=builder.build();
            frame.writeTo(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }

    //编码-判断是否为好友帧
    public static byte[] encodeIsFriendFrame(IFriend Isfriend) {
        Frame frame = new BuildFrame(BuildFrame.IsFriend).IsFriend(Isfriend);
        if(frame==null){
            Log.w("frame null","encodeIsFriendFrame");
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            frame.writeTo(baos);
            //添加消息长度
            long msglength=baos.toByteArray().length;
            baos.reset();
            Frame.Builder builder=frame.toBuilder();
            builder.setMsgLength(msglength);
            frame=builder.build();
            frame.writeTo(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }

    //编码-添加好友帧（模糊查找）
    public static  byte[] encodeAddFriendUncertainFrame(IFriend addfrienduncertain){
        Frame frame = new BuildFrame(BuildFrame.AddFriend).AddFriendUncertain(addfrienduncertain);
        if(frame==null){
            Log.w("frame null","encodeAddFriendUncertainFrame");
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            frame.writeTo(baos);
            //添加消息长度
            long msglength=baos.toByteArray().length;
            baos.reset();
            Frame.Builder builder=frame.toBuilder();
            builder.setMsgLength(msglength);
            frame=builder.build();
            frame.writeTo(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }
    //编码-添加好友帧
    public static byte[] encodeAddFriendFrame(IFriend addfriend) {
        Frame frame = new BuildFrame(BuildFrame.AddFriend).IsFriend(addfriend);
        if(frame==null){
            Log.w("frame null","encodeIsFriendFrame");
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            frame.writeTo(baos);
            //添加消息长度
            long msglength=baos.toByteArray().length;
            baos.reset();
            Frame.Builder builder=frame.toBuilder();
            builder.setMsgLength(msglength);
            frame=builder.build();
            frame.writeTo(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }


    //编码文件简要信息帧
    public static byte[] encodeFileMsgFrameHead(IFileMsg fileMsg){
        Frame frame = new BuildFrame(BuildFrame.FileInfo).GetFileInfoFrame(fileMsg);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(frame==null){
            Log.w("frame null","encodeFileMsgFrameHead");
            return null;
        }
        try {
            frame.writeTo(baos);
            //添加消息长度
            long msglength=baos.toByteArray().length;
            baos.reset();
            Frame.Builder builder=frame.toBuilder();
            builder.setMsgLength(msglength);
            frame=builder.build();
            frame.writeTo(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }

//    //编码文件简要信息帧
//    public static byte[] encodeFileMsgFrameHead(IFileMsg fileMsg){
//        Frame frame = new BuildFrame(BuildFrame.FileInfo).GetFileInfoFrame(fileMsg);
//        //fileMsg.setMsgType(frame.getMsgType());
//        fileMsg.setMsgType(IBaseMsg.MsgType.PHOTO);
//        switch(fileMsg.getMsgType()){
//            case PHOTO:
//                fileMsg.setType(ChatMsgViewAdapter.FROM_USER_IMG);
//                break;
//            case FILE:
//                Log.d("文件" , "_______________________文件传输布局还没做_________________");
//                break;
//            case SOUNDS:
//                fileMsg.setType(ChatMsgViewAdapter.FROM_USER_VOICE);
//                break;
//        }
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        try {
//            frame.writeTo(baos);
//        } catch (IOException e) {
//        }
//        return baos.toByteArray();
//    }

    //解码-所有帧
    public static Frame decodeFrame(byte[] buffer) {
        Frame frame = null;
        try {
            frame = Frame.parseFrom(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return frame;
    }

    //编码-注册帧
    public static byte[] encodeRegisterFrame(ILoginUser userToRegister){
        Frame frame = new BuildFrame(BuildFrame.Register).GetRegisterFrame(userToRegister);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if(frame==null){
            Log.w("frame null","encodeRegisterFrame");
            return null;
        }
        try {
            frame.writeTo(baos);
            //添加消息长度
            long msglength=baos.toByteArray().length;
            baos.reset();
            Frame.Builder builder=frame.toBuilder();
            builder.setMsgLength(msglength);
            frame=builder.build();
            frame.writeTo(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }
    //编码上传用户信息帧
    public static byte[] encodePushUserInfo(UserModel userModel){
        Frame buildFrame = new BuildFrame(BuildFrame.PushUserInfo).GetPushUserInfoFrame(userModel);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if(buildFrame==null){
            Log.w("frame null","encodePushUserInfo");
            return null;
        }
        try{
            buildFrame.writeTo(byteArrayOutputStream);
            //添加消息长度
            long msglength=byteArrayOutputStream.toByteArray().length;
            byteArrayOutputStream.reset();
            Frame.Builder builder=buildFrame.toBuilder();
            builder.setMsgLength(msglength);
            buildFrame=builder.build();
            buildFrame.writeTo(byteArrayOutputStream);
        }catch (IOException e){

        }
        return byteArrayOutputStream.toByteArray();
    }
    //编码请求用户信息帧
    public static byte[] encodePullUserInfo(UserModel userModel){
        Frame buildFrame = new BuildFrame(BuildFrame.PullUserInfo).GetPullUserInfoFrame(userModel);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if(buildFrame==null){
            Log.w("frame null","encodePullUserInfo");
            return null;
        }
        try{
            buildFrame.writeTo(byteArrayOutputStream);
            //添加消息长度
            long msglength=byteArrayOutputStream.toByteArray().length;
            byteArrayOutputStream.reset();
            Frame.Builder builder=buildFrame.toBuilder();
            builder.setMsgLength(msglength);
            buildFrame=builder.build();
            buildFrame.writeTo(byteArrayOutputStream);
        }catch (IOException e){

        }
        return byteArrayOutputStream.toByteArray();
    }
}
