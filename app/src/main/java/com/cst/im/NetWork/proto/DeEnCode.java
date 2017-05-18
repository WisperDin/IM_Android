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
    //编码-帧 添加帧长度
    private static byte[] encodeFrame(Frame frame){
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
            Log.e("encodeFrame","IOException:"+e.getMessage());
            return null;
        }
        return baos.toByteArray();
    }
    //编码-登录帧
    public static byte[] encodeLoginFrame(ILoginUser loginUser) {
        Frame frame = new BuildFrame(BuildFrame.Login).GetLoginFrame(loginUser);
        if(frame==null){
            Log.e("frame null","encodeLoginFrame");
            return null;
        }
        //编码成字节
        byte[] data = encodeFrame(frame);
        if(data==null){
            Log.e("encodeLoginFrame","data null");
            return null;
        }
        return data;
    }
    //编码-聊天消息帧
        public static byte[] encodeChatMsgFrame(IBaseMsg chatMsg) {
            ITextMsg txtMsg = ((ITextMsg) chatMsg);
            txtMsg.setType(ChatMsgViewAdapter.FROM_USER_MSG);
            Frame frame = new BuildFrame(BuildFrame.TextMsg).GetChatMsgFrame(txtMsg);
            if(frame==null){
                Log.e("frame null","encodeChatMsgFrame");
                return null;
            }
            byte[] data = encodeFrame(frame);
            if(data==null){
                Log.e("encodeChatMsgFrame","data null");
                return null;
            }
            return data;
        }

    //编码-获取好友列表帧
    public static byte[] encodeGetFriendListFrame(IFriend friendL) {
        Frame frame = new BuildFrame(BuildFrame.GetFriend).GetFriendList(friendL);
        if(frame==null){
            Log.e("frame null","encodeGetFriendListFrame");
            return null;
        }
        //编码成字节
        byte[] data = encodeFrame(frame);
        if(data==null){
            Log.e("encodeGetFriendFrame","data null");
            return null;
        }
        return data;
    }

    //编码-判断是否为好友帧
    public static byte[] encodeIsFriendFrame(IFriend Isfriend) {
        Frame frame = new BuildFrame(BuildFrame.IsFriend).IsFriend(Isfriend);
        if(frame==null){
            Log.e("frame null","encodeIsFriendFrame");
            return null;
        }
        //编码成字节
        byte[] data = encodeFrame(frame);
        if(data==null){
            Log.e("encodeIsFriendFrame","data null");
            return null;
        }
        return data;
    }

    //编码-添加好友帧（模糊查找）
    public static  byte[] encodeAddFriendUncertainFrame(IFriend addfrienduncertain){
        Frame frame = new BuildFrame(BuildFrame.AddFriend).AddFriendUncertain(addfrienduncertain);
        if(frame==null){
            Log.w("frame null","encodeAddFriendUncertainFrame");
            return null;
        }
        //编码成字节
        byte[] data = encodeFrame(frame);
        if(data==null){
            Log.e("encodeFriendUncertain","data null");
            return null;
        }
        return data;
    }
    //编码-添加好友帧
    public static byte[] encodeAddFriendFrame(IFriend addfriend) {
        Frame frame = new BuildFrame(BuildFrame.AddFriend).IsFriend(addfriend);
        if(frame==null){
            Log.w("frame null","encodeIsFriendFrame");
            return null;
        }
        //编码成字节
        byte[] data = encodeFrame(frame);
        if(data==null){
            Log.e("encodeAddFriendFrame","data null");
            return null;
        }
        return data;
    }


    //编码文件简要信息帧
    public static byte[] encodeFileMsgFrameHead(IFileMsg fileMsg){
        Frame frame = new BuildFrame(BuildFrame.FileInfo).GetFileInfoFrame(fileMsg);
        if(frame==null){
            Log.w("frame null","encodeFileMsgFrameHead");
            return null;
        }
        //编码成字节
        byte[] data = encodeFrame(frame);
        if(data==null){
            Log.e("encodeFileMsgFrameHead","data null");
            return null;
        }
        return data;
    }

    //编码-注册帧
    public static byte[] encodeRegisterFrame(ILoginUser userToRegister){
        Frame frame = new BuildFrame(BuildFrame.Register).GetRegisterFrame(userToRegister);
        if(frame==null){
            Log.w("frame null","encodeRegisterFrame");
            return null;
        }
        //编码成字节
        byte[] data = encodeFrame(frame);
        if(data==null){
            Log.e("encodeRegisterFrame","data null");
            return null;
        }
        return data;
    }
    //编码上传用户信息帧
    public static byte[] encodePushUserInfo(UserModel userModel){
        Frame frame = new BuildFrame(BuildFrame.PushUserInfo).GetPushUserInfoFrame(userModel);
        if(frame==null){
            Log.e("frame null","encodePushUserInfo");
            return null;
        }
        //编码成字节
        byte[] data = encodeFrame(frame);
        if(data==null){
            Log.e("encodePushUserInfo","data null");
            return null;
        }
        return data;
    }
    //编码请求用户信息帧
    public static byte[] encodePullUserInfo(UserModel userModel){
        Frame frame = new BuildFrame(BuildFrame.PullUserInfo).GetPullUserInfoFrame(userModel);
        if(frame==null){
            Log.w("frame null","encodePullUserInfo");
            return null;
        }
        //编码成字节
        byte[] data = encodeFrame(frame);
        if(data==null){
            Log.e("encodePullUserInfo","data null");
            return null;
        }
        return data;
    }


    //解码-所有帧
    public static Frame decodeFrame(byte[] buffer) {
        Frame frame = null;
        try {
            frame = Frame.parseFrom(buffer);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("decodeFrame","Exception:"+e.getMessage());
            return null;
        }
        return frame;
    }
}
