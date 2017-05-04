package com.cst.im.presenter;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.cst.im.NetWork.ComService;
import com.cst.im.NetWork.proto.DeEnCode;
import com.cst.im.dataBase.DBManager;
import com.cst.im.model.IMsg;
import com.cst.im.model.IUser;
import com.cst.im.model.MsgModel;
import com.cst.im.model.UserModel;
import com.cst.im.view.IChatView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2017/4/23.
 */

public class ChatPresenter implements IChatPresenter,ComService.ChatMsgHandler{
    private List<IMsg> mDataArrays = new ArrayList<IMsg>();// 消息对象数组
    private IChatView iChatView;
    private  Handler handler;
    private IUser localUser;//假设这个是登录这个客户端的用户
    public ChatPresenter(IChatView chatView , List<IMsg> msg) {
        this.iChatView =  chatView;
        this.mDataArrays = msg;
        handler = new Handler(Looper.getMainLooper());
        localUser=new UserModel("lzy","123");
        //监听收到消息的接口
        ComService.setChatMsgCallback(this);
    }



    //历史记录
//    private String[] msgArray = new String[] { "有大吗", "有！你呢？", "我也有", "那上吧",
//            "打啊！你放大啊！", "你TM咋不放大呢？留大抢人头啊？CAO！你个菜B", "2B不解释", "尼滚...",
//            "今晚去网吧包夜吧？", "有毛片吗？", "种子一大堆啊~还怕没片？", "OK,搞起！！" };
//
//    private String[] dataArray = new String[] { "2012-09-22 18:00:02",
//            "2012-09-22 18:10:22", "2012-09-22 18:11:24",
//            "2012-09-22 18:20:23", "2012-09-22 18:30:31",
//            "2012-09-22 18:35:37", "2012-09-22 18:40:13",
//            "2012-09-22 18:50:26", "2012-09-22 18:52:57",
//            "2012-09-22 18:55:11", "2012-09-22 18:56:45",
//            "2012-09-22 18:57:33", };
//    private final static int COUNT = 12;// 初始化数组总数
//    //加载历史消息
//    @Override
//    public List<IMsg>  LoadHisMsg(){
//        for (int i = 0; i < COUNT; i++) {
//            IMsg entity = new MsgModel();
//            entity.setDate(dataArray[i]);
//            if (i % 2 == 0) {
//                entity.setName("肖B");
//                entity.setMsgType(true);// 收到的消息
//            } else {
//                entity.setName("必败");
//                entity.setMsgType(false);// 自己发送的消息
//            }
//            entity.setMessage(msgArray[i]);
//            mDataArrays.add(entity);
//        }
//        return mDataArrays;
//    }
    //接受到新的消息 //参数为接收到的消息
    @Override
    public void handleChatMsgEvent(final IMsg msgRecv){
        //TODO: 做一个判断，判断这条信息的确是发给当前这个聊天窗口的对象的
        mDataArrays.add(msgRecv);
        handler.post(new Runnable() {
            @Override
            public void run() {
                iChatView.onRecvMsg(msgRecv.getMessage(), msgRecv.getDate());
            }});

    }

    @Override
    public void SendMsg(String contString){
        if (contString.length() > 0) {
            IMsg entity = new MsgModel();
            entity.setRight_name(localUser.getName());
            entity.setDate(Tools.getDate());
            entity.setMessage(contString);
            entity.setMsgType(false);

            entity.setLeft_name("abc");
            //发送数据到服务器
            //编码聊天消息帧
            final byte[] chatMsgFrame = DeEnCode.encodeChatMsgFrame(entity);
            //调用发送数据接口
            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        ComService.client.SendData(chatMsgFrame);
                    }
                    catch (IOException ioe)
                    {
                        Log.w("send","send data failed");
                    }
                }});
            mDataArrays.add(entity);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    iChatView.onSendMsg();
                }});
        }

    }
}
