package com.cst.im.NetWork;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cst.im.NetWork.proto.BuildFrame;
import com.cst.im.model.IMsg;
import com.cst.im.model.MsgModel;

import java.util.concurrent.CopyOnWriteArrayList;

import protocol.Protocol.Action;
import protocol.Protocol.Frame;

/**
 * Created by cjwddz on 2017/4/23.
 */

public class ComService extends TcpService {
    public interface MsgHandler{
        //void handleEvent(Frame frame);
        void handleFbEvent(int rslCode);//参数为反馈的状态码与状态信息
    }
    public interface ChatMsgHandler{
        void handleChatMsgEvent(IMsg msgRecv);//参数为接收到的消息
    }



    static CopyOnWriteArrayList<MsgHandler> msgListeners;
    static MsgHandler registerEvent;
    static MsgHandler loginFbEvent;
    static ChatMsgHandler chatMsgEvent;


    public static void setRegisterCallback(MsgHandler registerCallback){
        registerEvent =registerCallback;
    }
    public static void setLoginCallback(MsgHandler loginCallback){
        loginFbEvent=loginCallback;
    }

    public static void setChatMsgCallback(ChatMsgHandler chatMsgCallback){
        chatMsgEvent=chatMsgCallback;
    }

    @Override
    public void OnTcpStop() {
        // TODO: 2017/4/26 tcp连接断开处理
    }
    @Override
    public void OnMessageCome(Frame frame) {
        //根据消息类型分发客户端收到的消息
        switch (frame.getMsgType()){
            case BuildFrame.FeedBack://反馈信息
            {
                Log.d("OnMessage","feedback");
                Action action =  frame.getFbAction();
                //选择反馈信息的类型
                switch (action.getActionType()){
                    case BuildFrame.Login://登录反馈信息
                        Log.d("OnMessageCome","登录反馈");
                        if(loginFbEvent!=null)//执行登录反馈事件
                            loginFbEvent.handleFbEvent(action.getRslCode());
                        break;
                }
                break;
            }
            case BuildFrame.ChatMsg://聊天消息
            {
                System.out.println("chatMsg");
                //检查是否空
                if (frame.getSrc().getUserName()!=""&&frame.getDst().getDstCount()>0&&frame.getMsg().getMsg()!="")
                {
                    //模拟了一个date
                    IMsg msgRecv = new MsgModel(frame.getSrc().getUserName(),
                            frame.getDst().getDst(0).getUserName(),
                            "1000",
                            frame.getMsg().getMsg(),
                            true);
                    if(chatMsgEvent!=null)//执行登录反馈事件
                        chatMsgEvent.handleChatMsgEvent(msgRecv);
                }else{
                    Log.e(" bad value", "ConService,OnMessageCome ChatMsg");
                    System.out.println("ConService,OnMessageCome ChatMsg bad value");
                }
                break;

            }


            /*case 2://注册事件
                if(registerEvent!=null)
                    registerEvent.handleEvent(msg);
                break;
            case 3://消息事件
                handlerMsg(msg);
                break;*/
            default:
                break;
        }
    }
    /*
    private void handlerMsg(JSONObject msg){
        if(msgListeners==null || msgListeners.size()<=0)
            return;
        for (MsgHandler listener : msgListeners) {
            listener.handleEvent(msg);
        }
    }*/

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBind();
    }
    public class MyBind extends Binder {
        public ComService getService(){
            return ComService.this;
        }
    }
}
