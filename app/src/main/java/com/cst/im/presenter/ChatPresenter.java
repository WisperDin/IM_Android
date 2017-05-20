package com.cst.im.presenter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cst.im.FileAccess.FileSweet;
import com.cst.im.NetWork.ComService;
import com.cst.im.NetWork.Okhttp.impl.FileImRequest;
import com.cst.im.NetWork.Okhttp.impl.ImRequest;
import com.cst.im.NetWork.proto.DeEnCode;
import com.cst.im.dataBase.DBManager;
import com.cst.im.model.FileMsgModel;
import com.cst.im.model.IBaseMsg;
import com.cst.im.model.IFileMsg;
import com.cst.im.model.ITextMsg;
import com.cst.im.model.IUser;
import com.cst.im.model.PhotoMsgModel;
import com.cst.im.model.SoundMsgModel;
import com.cst.im.model.TextMsgModel;
import com.cst.im.model.UserModel;
import com.cst.im.tools.FileUtils;
import com.cst.im.tools.RecordUtils;
import com.cst.im.view.IChatView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by ASUS on 2017/4/23.
 */

public class ChatPresenter implements IChatPresenter,ComService.ChatMsgHandler{
    private IChatView iChatView;
    //做的东西包含UI更新的话一定要用handle.post
    private  Handler handler;
    //此窗口的目的用户
    private IUser[] dstUser;
    private int[] dst_ID;
    public ChatPresenter(IChatView chatView ,IUser[] dstUser) {
        //参数检查
        if(dstUser==null||dstUser.length<=0||chatView==null){
            Log.e("ChatPresenter","param error");
            return;
        }
        this.iChatView =  chatView;
        handler = new Handler(Looper.getMainLooper());
        //开启的这个聊天窗口的目的用户
        this.dstUser = dstUser;
        //将dstUser的ID取出
        dst_ID = new int[dstUser.length];
        for(int i = 0 ; i <dstUser.length ; i++){
            dst_ID[i] = dstUser[i].getId();
        }
        //监听收到消息的接口
        ComService.setChatMsgCallback(this);
    }
    //匹配发来的信息的发送源用户是否存在于当前窗口的目的用户
    public boolean CheckSrcID(int srcID){
        if(srcID==0){
            Log.e("CheckSrcID","srcID bad value");
            return false;
        }
        if(dst_ID==null||dst_ID.length<=0){
            Log.e("CheckSrcID","dst_ID bad value");
            return false;
        }
        for(int i=0;i<dst_ID.length;i++){
                if(dst_ID[i]==srcID){//匹配
                    return true;
                }
        }
        return false;
    }
    //匹配发来的信息的目的用户是否就是自己
    public static boolean CheckDstID(int[] dstID){
        if(dstID==null||dstID.length<=0){
            Log.e("CheckDstID","dstID bad value");
            return false;
        }
        if(UserModel.localUser==null||UserModel.localUser.getId()==0){
            Log.e("CheckDstID","localUser id bad value");
            return false;
        }
        for(int i=0;i<dstID.length;i++){
            if(dstID[i]==UserModel.localUser.getId()){//匹配
                return true;
            }
        }
        return false;
    }
    //接受到新的消息 //参数为接收到的消息
    @Override
    public void handleChatMsgEvent(final IBaseMsg msgRecv){
        if(msgRecv==null||msgRecv.getMsgType()==null){
            Log.e("handleChatMsgEvent","msgRecv 相关参数为空");
            return;
        }
        //判断收到这条信息的确是来自当前这个聊天窗口的对象的
        if(!CheckSrcID(msgRecv.getSrc_ID())){
            Log.d("handleChatMsgEvent","这条信息的发送者不在当前窗口的对象用户-----------------");
            return;
        }
        //判断这条信息的确是发给自己的
        if(!CheckDstID(msgRecv.getDst_ID())){
            Log.w("handleChatMsgEvent","这条信息的目的用户不是自身-----------------");
            return;
        }
        DBManager.InsertMsg(msgRecv);
        int fileType = 0;
        //判断数据类型
        switch(msgRecv.getMsgType()) {
            case TEXT:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iChatView.onRecvTextMsg(((TextMsgModel) msgRecv));
                    }
                });
                return;//文字消息的处理到这里就结束了
            case FILE:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iChatView.onReceriveFileText((FileMsgModel)msgRecv);
                    }
                });
                fileType = FileSweet.FILE_TYPE_FILE;

                break;
            case PHOTO:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iChatView.onReceriveImageText((PhotoMsgModel) msgRecv);
                    }
                });
                fileType = FileSweet.FILE_TYPE_PICTURE;
                break;
            case SOUNDS:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iChatView.onReceriveSoundText((SoundMsgModel) msgRecv);
                    }
                });
                fileType = FileSweet.FILE_TYPE_SOUND;
                break;
            default:
                Log.e("handleChatMsgEvent","msgRecv 类型不正确");
                return;

        }
        //file name without prefix
        if(((FileMsgModel) msgRecv).getFileName()==null||((FileMsgModel) msgRecv).getFileName()==""){
            Log.e("handleChatMsgEvent","filename bad value");
            return;
        }
        final String fileNameNoEx = FileUtils.getFileNameNoEx(((FileMsgModel) msgRecv).getFileName());
        if(fileNameNoEx==null||fileNameNoEx==""){
            Log.e("handleChatMsgEvent","fileNameNoEx bad value");
            return;
        }
        FileImRequest.Builder().downLoadFile(fileType,fileNameNoEx,new ImRequest.ResultCallBack(){

            @Override
            public void fail(final int code, final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iChatView.onFileDownloadFailed(code,msg);
                    }
                });
            }

            @Override
            public void success(int code, String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                            if(msgRecv.getMsgType()== IBaseMsg.MsgType.PHOTO){//只有图片才需要添加到imgList
                                File file = new File(FileUtils.getFilePath(FileSweet.FILE_TYPE_PICTURE),fileNameNoEx);
                                if(!file.exists()){
                                    Log.e("file","open failed");
                                    return;
                                }
                                msgRecv.setPhotoLocal(file.getAbsolutePath());
                                iChatView.onDownLoadImageSuccess(((PhotoMsgModel) msgRecv));
                            }
                    }
                });

            }
        });
    }
    //发送一般文件
    @Override
    public void SendFile(File file, IBaseMsg.MsgType msgType)  {
        //传入参数检查 消息，文件
        if(file==null||!file.exists()||msgType==null){
            Log.e("SendFile","param error");
            return;
        }
        //参数检查 目的用户
        if(dst_ID==null||dst_ID.length<=0){
            Log.e("SendFile","dst_ID bad value");
            return;
        }
        //参数检查 localUser
        if(UserModel.localUser==null||UserModel.localUser.getId()==0||
                UserModel.localUser.getName()==null||UserModel.localUser.getName()==""){
            Log.e("SendFile","UserModel.localUser exception");
            return;
        }
        IFileMsg fileMsg = null;
        String fileSize = null;
        int fileType = 0;
        switch(msgType) {
            case FILE:
                fileMsg = new FileMsgModel();
                iChatView.onSendFileMsg(fileMsg);
                fileType = FileSweet.FILE_TYPE_FILE;
                //参数
                fileSize = FileUtils.getAutoFileOrFilesSize(file);
                if(fileSize==null||fileSize==""){
                    Log.e("SendFile","fileSize exception");
                    return;
                }
                fileMsg.setFileSize(fileSize);
                fileMsg.setFileParam(fileSize);
                break;
            case PHOTO:
                fileMsg = new PhotoMsgModel();
                fileMsg.setPhotoLocal(file.getAbsolutePath());
                iChatView.onSendImg(((PhotoMsgModel) fileMsg));
                fileType = FileSweet.FILE_TYPE_PICTURE;

                //参数
                fileSize = FileUtils.getAutoFileOrFilesSize(file);
                if(fileSize==null||fileSize==""){
                    Log.e("SendFile","fileSize exception");
                    return;
                }
                fileMsg.setFileSize(fileSize);
                fileMsg.setFileParam(fileSize);
                break;
            case SOUNDS:
                fileMsg = new SoundMsgModel();
                try{
                    //设置URL路径
                    ((SoundMsgModel) fileMsg).setFileUrl(file.toURL().toString());
                }catch (MalformedURLException mie){
                    mie.printStackTrace();
                    Log.e("setSoundUrl","failed");
                    return;
                }
                // 设置时长
                String filePath = RecordUtils.getAudioPath();
                if(filePath==null||filePath==""){
                    Log.e("SendFile","Audio Path error");
                    return;
                }
                int duration = RecordUtils.getDuration(filePath);
                Log.d("Record", "duration : " + String.valueOf(duration));
                if (duration <= 0) {
                    Log.e("SendFile","duration <=0");
                    return;
                }
                float voiceTime = duration/1000.0f;
                ((SoundMsgModel) fileMsg).setUserVoiceTime(voiceTime);

                // 设置时间戳
                ((SoundMsgModel) fileMsg).setMsgDate(Tools.getDate());
                Log.d("Record","Time : " + Tools.getDate());

                //参数为时长
                fileMsg.setFileParam(Float.toString(voiceTime));

                iChatView.onSendVoice(((SoundMsgModel) fileMsg));
                fileType = FileSweet.FILE_TYPE_SOUND;
                break;
            default:
                Log.e("SendFile","msgType 类型不正确");
                return;
        }
        if(msgType==null||fileType==0){
            Log.w("msgType||fileType","null");
            return;
        }
        fileMsg.setFile(file);
        fileMsg.setSrc_ID(UserModel.localUser.getId());
        fileMsg.setSrc_Name(UserModel.localUser.getName());
        fileMsg.setDst_ID(dst_ID);
        fileMsg.setMsgType(msgType);
        fileMsg.setMsgDate(Tools.getDate());
        //使用http上传文件
        //解析文件
        FileSweet fs = null;
        try {
            fs = new FileSweet(fileType, file);
            //注，语音文件的文件名为文件指纹
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(fs==null||fs.getFeature()==null
                ||fs.getFeature()==""){
            Log.e("SendFile","FileSweet param error");
            return;
        }
        fs.setFileParam(fileMsg.getFileParam());

        FileImRequest.Builder().upLoadFile(fs, new ImRequest.ResultCallBack() {
            @Override
            public void fail(final int code,final String msg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iChatView.onFileUploadFailed(code,msg);
                    }
                });
            }
            @Override
            public void success(int code, String msg) {
                    //TODO 上传成功先不提示
            }
        });
        //发送文件简要信息帧到服务器
        //文件指纹
        fileMsg.setFileFeature(fs.getFeature());
        //文件名,如果是语音文件就用指纹代替
        fileMsg.setFileName(msgType == IBaseMsg.MsgType.SOUNDS ? fs.getFeature():file.getName());
        final byte[] fileHeadToSend = DeEnCode.encodeFileMsgFrameHead(fileMsg);
        if(fileHeadToSend==null){
            Log.e("file","fileHeadToSend data null");
            return;
        }
        //发送消息
        try {
            ComService.client.SendData(fileHeadToSend);
        } catch (IOException ioe) {
            Log.e("send", "send file []byte failed");
            return;
        }

    }


    //发送文字信息
    @Override
    public void SendMsg(String contString){
        //参数检查
        if(UserModel.localUser==null||UserModel.localUser.getId()==0||UserModel.localUser.getName()==""
                ||contString==null||contString.length() <= 0
                ||dst_ID==null||dst_ID.length<=0){
            Log.e("SendMsg", "param error");
            return;
        }
        ITextMsg textMsg = new TextMsgModel();
        textMsg.setSrc_ID(UserModel.localUser.getId());
        textMsg.setSrc_Name(UserModel.localUser.getName());
        textMsg.setMsgDate(Tools.getDate());
        textMsg.setText(contString);
        textMsg.sendOrRecv(false);
        textMsg.setDst_ID(dst_ID);
        textMsg.setMsgType(IBaseMsg.MsgType.TEXT);
        DBManager.InsertMsg(textMsg);
        //发送数据到服务器
        //编码聊天消息帧
        final byte[] chatMsgFrame = DeEnCode.encodeChatMsgFrame(textMsg);
        if (chatMsgFrame == null) {
            Log.e("SendMsg", "chatMsgFrame data null");
            return;
        }
        //发送消息
        try {
            ComService.client.SendData(chatMsgFrame);
        } catch (IOException ioe) {
            Log.w("send", "send data failed");
        }
        //更新适配器数据
        iChatView.onShowMsg(textMsg);
    }
}
