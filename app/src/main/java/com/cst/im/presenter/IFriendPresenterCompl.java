package com.cst.im.presenter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cst.im.NetWork.ComService;
import com.cst.im.NetWork.proto.DeEnCode;
import com.cst.im.model.IFriend;
import com.cst.im.model.IFriendModel;


import com.cst.im.view.IFriendView;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by sun on 2017/5/4.
 */

public class IFriendPresenterCompl implements IFriendPresenter,ComService.FriendListHandler {

    Handler handler;
    IFriend ifriend;
    IFriendView ifriendview;
    ArrayList<String> friendlist=new ArrayList<String>();

    public IFriendPresenterCompl(IFriendView friendview ) {
        this.ifriendview =  friendview;
        handler = new Handler(Looper.getMainLooper());
        ifriend=new IFriendModel(friendlist);
        //监听收到消息的接口
        ComService.setFriendListCallback(this);
    }

    //发送请求获取好友列表
    @Override
    public void Getfriendlist(String name) {
        IFriend friendget=new IFriendModel(name);
        final byte[] GetFriendFrame = DeEnCode.encodeGetFriendListFrame(friendget);
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    ComService.client.SendData(GetFriendFrame);
                    Log.w("send","send data chenggong");
                }
                catch (IOException ioe)
                {
                    Log.w("send","send data failed");
                }
            }});
    }


    @Override
    public void handleFriendLisEvent(final IFriend msgRecv) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.w("send","Get data success");
                System.out.println(msgRecv.getfriendlist().toString());
                //数据传输-》loginactivity用到
                ifriendview.onRecvMsg(msgRecv.getfriendlist());
            }});

        }
}
