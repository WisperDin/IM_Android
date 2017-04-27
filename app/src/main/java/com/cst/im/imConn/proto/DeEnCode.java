package com.cst.im.imConn.proto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

        //编码
   public static byte[] encodeTest() {
        tutorial.Example.Person person = AddPerson.createPerson("lzy");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            person.writeTo(baos);
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
    //解码
    public static tutorial.Example.Person decodeTest(InputStream is) {
        tutorial.Example.Person person = null;
        try {
            person = tutorial.Example.Person.parseFrom(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return person;
    }
}
