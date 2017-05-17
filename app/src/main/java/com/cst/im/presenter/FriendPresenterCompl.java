package com.cst.im.presenter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cst.im.NetWork.ComService;
import com.cst.im.NetWork.proto.DeEnCode;
import com.cst.im.model.FriendModel;
import com.cst.im.model.IFriend;
import com.cst.im.view.IFriendView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by sun on 2017/5/4.
 */

public class FriendPresenterCompl implements IFriendPresenter,ComService.FriendListHandler,ComService.IsFriendHandler ,ComService.AddFriendHandler{

    Handler handler;
    IFriend ifriend;
    IFriend IsFriend;
    IFriendView ifriendview;
    ArrayList<String> friendlist=new ArrayList<String>();
    HashMap<String ,Integer> friendNAndID = new HashMap<String , Integer>();

    public FriendPresenterCompl(IFriendView friendview ) {
        this.ifriendview =  friendview;
        handler = new Handler(Looper.getMainLooper());
        ifriend=new FriendModel(friendlist,friendNAndID);
        //监听收到消息的接口
        ComService.setFriendListCallback(this);
        ComService.setIsfriendCallback(this);
        ComService.setaddfriendCallback(this);
    }

    //发送请求添加好友（模糊查找）
    @Override
    public void AddFriendUncertain(FriendModel.Searchinfo info){
        IFriend AddFriendUncertain=new FriendModel(info);
        final byte[] AddFriendUncertainFrame = DeEnCode.encodeAddFriendUncertainFrame(AddFriendUncertain);
        if( AddFriendUncertainFrame==null){
            Log.w("GetFriendFrame","null");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ComService.client.SendData( AddFriendUncertainFrame);
                    Log.w("send","send data chenggong");
                }
                catch (IOException ioe)
                {
                    Log.w("send","send data failed");
                }
            }}).start();
    }


    //发送请求添加好友（精确id）
    @Override
    public void AddFriend(int ownerid,int friendid){
        IFriend Addfriend=new FriendModel(ownerid,friendid);
        final byte[] AddFriendFrame = DeEnCode.encodeAddFriendFrame(Addfriend);
        if( AddFriendFrame==null){
            Log.w("GetFriendFrame","null");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ComService.client.SendData( AddFriendFrame);
                    Log.w("send","send data chenggong");
                }
                catch (IOException ioe)
                {
                    Log.w("send","send data failed");
                }
            }}).start();
    }

    //发送请求获取好友列表
    @Override
    public void Getfriendlist(int id) {
        IFriend friendget=new FriendModel(id);
        final byte[] GetFriendFrame = DeEnCode.encodeGetFriendListFrame(friendget);
        if( GetFriendFrame==null){
            Log.w("GetFriendFrame","null");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ComService.client.SendData( GetFriendFrame);
                    Log.w("send","send data chenggong");
                }
                catch (IOException ioe)
                {
                    Log.w("send","send data failed");
                }
            }}).start();
    }

    @Override
    public void Isfriend(int ownerid,int IsFriendId) {
        IFriend Isfriend=new FriendModel(ownerid,IsFriendId);
        final byte[] IsFriendFrame = DeEnCode.encodeIsFriendFrame(Isfriend);
        if( IsFriendFrame==null){
            Log.w("GetFriendFrame","null");
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ComService.client.SendData( IsFriendFrame);
                    Log.w("send","send data chenggong");
                }
                catch (IOException ioe)
                {
                    Log.w("send","send data failed");
                }
            }}).start();
    }

    @Override
    public void handleFriendLisEvent(final IFriend msgRecv) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.w("send","Get data success");
                ifriendview.onRecvMsg(msgRecv.getfriendlist(),msgRecv.getFriendNameAndID());
            }});

        }

    @Override
    public void handleIsFriendEvent(final IFriend msgRecv) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.w("send","Get data success");
                ifriendview.onReaultCode(msgRecv.ReaultCode(),msgRecv.getname());
            }});

    }


    @Override
    public void handleAddFriendEvent(final IFriend msgRecv) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.w("send","Get data success");
                ifriendview.onReaultCodebyAddFriend(msgRecv.ReaultCode(),msgRecv.getId(),msgRecv.getname());
            }});

    }
}
