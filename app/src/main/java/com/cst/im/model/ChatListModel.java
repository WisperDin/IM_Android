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

    //接收到消息或发送消息时，消息置前，若不存在该消息则添加该消息并置前（置前不同于顶置）
    @Override
    public void newChatItem(ChatItem chatItem) {
        chatItem.setRead(false);
        int existPos=checkChatItem(chatItem);
        int count=0;    //记录已有多少个顶置
        //消息对象已存在检查是否已经置顶
        if(existPos!=-1){
            //检查是否已经设置为置顶，若设置为置顶，则直接置顶，若否则置前
            if(MsgList.get(existPos).isHasTop()){
                MsgList.remove(existPos);
                MsgList.add(existPos,chatItem);
                setTop(chatItem);
            }else
            {
                while(MsgList.get(count).isHasTop())
                {
                    count++;
                }
                MsgList.remove(existPos);
                MsgList.add(count,chatItem);
            }
        }else{
            while(MsgList.get(count).isHasTop())
            {
                count++;
            }
            //消息不存在，添加到列表并置前
            MsgList.add(count,chatItem);
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

    //顶置消息，用于长按时的置顶，不可用于其他
    @Override
    public void setTop(ChatItem chatItem) {
        preAddress.put(chatItem,MsgList.indexOf(chatItem));
        MsgList.remove(MsgList.indexOf(chatItem));
        chatItem.setHasTop(true);
        MsgList.addFirst(chatItem);
    }

    //取消消息顶置，用于长按时的取消置顶，不可用于其他
    @Override
    public void offsetTop(ChatItem chatItem) {
        Integer offset=preAddress.get(chatItem);
        MsgList.remove(MsgList.indexOf(chatItem));
        chatItem.setHasTop(false);
        MsgList.add(offset,chatItem);
    }
}
