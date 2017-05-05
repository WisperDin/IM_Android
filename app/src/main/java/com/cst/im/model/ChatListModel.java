package com.cst.im.model;

import com.cst.im.UI.main.msg.ChatItem;

import java.util.HashMap;
import java.util.LinkedList;

/**
 * Created by jijinping on 2017/5/3.
 */

public class ChatListModel implements IChatList {
    //储存消息对象
    private LinkedList<ChatItem> MsgList=null;
    //存储消息顶置之前的位置
    private HashMap<ChatItem,Integer> preAddress;


    public ChatListModel(){
        MsgList=new LinkedList<ChatItem>();
        preAddress= new HashMap<ChatItem,Integer>();
    }
    public ChatListModel(LinkedList<ChatItem> msglist){
        MsgList=new LinkedList<ChatItem>();
        MsgList=msglist;
        preAddress= new HashMap<ChatItem,Integer>();
    }

    @Override
    public void newChatItem(ChatItem chatItem) {

    }

    //新增一个消息对象
    @Override
    public void addChatItem(ChatItem chatItem) {
        MsgList.add(chatItem);
    }

    //删除一个消息对象
    @Override
    public void deleteChatItem(int key) {
        MsgList.remove(key);
    }

    //获取指定的消息对象
    @Override
    public ChatItem getChatItem(int key) {
        return MsgList.get(key);
    }

    @Override
    public LinkedList<ChatItem> getMsgList() {
        return MsgList;
    }

    //判断某个消息对象是否存在
    @Override
    public boolean checkChatItem(ChatItem chatItem) {
        boolean ok=false;
        if(MsgList.contains(chatItem)){
            ok=true;
        }
        return ok;
    }

    @Override
    public void setTop(ChatItem chatItem) {
        preAddress.put(chatItem,MsgList.indexOf(chatItem));
        MsgList.remove(MsgList.indexOf(chatItem));
        MsgList.addFirst(chatItem);
    }

    @Override
    public void offsetTop(ChatItem chatItem) {
        Integer offset=preAddress.get(chatItem);
        MsgList.remove(MsgList.indexOf(chatItem));
        MsgList.add(offset,chatItem);
    }
}
