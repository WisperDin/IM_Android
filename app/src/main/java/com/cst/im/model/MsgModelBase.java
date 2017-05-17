package com.cst.im.model;

import android.util.Log;

/**
 * Created by ASUS on 2017/5/4.
 */

public class MsgModelBase implements IBaseMsg {
    int srcID;//消息源
    int[] dstID;//消息目的
    String msgDate;//消息日期
    IBaseMsg.MsgType msgType;// 消息类型
    String src_name; //发送源用户名
    String dst_name;//发送目的名
    boolean isComMeg = true;// 是否为收到的消息
    public int sendState;
    private int type;
    String photoLocal;



    //发送源用户名
    public String getSrc_Name(){return src_name;}
    public void setSrc_Name(String src_name){
        this.src_name = src_name;
    }

    //发送源用户id
    public int getSrc_ID(){return srcID;}
    public void setSrc_ID(int src_id){
        this.srcID=src_id;
    }
    //发送目的用户id组
    public int[] getDst_ID(){return dstID;}
    public int getDst_IDAt(int pos){
        if (pos<dstID.length)
            return dstID[pos];
        Log.w("getDstID","get dst index beyond the group");
        return -1;
    }
    public void setDst_ID(int[] dst_id){
        this.dstID=dst_id;
    }
    //发送时间
    public String getMsgDate(){return msgDate;}
    public void setMsgDate(String msgDate){
        this.msgDate=msgDate;
    }

    //消息类型，标志消息是文字，图片，语音，文件等
    public IBaseMsg.MsgType getMsgType(){return  msgType;}
    public int  getMsgTypeInt(){
        switch (msgType){
            case TEXT:
                return 1;
            case PHOTO:
                return 2;
            case FILE:
                return 3;
            case SOUNDS:
                return 4;
            case LOCATION:
                return 5;
            default:
                return 0;
        }
    }
    public void setMsgType(IBaseMsg.MsgType msgType){
        this.msgType=msgType;
    }

    //判断消息操作：发送/接受
    public boolean sendOrRecv() {
        return isComMeg;
    }
    public void sendOrRecv(boolean isComMsg) {
        isComMeg = isComMsg;
    }

    public  MsgModelBase(){}


    public int getSendState() {
        return this.sendState;
    }

    public void setSendState(int sendState) {
        this.sendState = sendState;
    }


    //消息类型
    public int getType() {
        return this.type;
    }
    public void setType(int type) {
        this.type = type;
    }
    public String getPhotoLocal() {
        return this.photoLocal;
    }

    public void setPhotoLocal(String photoLocal) {
        this.photoLocal = photoLocal;
    }

}
