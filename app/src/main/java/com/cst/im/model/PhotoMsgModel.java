package com.cst.im.model;

/**
 * Created by wzb on 2017/5/8.
 */

public class PhotoMsgModel extends MsgModelBase implements IPhotoMsg {
    String photoUrl;
    @Override
    public String getPhotoUrl() {
        return photoUrl;
    }

    @Override
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
