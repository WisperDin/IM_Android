package com.cst.im.NetWork.proto;

import com.cst.im.model.IFriend;
import com.cst.im.model.ILoginUser;
import com.cst.im.model.IMsg;
import com.cst.im.model.IUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import protocol.Protocol.Frame;
/**
 * Created by ASUS on 2017/4/27.
 */
public class DeEnCode {
    //编码
/*    public static byte[] encodeTest(String[] names) {
        tutorial.Example.AddressBook.Builder addressBook = tutorial.Example.AddressBook.newBuilder();
        for(int i = 0; i < names.length; ++ i) {
            addressBook.addPeople(AddPerson.createPerson(names[i]));
        }
        tutorial.Example.AddressBook book = addressBook.build();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            book.writeTo(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }*/

    //编码-登录帧
    public static byte[] encodeLoginFrame(ILoginUser loginUser) {
        Frame frame = new BuildFrame(BuildFrame.Login).GetLoginFrame(loginUser);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            frame.writeTo(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }
    //编码-聊天消息帧
    public static byte[] encodeChatMsgFrame(IMsg chatMsg) {
        Frame frame = new BuildFrame(BuildFrame.ChatMsg).GetChatMsgFrame(chatMsg);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            frame.writeTo(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }

    //编码-获取好友列表帧
    public static byte[] encodeGetFriendListFrame(IFriend friendL) {
        Frame frame = new BuildFrame(BuildFrame.GetFriend).GetFriendList(friendL);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            frame.writeTo(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }
    /*//解码
    public static tutorial.Example.AddressBook decodeTest(InputStream is) {
        tutorial.Example.AddressBook addressBook = null;
        try {
            addressBook = tutorial.Example.AddressBook.parseFrom(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return addressBook;
    }*/
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
    public static byte[] encodeRegisterFrame(IUser userToRegister){
        Frame frame = new BuildFrame(BuildFrame.Register).GetRegisterFrame(userToRegister);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            frame.writeTo(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }
}
