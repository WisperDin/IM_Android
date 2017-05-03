package com.cst.im.presenter;

import com.cst.im.model.IChatList;
import com.cst.im.view.IFragmentView;

/**
 * Created by jijinping on 2017/5/3.
 */

public class ChatListPresenter implements IChatListPresenter{

    private IChatList iChatList;
    private IFragmentView iFragmentView;

    public ChatListPresenter(){

    }
    //保存消息列表到本地
    @Override
    public void setChatListLocal() {

    }
}
