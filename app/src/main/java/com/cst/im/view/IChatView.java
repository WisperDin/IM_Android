package com.cst.im.view;


import com.cst.im.model.IBaseMsg;
import com.cst.im.model.IFileMsg;
import com.cst.im.model.IPhotoMsg;
import com.cst.im.model.ISoundMsg;

/**
 * Created by ASUS on 2017/4/23.
 */

public interface IChatView {
     void onSendMsg();
     void onRecvMsg();
     void onSendImg(IPhotoMsg msg);
     void onReceriveImageText(IPhotoMsg msg);
     void onSendVoice(final ISoundMsg soundMsg);
     void onReceriveSoundText(final ISoundMsg soundMsg);
}
