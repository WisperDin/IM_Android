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

    //接收到消息或发送消息时，消息顶置，若不存在该消息则添加该消息
    @Override
    public void newChatItem(ChatItem chatItem) {
        chatItem.setRead(false);
        int existPos=checkChatItem(chatItem);
        //消息对象已存在检查是否已经置顶
        if(existPos!=-1){
            //检查是否已经置顶
            if(!MsgList.get(existPos).isHasTop()){
                setTop(MsgList.get(existPos));
            }
        }else{
            //消息不存在，添加到列表并置顶
            MsgList.addFirst(chatItem);
        }
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
    //返回非-1表示已存在对象的位置
    @Override
    public int checkChatItem(ChatItem chatItem) {
        int ok=-1;
        for(int i=0;i<MsgList.size();i++)
        {
            //比较id
            if(MsgList.get(i).getID()==chatItem.getID())
            {
                ok=i;
            }
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
