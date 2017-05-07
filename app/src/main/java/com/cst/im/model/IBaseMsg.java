package com.cst.im.model;

/**
 * Created by ASUS on 2017/5/4.
 */

public interface IBaseMsg {
    //发送源用户id
    int getSrc_ID();
    void setSrc_ID(int src_id);
    //发送目的用户id组
    int[] getDst_ID();
    int getDst_IDAt(int pos);
    void setDst_ID(int[] dst_id);
    //发送时间
    String getMsgDate();
    void setMsgDate(String msgDate);
    //消息类型，标志消息是文字，图片，语音，文件等
    enum MsgType{
        TEXT,PHOTO,FILE,SOUNDS
    }
    MsgType  getMsgType();
    void setMsgType(MsgType msgType);

    boolean sendOrRecv();
    void sendOrRecv(boolean isComMsg);
}
