package com.cst.im.NetWork.proto;

import android.util.Log;

import com.cst.im.model.IFileMsg;
import com.cst.im.model.IMsg;
import com.cst.im.model.IUser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

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
   public static byte[] encodeLoginFrame(IUser userToLogin) {
       Frame frame = new BuildFrame(BuildFrame.Login).GetLoginFrame(userToLogin);
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
    public static byte[] encodeFileMsgFrameHead(IFileMsg fileMsg){
        Frame head = new BuildFrame(BuildFrame.FileSend).GetFileMsgFrame(fileMsg);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            head.writeTo(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }
    //编码-文件发送帧（proto帧头+文件）
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
}
