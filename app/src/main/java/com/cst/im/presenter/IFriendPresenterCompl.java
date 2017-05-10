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
import java.util.HashMap;


/**
 * Created by sun on 2017/5/4.
 */

public class IFriendPresenterCompl implements IFriendPresenter,ComService.FriendListHandler,ComService.IsFriendHandler {

    Handler handler;
    IFriend ifriend;
    IFriend IsFriend;
    IFriendView ifriendview;
    ArrayList<String> friendlist=new ArrayList<String>();
    HashMap<String ,Integer> friendNAndID = new HashMap<String , Integer>();

    public IFriendPresenterCompl(IFriendView friendview ) {
        this.ifriendview =  friendview;
        handler = new Handler(Looper.getMainLooper());
        ifriend=new IFriendModel(friendlist,friendNAndID);
        //监听收到消息的接口
        ComService.setFriendListCallback(this);
        ComService.setIsfriendCallback(this);
    }

    //发送请求获取好友列表
    @Override
    public void Getfriendlist(int id) {
        IFriend friendget=new IFriendModel(id);
        final byte[] GetFriendFrame = DeEnCode.encodeGetFriendListFrame(friendget);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //TODO 先避免一下粘包问题，，，
                    Thread.sleep(100);
                    ComService.client.SendData(GetFriendFrame);
                    Log.w("send","send data chenggong");
                }
                catch (IOException ioe)
                {
                    ioe.printStackTrace();
                    Log.w("send","send data failed");
                }catch (InterruptedException ie){
                    ie.printStackTrace();
                    Log.w("send","delay send data failed");
                }
                catch (NullPointerException nil){
                    Log.w("send","null client");
                }
            }}).start();
    }

    //发送请求获取好友列表
    @Override
    public void Isfriend(int ownerid,int IsFriendId) {
        IFriend Isfriend=new IFriendModel(ownerid,IsFriendId);
        final byte[] GetFriendFrame = DeEnCode.encodeIsFriendFrame(Isfriend);
        if(GetFriendFrame==null){
            Log.w("GetFriendFrame","null");
            return;
        }
        new Thread(new Runnable() {
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
            }}).start();
    }

    @Override
    public void handleFriendLisEvent(final IFriend msgRecv) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.w("send","Get data success");
                System.out.println(msgRecv.getfriendlist().toString());
                //数据传输-》loginactivity用到
                ifriendview.onRecvMsg(msgRecv.getfriendlist(),msgRecv.getFriendNameAndID());
            }});

        }

    @Override
    public void handleIsFriendEvent(final IFriend msgRecv) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Log.w("send","Get data success");
                System.out.println(msgRecv.getfriendlist().toString());
                //数据传输-》loginactivity用到
                ifriendview.onReaultCode(msgRecv.ReaultCode(),msgRecv.getname());
            }});

    }
}
