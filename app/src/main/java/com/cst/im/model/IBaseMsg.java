package com.cst.im.model;

/**
 * Created by ASUS on 2017/5/4.
 */

public interface IBaseMsg {
    //发送源用户id
    int getSrc_ID();
    void setSrc_ID(int src_id);
    //发送源用户名
    String getSrc_Name();
    void setSrc_Name(String src_name);
    //发送目的用户id组
    int[] getDst_ID();
    int getDst_IDAt(int pos);
    void setDst_ID(int[] dst_id);
    //发送时间
    String getMsgDate();
    void setMsgDate(String msgDate);
    //消息类型，标志消息是文字，图片，语音，文件等
    //分别为1，2，3，4
    enum MsgType{
        TEXT,PHOTO,FILE,SOUNDS
    }
    MsgType  getMsgType();
    int  getMsgTypeInt();
    void setMsgType(MsgType msgType);

    boolean sendOrRecv();
    void sendOrRecv(boolean isComMsg);

    int getSendState();
    void setSendState(int sendState);

    int getType();
    void setType(int type);

    String getPhotoLocal();
    void setPhotoLocal(String imageLocal);
}
