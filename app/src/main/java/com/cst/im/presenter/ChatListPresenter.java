package com.cst.im.presenter;

import android.os.Handler;
import android.os.Looper;

import com.cst.im.NetWork.ComService;
import com.cst.im.R;
import com.cst.im.UI.main.msg.ChatItem;
import com.cst.im.model.ChatListModel;
import com.cst.im.model.IChatList;
import com.cst.im.model.IMsg;
import com.cst.im.view.IMsgView;

import java.util.LinkedList;

/**
 * Created by jijinping on 2017/5/3.
 */

public class ChatListPresenter implements IChatListPresenter,ComService.ChatListHandler{
    //静态对象 消息列表
    private static IChatList iChatList;
    private IMsgView iMsgView;
    private Handler handler = new Handler(Looper.getMainLooper());
    public ChatListPresenter(IMsgView iMsgView){
        this.iMsgView=iMsgView;
        if(iChatList==null){
            iChatList = new ChatListModel();
        }
        //监听收到消息的接口
        ComService.setChatListCallback(this);
    }

    @Override
    public void SetTop(ChatItem chatItem) {
        iChatList.setTop(chatItem);
    }

    @Override
    public void OffsetTop(ChatItem chatItem) {
        iChatList.offsetTop(chatItem);
    }

    //保存消息列表到本地
    @Override
    public void setChatListLocal() {

    }

    //从本地读取消息列表
    @Override
    public void loadChatListLocal() {
/*        iChatList.addChatItem(new ChatItem(R.drawable.msg_icon,"21:55","小一","小一你好吗？",R.drawable.msg_item_redpoint));
        iChatList.addChatItem(new ChatItem(R.drawable.msg_icon,"21:56","小二","小二你好吗？",R.drawable.msg_item_redpoint));
        iChatList.addChatItem(new ChatItem(R.drawable.msg_icon,"21:57","小三","小三你好吗？",R.drawable.msg_item_redpoint));
        iChatList.addChatItem(new ChatItem(R.drawable.msg_icon,"21:58","小四","小四你好吗？",R.drawable.msg_item_redpoint));
        iChatList.addChatItem(new ChatItem(R.drawable.msg_icon,"21:59","小五","小五你好吗？",R.drawable.msg_item_redpoint));
        iChatList.addChatItem(new ChatItem(R.drawable.msg_icon,"22:00","小六","小六你好吗？",R.drawable.msg_item_redpoint));
    */
    }

    @Override
    public LinkedList<ChatItem> getMsgList() {
        return iChatList.getMsgList();
    }

    //接收到服务端转发消息
    @Override
    public void handleChatListEvent(IMsg msgRecv) {
        AddChatMsg(msgRecv.getLeft_name(),msgRecv.getMessage());
    }

    //添加一条消息的接口
    @Override
    public void AddChatMsg(final String userName,final  String message) {
        //构造消息
        ChatItem chatItem=new ChatItem(R.drawable.msg_icon, Tools.getDate().substring(11,16),userName,message,R.drawable.msg_item_redpoint);
        //进行该消息的处理，若列表中不存在则添加，并顶置列表中
        ChatListPresenter.iChatList.newChatItem(chatItem);
        handler.post(new Runnable() {
            @Override
            public void run() {
                iMsgView.onRefreshMsgList();
            }});
    }
    public IChatList getiChatList() {
        return iChatList;
    }

    public void setiChatList(IChatList iChatList) {
        this.iChatList = iChatList;
    }



}
