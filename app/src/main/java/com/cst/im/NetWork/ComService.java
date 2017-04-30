package com.cst.im.NetWork;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by cjwddz on 2017/4/23.
 */

public class ComService extends TcpService {
    public interface MsgHandler{
        void handleEvent(JSONObject object);
    }

    static CopyOnWriteArrayList<MsgHandler> msgListeners;
    static MsgHandler registerEvent;
    static MsgHandler loginEvent;

    public static void setRegisterCallback(MsgHandler registerCallback){
        registerEvent =registerCallback;
    }
    public static void setLoginCallback(MsgHandler loginCallback){
        loginEvent=loginCallback;
    }
    public static void addMsgListener(MsgHandler msgListener){
        if (msgListeners == null) {
            msgListeners = new CopyOnWriteArrayList<>();
        }
        msgListeners.add(msgListener);
    }
    public static void removeMsgListener(MsgHandler msgListener){
        if (msgListeners == null) {
            msgListeners = new CopyOnWriteArrayList<>();
        }
        msgListeners.add(msgListener);
    }
    
/*    public static boolean sendMessageObj(JSONObject object){
        if(client==null ||object==null)
            return false;
        try {
            client.sendJSON(object);
            return true;
        } catch (IOException e) {
           return false;
        }
    }*/

    @Override
    public void OnTcpStop() {
        // TODO: 2017/4/26 tcp连接断开处理
    }
    @Override
    public void OnMessageCome(JSONObject msg) throws JSONException {
        switch (msg.getInt("msgType")){
            case 1://登录事件
                if(loginEvent!=null)
                    loginEvent.handleEvent(msg);
                break;
            case 2://注册事件
                if(registerEvent!=null)
                    registerEvent.handleEvent(msg);
                break;
            case 3://消息事件
                handlerMsg(msg);
                break;
            default:
                break;
        }
    }
    private void handlerMsg(JSONObject msg){
        if(msgListeners==null || msgListeners.size()<=0)
            return;
        for (MsgHandler listener : msgListeners) {
            listener.handleEvent(msg);
        }
    }

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
