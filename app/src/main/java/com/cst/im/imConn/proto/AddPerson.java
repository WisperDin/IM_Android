package com.cst.im.imConn.proto;

import tutorial.Example;

public class AddPerson {
    static Example.Person createPerson(String personName) {
        Example.Person.Builder person = Example.Person.newBuilder();

        int id = 13958235;
        person.setId(id);

        String name = personName;
        person.setName(name);

        String email = "zhangsan@gmail.com";
        person.setEmail(email);

        Example.Person.PhoneNumber.Builder phoneNumber = Example.Person.PhoneNumber.newBuilder();
        phoneNumber.setType(Example.Person.PhoneType.HOME);
        phoneNumber.setNumber("0157-23443276");

        person.addPhones(phoneNumber.build());

        phoneNumber = Example.Person.PhoneNumber.newBuilder();
        phoneNumber.setType(Example.Person.PhoneType.MOBILE);
        phoneNumber.setNumber("136183667387");

        person.addPhones(phoneNumber.build());

        return person.build();
    }
}