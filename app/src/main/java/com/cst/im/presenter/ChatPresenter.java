package com.cst.im.presenter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cst.im.NetWork.ComService;
import com.cst.im.NetWork.proto.DeEnCode;
import com.cst.im.dataBase.DBManager;
import com.cst.im.model.IMsg;
import com.cst.im.model.IUser;
import com.cst.im.model.MsgModel;
import com.cst.im.model.UserModel;
import com.cst.im.view.IChatView;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2017/4/23.
 */

public class ChatPresenter implements IChatPresenter,ComService.ChatMsgHandler{
    private List<IMsg> mDataArrays = new ArrayList<IMsg>();// 消息对象数组
    private IChatView iChatView;
    private  Handler handler;
    private IUser localUser;//假设这个是登录这个客户端的用户
    private int DstID = 2;
    public ChatPresenter(IChatView chatView , List<IMsg> msg) {
        this.iChatView =  chatView;
        this.mDataArrays = msg;
        handler = new Handler(Looper.getMainLooper());
        localUser=new UserModel("lzy","123",1);
        //监听收到消息的接口
        ComService.setChatMsgCallback(this);
    }


    //接受到新的消息 //参数为接收到的消息
    @Override
    public void handleChatMsgEvent(final IMsg msgRecv){
        //TODO: 做一个判断，判断这条信息的确是发给当前这个聊天窗口的对象的
        Log.d("test" , "_________________________________________________________________");
        Log.d("Message" , msgRecv.getMessage());
        mDataArrays.add(msgRecv);
        DBManager.InsertMsg(msgRecv);
        handler.post(new Runnable() {
            @Override
            public void run() {
                iChatView.onRecvMsg(msgRecv.getMessage(), msgRecv.getDate());
            }});

    }
    //发送一般文件
    @Override
    public void SendFile(File fileToSend){
        //获取文件二进制流

        byte[] fileData=null;
        try {
            long longlength = fileToSend.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            RandomAccessFile f = new RandomAccessFile(fileToSend, "r");
            fileData = new byte[length];
            f.readFully(fileData);
            System.out.println(fileData);


        }
        catch (IOException IOE){
            Log.w("file","file send failed");
            System.out.println("file send failed");
            return;

        }
        if(fileData!=null)
        {
            final byte[] fileDataToSend = fileData.clone();
            //发送数据
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        ComService.client.SendData(fileDataToSend);
                    }
                    catch (IOException ioe)
                    {
                        Log.w("send","send file []byte failed");
                        System.out.println("send file []byte failed");
                    }
                }});
        }
        else
        {
            Log.w("file","fileData null");
            System.out.println("fileData null");
        }


    }


    //发送文字信息
    @Override
    public void SendMsg(String contString){
        if (contString.length() > 0) {
            IMsg entity = new MsgModel();
            entity.setRight_name(localUser.getName());
            entity.setDate(Tools.getDate());
            entity.setMessage(contString);
            entity.setMsgType(false);
            entity.setRight_ID(DstID);

            entity.setLeft_name("lzy");
            DBManager.InsertMsg(entity);
            //发送数据到服务器
            //编码聊天消息帧
            final byte[] chatMsgFrame = DeEnCode.encodeChatMsgFrame(entity);
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
            mDataArrays.add(entity);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    iChatView.onSendMsg();
                }});
        }

    }
}
