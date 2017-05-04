package com.cst.im.presenter;

import com.cst.im.NetWork.ComService;
import com.cst.im.UI.main.msg.MsgFragment;
import com.cst.im.model.ChatListModel;
import com.cst.im.model.IChatList;
import com.cst.im.model.IMsg;
import com.cst.im.view.IFragmentView;

/**
 * Created by jijinping on 2017/5/3.
 */

public class ChatListPresenter implements IChatListPresenter,ComService.ChatMsgHandler{

    private IChatList iChatList;
    private IFragmentView iFragmentView;

    public ChatListPresenter(){
        iChatList=new ChatListModel();
        iFragmentView=new MsgFragment();
        //监听收到消息的接口
        ComService.setChatMsgCallback(this);
    }
    //保存消息列表到本地
    @Override
    public void setChatListLocal() {

    }

    @Override
    public void handleChatMsgEvent(IMsg msgRecv) {

    }
}
