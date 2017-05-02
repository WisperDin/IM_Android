package com.cst.im.NetWork;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.cst.im.NetWork.proto.BuildFrame;

import java.util.concurrent.CopyOnWriteArrayList;

import protocol.Protocol.Action;
import protocol.Protocol.Frame;

/**
 * Created by cjwddz on 2017/4/23.
 */

public class ComService extends TcpService {

    public interface MsgHandler {
        //void handleEvent(Frame frame);
        void handleFbEvent(int rslCode, String rslMsg);//参数为反馈的状态码与状态信息
    }

    static CopyOnWriteArrayList<MsgHandler> msgListeners;
    static MsgHandler registerEvent;
    static MsgHandler loginFbEvent;

    public static void setRegisterCallback(MsgHandler registerCallback) {
        registerEvent = registerCallback;
    }

    public static void setLoginCallback(MsgHandler loginCallback) {
        loginFbEvent = loginCallback;
    }

    @Override
    public void OnTcpStop() {
        // TODO: 2017/4/26 tcp连接断开处理
    }

    @Override
    public void OnMessageCome(Frame frame) {
        //根据消息类型分发客户端收到的消息
        switch (frame.getMsgType()) {
            case BuildFrame.FeedBack://反馈信息
                System.out.println("fb");
                Action action = frame.getFbAction();
                //选择反馈信息的类型
                switch (action.getActionType()) {
                    case BuildFrame.Login://登录反馈信息
                        System.out.println("loginfb");
                        if (loginFbEvent != null)//执行登录反馈事件
                            loginFbEvent.handleFbEvent(action.getRslCode(), action.getRslMsg());
                        break;
                }
            //注册事件
            /*case BuildFrame.Register:
                if(registerEvent!=null)
                    registerEvent.handleEvent(msg);
                break;*/
            /*
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
        public ComService getService() {
            return ComService.this;
        }
    }
}
