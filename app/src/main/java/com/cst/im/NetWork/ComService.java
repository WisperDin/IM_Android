package com.cst.im.NetWork;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.cst.im.NetWork.proto.BuildFrame;
import com.cst.im.model.IBaseMsg;
import com.cst.im.model.IFriend;
import com.cst.im.model.IFriendModel;
import com.cst.im.model.ITextMsg;
import com.cst.im.model.TextMsgModel;
import com.cst.im.presenter.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import protocol.Protocol.Action;
import protocol.Protocol.Frame;
import protocol.Protocol.User;

/**
 * Created by cjwddz on 2017/4/23.
 */

public class ComService extends TcpService {
    public interface MsgHandler{
        //void handleEvent(Frame frame);
        void handleFbEvent(int rslCode,int id);//参数为反馈的状态码与状态信息
    }
    public interface ChatMsgHandler{
        void handleChatMsgEvent(IBaseMsg msgRecv);//参数为接收到的消息
    }
    public interface ChatListHandler{
        void handleChatListEvent(IBaseMsg msgRecv);//参数为接收到的消息
    }

    public interface FriendListHandler{
        void handleFriendLisEvent(IFriend fl);//参数为接收到的消息
    }


    static CopyOnWriteArrayList<MsgHandler> msgListeners;
    static MsgHandler registerEvent;
    static MsgHandler loginFbEvent;
    static ChatMsgHandler chatMsgEvent;
    static FriendListHandler FriendListEvent;
    static ChatListHandler chatListEvent;

    public static void setRegisterCallback(MsgHandler registerCallback){
        registerEvent =registerCallback;
    }
    public static void setLoginCallback(MsgHandler loginCallback){
        loginFbEvent=loginCallback;
    }

    public static void setChatMsgCallback(ChatMsgHandler chatMsgCallback){
        chatMsgEvent=chatMsgCallback;
    }

    public static  void setChatListCallback(ChatListHandler chatListCallback) {
        chatListEvent=chatListCallback;
    }
    public static void setFriendListCallback(FriendListHandler FriendListCallback){
        FriendListEvent=FriendListCallback;
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
                User user = frame.getSrc();
                //选择反馈信息的类型
                switch (action.getActionType()){
                    case BuildFrame.Login://登录反馈信息
                        Log.d("OnMessageCome","登录反馈");
                        if(loginFbEvent!=null)//执行登录反馈事件
                            loginFbEvent.handleFbEvent(action.getRslCode(),user.getUserID());
                        break;
                    case BuildFrame.Register://注册反馈信息
                        Log.d("OnMessageCome","注册反馈");
                        if(registerEvent!=null)//执行登录反馈事件
                            registerEvent.handleFbEvent(action.getRslCode(),0);
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
                    int dst[] = new int[frame.getDst().getDstCount()+1];
                    for(int i = 0 ; i < frame.getDst().getDstCount() ; i++){
                        dst[i] = frame.getDst().getDst(i).getUserID();
                    }
                    ITextMsg textMsg = new TextMsgModel();
                    textMsg.setSrc_ID(frame.getSrc().getUserID());
                    textMsg.setDst_ID(dst);
                    textMsg.setMsgDate(Tools.getDate());
                    textMsg.setText(frame.getMsg().getMsg());
                    textMsg.sendOrRecv(true);
//                    IMsg msgRecv = new MsgModel(frame.getSrc().getUserName(),
//                            frame.getDst().getDst(0).getUserName(),
//                            "1000",
//                            frame.getMsg().getMsg(),
//                            true);
                    if(chatMsgEvent!=null)//执行登录反馈事件
                        chatMsgEvent.handleChatMsgEvent(textMsg);
                    if(chatListEvent!=null)
                        chatListEvent.handleChatListEvent(textMsg);
                }else{
                    Log.e(" bad value", "ConService,OnMessageCome ChatMsg");
                    System.out.println("ConService,OnMessageCome ChatMsg bad value");
                }
                break;
            }

            case BuildFrame.GetFriend://好友列表信息
            {
                if(frame.getSrc()==null||frame.getSrc().getUserID()==0||frame.getDst()==null||frame.getDst().getDstCount()<=0){
                    Log.e(" bad value", "ComService,OnMessageCome GetFriend");
                    return;
                }
                Log.d("OnMessage", "feedbackofFriendlist");
                ArrayList<String> list = new ArrayList<String>();
                HashMap<String ,Integer> NameAndID = new HashMap<String , Integer>();
                for (int i = 0; i < frame.getDst().getDstCount(); i++) {
                    list.add(frame.getDst().getDst(i).getUserName());
                    NameAndID.put(frame.getDst().getDst(i).getUserName(),frame.getDst().getDst(i).getUserID());
                }
                IFriend myfriend = new IFriendModel(list,NameAndID);
                FriendListEvent.handleFriendLisEvent(myfriend);
                return;
            }

            default:
                Log.w("OnMessageCome","msgType异常");
                break;
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
