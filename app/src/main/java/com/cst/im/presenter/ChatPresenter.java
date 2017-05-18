package com.cst.im.presenter;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.cst.im.FileAccess.FileSweet;
import com.cst.im.NetWork.ComService;
import com.cst.im.NetWork.Okhttp.impl.FileImRequest;
import com.cst.im.NetWork.Okhttp.impl.ImRequest;
import com.cst.im.NetWork.proto.DeEnCode;
import com.cst.im.UI.main.chat.ChatMsgViewAdapter;
import com.cst.im.UI.main.chat.ListViewChatActivity;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2017/4/23.
 */

public class ChatPresenter implements IChatPresenter,ComService.ChatMsgHandler{
    private List<IBaseMsg> mDataArrays = new ArrayList<IBaseMsg>();// 消息对象数组
    private IChatView iChatView;
    private  Handler handler;
    private final Activity activity;
    //此窗口的目的用户
    private IUser[] dstUser;
    private int[] dst_ID;
    public ChatPresenter(IChatView chatView , List<IBaseMsg> msg,IUser[] dstUser) {
        //参数检查
        if(dstUser==null||dstUser.length<=0||msg==null||chatView==null){
            Log.e("ChatPresenter","param error");
            activity= null;
            return;
        }
        this.iChatView =  chatView;
        this.activity= ((ListViewChatActivity) chatView);
        this.mDataArrays = msg;
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

       //test
       /* FileImRequest.Builder().downLoadFile(FileSweet.FILE_TYPE_PICTURE, FileUtils.getFileNameNoEx("1.txt"),new ImRequest.ResultCallBack(){

            @Override
            public void fail(int code, String msg) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "下载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void success(int code, String msg) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "下载成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });*/
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
        mDataArrays.add(msgRecv);
        DBManager.InsertMsg(msgRecv);
        int fileType = 0;
        //判断数据类型
        switch(msgRecv.getMsgType()) {
            case TEXT:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iChatView.onRecvMsg();
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
            public void fail(int code, String msg) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "下载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void success(int code, String msg) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //TODO be care the cast
                        Toast.makeText(activity, "下载成功", Toast.LENGTH_SHORT).show();
                       // ArrayList<String> imgList = new ArrayList<String>();

                        if(msgRecv.getMsgType()== IBaseMsg.MsgType.PHOTO){//只有图片才需要添加到imgList
                            File file = new File(FileUtils.getFilePath(FileSweet.FILE_TYPE_PICTURE),fileNameNoEx);
                            if(!file.exists()){
                                Log.e("file","open failed");
                            }
                            ChatMsgViewAdapter chatMsgAdapter = ((ListViewChatActivity) activity).mAdapter;
                            chatMsgAdapter.getImageList().add(file.getAbsolutePath());
                            chatMsgAdapter.getImagePosition().put(chatMsgAdapter.getCount()-1,chatMsgAdapter.getImageList().size()-1);
                        }

                    }
                });
            }
        });
/*        ArrayList<String> imgList = new ArrayList<String>();
        File file = new File(FileUtils.getFilePath(FileSweet.FILE_TYPE_PICTURE),fileNameNoEx);
        if(!file.exists()){
            Log.e("file","open failed");
        }
        imgList.add(file.getAbsolutePath());
        ((ListViewChatActivity) activity).mAdapter.setImageList(imgList);*/
    }
    //发送一般文件
    @Override
    public void SendFile(File file, IBaseMsg.MsgType msgType)  {
        //参数检查
        if(file==null||!file.exists()){
            Log.e("SendFile","param error");
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
                fileMsg.setFileSize(fileSize);
                fileMsg.setFileParam(fileSize);
/*                //设置URL
                try{
                    //设置URL路径
                    fileMsg.setFileUrl(file.toURL().toString());
                }catch (MalformedURLException mie){
                    mie.printStackTrace();
                    Log.e("setFileUrl","failed");
                    return;
                }*/
                break;
            case PHOTO:
                fileMsg = new PhotoMsgModel();
                iChatView.onSendImg(((PhotoMsgModel) fileMsg));
                fileType = FileSweet.FILE_TYPE_PICTURE;

                //添加图片到imgList
                ChatMsgViewAdapter chatMsgAdapter = ((ListViewChatActivity) activity).mAdapter;
                chatMsgAdapter.getImageList().add(file.getAbsolutePath());
                chatMsgAdapter.getImagePosition().put(chatMsgAdapter.getCount()-1,chatMsgAdapter.getImageList().size()-1);

                //参数
                fileSize = FileUtils.getAutoFileOrFilesSize(file);
                fileMsg.setFileSize(fileSize);
                fileMsg.setFileParam(fileSize);
                break;
            case SOUNDS:
                fileMsg = new SoundMsgModel();
                try{
                    //设置URL路径
                    ((SoundMsgModel) fileMsg).setSoundUrl(file.toURL().toString());
                }catch (MalformedURLException mie){
                    mie.printStackTrace();
                    Log.e("setSoundUrl","failed");
                    return;
                }
                // 设置时长
                String filePath = RecordUtils.getAudioPath();
                int duration = RecordUtils.getDuration(filePath);
                Log.d("Record", "duration : " + String.valueOf(duration));
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
        }
        if(msgType==null||fileType==0){
            Log.w("msgType||fileType","null");
            return;
        }
        fileMsg.setFile(file);
        fileMsg.setSrc_ID(UserModel.localUser.getId());
        fileMsg.setDst_ID(dst_ID);
        fileMsg.setMsgType(msgType);
        fileMsg.setMsgDate(Tools.getDate());
        //更新适配器的数据
        mDataArrays.add(fileMsg);


        //使用http上传文件
        // TODO: 2017/5/8 delete it just test,cjwddz
        //解析文件
        FileSweet fs = null;
        try {
            fs = new FileSweet(fileType, file);
            //注，语音文件的文件名为文件指纹
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(fs==null){
            Log.e("SendFile","FileSweet null");
            return;
        }
        fs.setFileParam(fileMsg.getFileParam());
        FileImRequest.Builder().upLoadFile(fs, new ImRequest.ResultCallBack() {
            @Override
            public void fail(int code, String msg) {
                // TODO: 2017/5/8 给某个View做点事
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void success(int code, String msg) {
                // TODO: 2017/5/8 某个View做点事
                // TODO: 2017/5/8 如果操作不了UI的话调到主线程操作，如果！
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity, "上传成功", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        //发送文件简要信息帧到服务器
        //文件指纹
        fileMsg.setFileFeature(fs.getFeature());
        //文件名,如果是语音文件就用指纹代替
        fileMsg.setFileName(msgType== IBaseMsg.MsgType.SOUNDS ? fs.getFeature():file.getName());
        final byte[] fileHeadToSend = DeEnCode.encodeFileMsgFrameHead(fileMsg);
        if(fileHeadToSend==null){
            Log.w("file","fileHeadToSend null");
            System.out.println("fileHeadToSend null");
        }
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    ComService.client.SendData(fileHeadToSend);
                } catch (IOException ioe) {
                    Log.w("send", "send file []byte failed");
                    System.out.println("send file []byte failed");
                }
            }
        });

    }


    //发送文字信息
    @Override
    public void SendMsg(String contString){
        if (contString.length() > 0) {
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
            //调用发送数据接口
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        ComService.client.SendData(chatMsgFrame);
                    }
                    catch (IOException ioe)
                    {
                        Log.w("send","send data failed");
                    }
                }});
            mDataArrays.add(textMsg);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    iChatView.onSendMsg();
                }});
        }

    }
}
