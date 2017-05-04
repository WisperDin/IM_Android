package com.cst.im.model;

import com.cst.im.UI.main.msg.ChatItem;

import java.util.HashMap;

/**
 * Created by jijinping on 2017/5/3.
 */

public class ChatListModel implements IChatList {
    //储存消息对象，key:AccepterID
    HashMap<String,ChatItem> MsgList=null;
    public ChatListModel(){
        MsgList=new HashMap<String,ChatItem>();
    }

    //新增一个消息对象
    @Override
    public void addChatItem(String key, ChatItem chatItem) {
        MsgList.put(key, chatItem);
    }

    //删除一个消息对象
    @Override
    public void deleteChatItem(String key) {
        MsgList.remove(key);
    }

    //获取指定的消息对象
    @Override
    public ChatItem getChatItem(String key) {
        return MsgList.get(key);
    }

    //判断某个消息对象是否存在
    @Override
    public boolean checkChatItem(String key) {
        boolean ok=false;
        if(MsgList.containsKey(key)){
            ok=true;
        }
        return ok;
    }
}
