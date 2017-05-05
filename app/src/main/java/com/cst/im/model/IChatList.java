package com.cst.im.model;

import com.cst.im.UI.main.msg.ChatItem;

import java.util.LinkedList;

/**
 * Created by jijinping on 2017/5/3.
 */

public interface IChatList {
    void addChatItem(ChatItem chatItem);
    void deleteChatItem(int key);
    ChatItem getChatItem(int key);
    LinkedList<ChatItem> getMsgList();
    boolean checkChatItem(ChatItem chatItem);
    void setTop(ChatItem chatItem);
    void offsetTop(ChatItem chatItem);
    void newChatItem(ChatItem chatItem);
}
