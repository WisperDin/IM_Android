package com.cst.im.NetWork.proto;

import android.util.Log;

import com.cst.im.FileAccess.FileAccess;
import com.cst.im.model.FileMsgModel;
import com.cst.im.model.IFileMsg;
import com.cst.im.model.IFriend;
import com.cst.im.model.ILoginUser;
import com.cst.im.model.IMsg;
import com.cst.im.model.IUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import protocol.Protocol.Frame;
/**
 * Created by ASUS on 2017/4/27.
 */
public class DeEnCode {
    //特殊帧帧头
    public static final byte SpecialFrameHead = (byte) 0xAA;
    //特殊帧帧头长度
    public static final int SpecialFrameLength=6;
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
    //字节数组合并
    private static synchronized byte[] mergerByte(byte[] bytes_1, byte[] bytes_2) {
        byte[] bytes = new byte[bytes_1.length + bytes_2.length];
        System.arraycopy(bytes_1, 0, bytes, 0, bytes_1.length);
        System.arraycopy(bytes_2, 0, bytes, bytes_1.length, bytes_2.length);
        return bytes;
    }
    //文件流转字节流
    public static byte[] encodeFileToByte(File file){
        try {
            long longlength = file.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            RandomAccessFile f = new RandomAccessFile(file, "r");
            byte[] fileData = new byte[length];
            f.readFully(fileData);
            //System.out.println(fileData);
            return fileData;
        }
        catch (IOException IOE){
            Log.w("file","file send failed");
            System.out.println("file send failed");

        }
        return null;
    }
    //short转byte数组
    public static byte[] shortToByteArray(short s) {
        //java 里面的short是大端存储方式，数据的高位存放在低地址位
        byte[] targets = new byte[2];
        targets[1] = (byte) ((s >> 8) & 0xff);
        targets[0] = (byte) (s & 0xff);
        return targets;
    }
    //byte数组转short
    public static short byteArrayToShort(byte H,byte L) {
        //java 里面的short是大端存储方式，数据的高位存放在低地址位
        return (short)((H&0xFF)<<8 |(L& 0xFF));
    }
    //编码文件头 包含帧头，帧类型，源，目的
    public static byte[] encodeFileMsgFrameHead(IFileMsg fileMsg){
/*        Frame head = new BuildFrame(BuildFrame.FileSend).GetFileMsgFrame(fileMsg);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            head.writeTo(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();*/
        byte[] srcID = shortToByteArray((short)fileMsg.getSrc_ID());
        byte[] dstID = shortToByteArray((short)fileMsg.getDst_IDAt(0));
        byte[] fileHead = new byte[]{SpecialFrameHead,(byte) BuildFrame.FileSend,srcID[0],srcID[1],dstID[0],dstID[1]};
        return fileHead;
    }
    //编码-文件发送帧（固定标志头+文件）
    public static byte[] encodeFileMsgFrame(IFileMsg fileMsg) {
        byte[] fileMsgHead = encodeFileMsgFrameHead(fileMsg);
        byte[] fileData = encodeFileToByte(fileMsg.getFile());

        return mergerByte(fileMsgHead,fileData);
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
    //解码-特殊帧
    public static void decodeSpFrame(byte[] frameData){
        int msgType=frameData[1];
        switch (msgType){
            case BuildFrame.FileSend://文件帧
                DeEnCode.decodeFileFrame(frameData);
                break;
            default:
                break;
        }
    }
   //解码-文件帧
    public static IFileMsg decodeFileFrame(byte[] buffer){
        IFileMsg fileMsg = new FileMsgModel();
        fileMsg.setSrc_ID(byteArrayToShort(buffer[3],buffer[2]));
        int[] dstID = new int[]{byteArrayToShort(buffer[5],buffer[4])};
        fileMsg.setDst_ID(dstID);
        byte[] fileData = Arrays.copyOfRange(buffer,SpecialFrameLength,buffer.length);
        fileMsg.setFile(FileAccess.WriteFile("abc.txt",fileData));
        System.out.println("save file ok");
        return  fileMsg;
    }


}
