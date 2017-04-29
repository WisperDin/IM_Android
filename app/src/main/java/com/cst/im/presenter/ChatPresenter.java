package com.cst.im.presenter;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Looper;

import com.cst.im.dataBase.DBManager;
import com.cst.im.dataBase.DatabaseHelper;
import com.cst.im.model.IMsg;
import com.cst.im.model.IUser;
import com.cst.im.model.MsgModel;
import com.cst.im.model.UserModel;
import com.cst.im.view.IChatView;

import java.sql.SQLData;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ASUS on 2017/4/23.
 */

public class ChatPresenter implements IChatPresenter{
    private List<IMsg> mDataArrays = new ArrayList<IMsg>();// 消息对象数组
    private IChatView iChatView;
    private  Handler handler;
    private SQLiteOpenHelper helper;//从数据库获取历史消息
    private IUser localUser;//假设这个是登录这个客户端的用户
    public ChatPresenter(Activity activity) {
        this.iChatView = (IChatView) activity;
        handler = new Handler(Looper.getMainLooper());
        localUser=new UserModel("lzy","123");
        helper = DBManager.getIntance(activity);
        //以下两个方法作用一致，都提供了打开可供读写的数据库，若数据库不存在，则新建
        //helper.getReadableDatabase();
        SQLiteDatabase db = helper.getWritableDatabase();
    }



    //历史记录
    private String[] msgArray = new String[] { "有大吗", "有！你呢？", "我也有", "那上吧",
            "打啊！你放大啊！", "你TM咋不放大呢？留大抢人头啊？CAO！你个菜B", "2B不解释", "尼滚...",
            "今晚去网吧包夜吧？", "有毛片吗？", "种子一大堆啊~还怕没片？", "OK,搞起！！" };

    private String[] dataArray = new String[] { "2012-09-22 18:00:02",
            "2012-09-22 18:10:22", "2012-09-22 18:11:24",
            "2012-09-22 18:20:23", "2012-09-22 18:30:31",
            "2012-09-22 18:35:37", "2012-09-22 18:40:13",
            "2012-09-22 18:50:26", "2012-09-22 18:52:57",
            "2012-09-22 18:55:11", "2012-09-22 18:56:45",
            "2012-09-22 18:57:33", };
    private final static int COUNT = 12;// 初始化数组总数
    //加载历史消息
    @Override
    public List<IMsg>  LoadHisMsg(){
        for (int i = 0; i < COUNT; i++) {
            IMsg entity = new MsgModel();
            entity.setDate(dataArray[i]);
            if (i % 2 == 0) {
                entity.setName("肖B");
                entity.setMsgType(true);// 收到的消息
            } else {
                entity.setName("必败");
                entity.setMsgType(false);// 自己发送的消息
            }
            entity.setMessage(msgArray[i]);
            mDataArrays.add(entity);
        }
        return mDataArrays;
    }

    @Override
    public void SendMsg(String contString){
        if (contString.length() > 0) {
            IMsg entity = new MsgModel();
            entity.setName(localUser.getName());
            entity.setDate(Tools.getDate());
            entity.setMessage(contString);
            entity.setMsgType(false);
            mDataArrays.add(entity);

            handler.post(new Runnable() {
                @Override
                public void run() {
                    iChatView.onSendMsg();
                }});

        }

    }
}
