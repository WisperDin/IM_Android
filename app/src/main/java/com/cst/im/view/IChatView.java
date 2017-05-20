package com.cst.im.view;


import com.cst.im.model.IBaseMsg;
import com.cst.im.model.IFileMsg;
import com.cst.im.model.IPhotoMsg;
import com.cst.im.model.ISoundMsg;
import com.cst.im.model.ITextMsg;

/**
 * Created by ASUS on 2017/4/23.
 */

public interface IChatView {
     void onShowMsg(IBaseMsg msg);
     void onRecvTextMsg(ITextMsg txtMsg);
     void onSendImg(IPhotoMsg msg);
     void onReceriveImageText(IPhotoMsg msg);
     void onDownLoadImageSuccess(IPhotoMsg msg);//成功下载完图片的回调
     void onSendVoice(final ISoundMsg soundMsg);
     void onReceriveSoundText(final ISoundMsg soundMsg);
     void onSendFileMsg(final IFileMsg fileMsg);
     void onReceriveFileText(final IFileMsg fileMsg);

     void onFileDownloadFailed(int code, String msg);
     void onFileUploadFailed(int code, String msg);
}
