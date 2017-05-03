package com.cst.im.model;

import com.cst.im.UI.main.msg.ChatItem;

/**
 * Created by jijinping on 2017/5/3.
 */

public interface IChatList {
    void addChatItem(String key,ChatItem chatItem);
    void deleteChatItem(String key);
    ChatItem getChatItem(String key);
    boolean checkChatItem(String key);
}
