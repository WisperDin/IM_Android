package com.cst.im.model;

/**
 * Created by ASUS on 2017/4/23.
 */

public interface IMsg {
    String getName();
    void setName(String name);

    String getDate();
    void setDate(String date);

    String getMessage();
    void setMessage(String message);

    boolean getMsgType();
    void setMsgType(boolean isComMsg);

}
