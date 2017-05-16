package com.cst.im.model;

import com.cst.im.UI.main.msg.ChatItem;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by jijinping on 2017/5/16.
 */
public class ChatListModelTest {
    private IChatList mChatListModel;
    private ChatItem chatItem;
    @Before
    public void setUp() throws Exception {
        mChatListModel=new ChatListModel();
        mChatListModel.addChatItem(new ChatItem(1,"21:55","小一","小一你好吗？",2,1));
        mChatListModel.addChatItem(new ChatItem(1,"21:56","小二","小二你好吗？",2,2));
        mChatListModel.addChatItem(new ChatItem(1,"21:57","小三","小三你好吗？",2,3));
        mChatListModel.addChatItem(new ChatItem(1,"21:58","小四","小四你好吗？",2,4));
        mChatListModel.addChatItem(new ChatItem(1,"21:59","小五","小五你好吗？",2,5));
        mChatListModel.addChatItem(new ChatItem(1,"22:00","小六","小六你好吗？",2,6));
        chatItem=mChatListModel.getChatItem(2);
    }

    @Test
    public void newChatItem() throws Exception {
        mChatListModel.setTop(mChatListModel.getChatItem(3));
        mChatListModel.setTop(mChatListModel.getChatItem(4));
        for(int i=4;i<6;i++)
        {
            ChatItem c=new ChatItem(1,"21:56","小二","小二你好吗？",2,i);
            mChatListModel.newChatItem(c);
            assertSame(c,mChatListModel.getChatItem(0));
        }
//        for(int i=1;i<7;i++)
//        {
//            ChatItem c=new ChatItem(1,"21:56","小二","小二你好吗？",2,i);
//            mChatListModel.newChatItem(c);
//            assertSame(c,mChatListModel.getChatItem(0));
//        }
    }

    @Test
    public void setTop() throws Exception {
        mChatListModel.setTop(mChatListModel.getChatItem(2));
        assertSame(chatItem,mChatListModel.getChatItem(0));
    }

    @Test
    public void offsetTop() throws Exception {
        mChatListModel.setTop(mChatListModel.getChatItem(2));
        mChatListModel.offsetTop(mChatListModel.getChatItem(0));
        assertEquals(chatItem,mChatListModel.getChatItem(2));
    }

}