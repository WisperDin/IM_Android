package com.cst.im.model;

import com.cst.im.UI.main.msg.ChatItem;

import java.util.HashMap;

/**
 * Created by jijinping on 2017/5/3.
 */

public class ChatListModel implements IChatList {
    //储存消息对象，key:Sender To Accepter
    HashMap<String,ChatItem> MsgList=null;
    public ChatListModel(){
        MsgList=new HashMap<String,ChatItem>();
    }

    //新增一个消息对象
    @Override
    public void addChatItem(String key, ChatItem chatItem) {

    }

    //删除一个消息对象
    @Override
    public void deleteChatItem(String key) {

    }

    //获取指定的消息对象
    @Override
    public ChatItem getChatItem(String key) {
        return null;
    }

    //判断某个消息对象是否存在
    @Override
    public boolean checkChatItem(String key) {
        return false;
    }
}
