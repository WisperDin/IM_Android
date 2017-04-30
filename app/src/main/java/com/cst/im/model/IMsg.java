package com.cst.im.model;

/**
 * Created by ASUS on 2017/4/23.
 */

public interface IMsg {
    String getLeft_name();
    void setLeft_name(String left_name);

    String getRight_name();
    void setRight_name(String right_name);

    String getDate();
    void setDate(String date);

    String getMessage();
    void setMessage(String message);

    boolean getMsgType();
    void setMsgType(boolean isComMsg);

}
