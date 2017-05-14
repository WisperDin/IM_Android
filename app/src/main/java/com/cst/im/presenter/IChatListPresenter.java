package com.cst.im.presenter;

import com.cst.im.UI.main.msg.ChatItem;
import com.cst.im.model.IBaseMsg;

import java.util.LinkedList;

/**
 * Created by jijinping on 2017/5/3.
 */

public interface IChatListPresenter {
    void setChatListLocal();
    void loadChatListLocal();
    LinkedList<ChatItem> getMsgList();
    void SetTop(ChatItem chatItem);
    void OffsetTop(ChatItem chatItem);
    //void AddChatMsg(IBaseMsg msg);
    void AddChatMsg(int id ,String name , String text);
}
