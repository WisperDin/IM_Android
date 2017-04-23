package com.cst.im.model;

/**
 * Created by ASUS on 2017/4/23.
 */

public class MsgModel implements  IMsg {

    public MsgModel() {
    }

    public MsgModel(String name, String date, String text, boolean isComMsg) {
        super();
        this.name = name;
        this.date = date;
        this.message = text;
        this.isComMeg = isComMsg;
    }

    String name;//消息来自
    String date;//消息日期
    String message;//消息内容
    boolean isComMeg = true;// 是否为收到的消息

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getMsgType() {
        return isComMeg;
    }

    public void setMsgType(boolean isComMsg) {
        isComMeg = isComMsg;
    }


}
