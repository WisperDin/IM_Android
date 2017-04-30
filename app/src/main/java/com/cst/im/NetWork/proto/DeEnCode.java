package com.cst.im.NetWork.proto;

import com.cst.im.model.IUser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import protocol.Protocol.*;
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
    //解码-反馈帧
    public static Frame decodeFbFrame(InputStream is) {
        Frame frame = null;
        try {
            frame = Frame.parseFrom(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return frame;
    }
}
