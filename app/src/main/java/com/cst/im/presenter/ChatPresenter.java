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
import com.cst.im.UI.main.chat.ListViewChatActivity;
import com.cst.im.dataBase.DBManager;
import com.cst.im.model.FileMsgModel;
import com.cst.im.model.IBaseMsg;
import com.cst.im.model.IFileMsg;
import com.cst.im.model.ITextMsg;
import com.cst.im.model.IUser;
import com.cst.im.model.TextMsgModel;
import com.cst.im.model.UserModel;
import com.cst.im.tools.FileUtils;
import com.cst.im.view.IChatView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
    public ChatPresenter(IChatView chatView , List<IBaseMsg> msg) {
        this.iChatView =  chatView;
        this.activity= ((ListViewChatActivity) chatView);
        this.mDataArrays = msg;
        handler = new Handler(Looper.getMainLooper());
        //监听收到消息的接口
        ComService.setChatMsgCallback(this);

       /* //test
        FileImRequest.Builder().downLoadFile(FileSweet.FILE_TYPE_FILE, FileUtils.getFileNameNoEx("1.txt"),new ImRequest.ResultCallBack(){

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


    //接受到新的消息 //参数为接收到的消息
    @Override
    public void handleChatMsgEvent(final IBaseMsg msgRecv){
        //TODO: 做一个判断，判断这条信息的确是发给当前这个聊天窗口的对象的
        mDataArrays.add(msgRecv);
        DBManager.InsertMsg(msgRecv);
        //判断数据类型
        switch(msgRecv.getMsgType()) {
            case TEXT:
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        iChatView.onRecvMsg();
                    }
                });
                break;
            case FILE:
                //TODO： 这个路径还要改
                FileImRequest.Builder().downLoadFile(FileSweet.FILE_TYPE_FILE, FileUtils.getFileNameNoEx(((FileMsgModel) msgRecv).getFileName()),new ImRequest.ResultCallBack(){

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
                });
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        //TODO 出问题
                        //iChatView.onRecvMsg();
                    }
                });
                break;
            case PHOTO:
                break;
            case SOUNDS:
                break;
        }
    }
    //发送一般文件
    @Override
    public void SendFile(IUser[] dstUser ,File file){
        //将dstUser的ID取出
        int dst_ID[] = new int[dstUser.length];
        for(int i = 0 ; i <dstUser.length ; i++){
            dst_ID[i] = dstUser[i].getId();
        }
        IFileMsg fileMsg = new FileMsgModel();
        fileMsg.setFile(file);
        fileMsg.setFileName(file.getName());
        fileMsg.setSrc_ID(UserModel.localUser.getId());
        fileMsg.setDst_ID(dst_ID);
        fileMsg.setMsgType(IBaseMsg.MsgType.FILE);
        fileMsg.setMsgDate(Tools.getDate());
        //使用http上传文件
        // TODO: 2017/5/8 delete it just test,cjwddz
        try {
            FileSweet fs = new FileSweet(FileSweet.FILE_TYPE_FILE, file);
            //使用文件信息写入到FileMsg中
            fileMsg.setFileSize(fs.getFileParam());
            fileMsg.setFileParam(fs.getFileParam());
            fileMsg.setFileFeature(fs.getFeature());
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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //发送文件简要信息帧到服务器
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

        //更新UI的适配器
        mDataArrays.add(fileMsg);
        handler.post(new Runnable() {
            @Override
            public void run() {
                iChatView.onSendMsg();
            }});
    }


    //发送文字信息
    @Override
    public void SendMsg(IUser[] dstUser,String contString){
        //将dstUser的ID取出
        int dst_ID[] = new int[dstUser.length];
        for(int i = 0 ; i <dstUser.length ; i++){
            dst_ID[i] = dstUser[i].getId();
        }
        if (contString.length() > 0) {
            ITextMsg textMsg = new TextMsgModel();
            textMsg.setSrc_ID(UserModel.localUser.getId());
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
