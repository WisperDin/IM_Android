package com.cst.im.model;

/**
 * Created by ASUS on 2017/4/23.
 */

public class MsgModel implements IMsg {

    public MsgModel() {
    }

    public MsgModel(String left_name, String right_name,String date, String text, boolean isComMsg) {
        super();
        this.left_name = left_name;
        this.right_name = right_name;
        this.date = date;
        this.message = text;
        this.isComMeg = isComMsg;
    }

    String left_name;//消息来自
    String right_name;//用户自身
    String date;//消息日期
    String message;//消息内容
    boolean isComMeg = true;// 是否为收到的消息

    public String getLeft_name() {
        return left_name;
    }

    public void setLeft_name(String left_name) {
        this.left_name = left_name;
    }

    public String getRight_name() {
        return right_name;
    }

    public void setRight_name(String right_name) {
        this.right_name = right_name;
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
