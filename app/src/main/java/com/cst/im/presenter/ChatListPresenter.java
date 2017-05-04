package com.cst.im.presenter;

import com.cst.im.NetWork.ComService;
import com.cst.im.R;
import com.cst.im.UI.main.msg.ChatItem;
import com.cst.im.UI.main.msg.MsgFragment;
import com.cst.im.model.ChatListModel;
import com.cst.im.model.IChatList;
import com.cst.im.model.IMsg;
import com.cst.im.view.IFragmentView;

import java.util.LinkedList;

/**
 * Created by jijinping on 2017/5/3.
 */

public class ChatListPresenter implements IChatListPresenter,ComService.ChatMsgHandler{

    private IChatList iChatList;
    private IFragmentView iFragmentView;

    public ChatListPresenter(){
        iChatList=new ChatListModel();
        loadChatListLocal();
        iFragmentView=new MsgFragment();
        //监听收到消息的接口
        ComService.setChatMsgCallback(this);
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
        iChatList.addChatItem(new ChatItem(R.drawable.msg_icon,"21:55","小一","小一你好吗？",R.drawable.msg_item_redpoint));
        iChatList.addChatItem(new ChatItem(R.drawable.msg_icon,"21:56","小二","小二你好吗？",R.drawable.msg_item_redpoint));
        iChatList.addChatItem(new ChatItem(R.drawable.msg_icon,"21:57","小三","小三你好吗？",R.drawable.msg_item_redpoint));
        iChatList.addChatItem(new ChatItem(R.drawable.msg_icon,"21:58","小四","小四你好吗？",R.drawable.msg_item_redpoint));
        iChatList.addChatItem(new ChatItem(R.drawable.msg_icon,"21:59","小五","小五你好吗？",R.drawable.msg_item_redpoint));
        iChatList.addChatItem(new ChatItem(R.drawable.msg_icon,"22:00","小六","小六你好吗？",R.drawable.msg_item_redpoint));
    }

    @Override
    public LinkedList<ChatItem> getMsgList() {
        return iChatList.getMsgList();
    }

    @Override
    public void handleChatMsgEvent(IMsg msgRecv) {

    }
    public IChatList getiChatList() {
        return iChatList;
    }

    public void setiChatList(IChatList iChatList) {
        this.iChatList = iChatList;
    }

    public IFragmentView getiFragmentView() {
        return iFragmentView;
    }

    public void setiFragmentView(IFragmentView iFragmentView) {
        this.iFragmentView = iFragmentView;
    }
}
