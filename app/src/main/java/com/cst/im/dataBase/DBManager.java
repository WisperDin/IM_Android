package com.cst.im.dataBase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.cst.im.model.IBaseMsg;
import com.cst.im.model.ILoginUser;
import com.cst.im.model.ITextMsg;
import com.cst.im.model.LoginUserModel;
import com.cst.im.model.TextMsgModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wzb on 2017/4/29.
 * 主要是对数据库操作的工具类
 */

public class DBManager {
    private static DatabaseHelper helper;

    public static DatabaseHelper getIntance(Context context){
        Log.d("DBManager","getIntance");
        if(helper == null){
            helper = new DatabaseHelper(context);
        }
        return helper;
    }

    /**
     * 向数据库插入聊天信息
     * 通过MsgModel获取聊天对象用户名，聊天内容以及时间
     * localUert表示用户自身
     * status 表示发送或者接收的状态，在Constant类中有SEND和RECEIVE两个常量表示两种状态
     * */
    public static void InsertMsg(IBaseMsg msg){
        //判断接受/发送状态
        switch(msg.getMsgType()){
            case TEXT:
                String status;
                if(!msg.sendOrRecv()){
                    status = Constant.SEND;
                }
                else {
                    status = Constant.RECEIVE;
                }
                ITextMsg textMsg = ((ITextMsg) msg);
                SQLiteDatabase sdb = helper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(""+Constant.Chat.LEFT_ID+"", textMsg.getDst_IDAt(0));
                values.put(""+Constant.Chat.RIGHT_ID+"",textMsg.getSrc_ID());
                values.put(""+Constant.Chat.MSG+"", textMsg.getText());
                values.put(""+Constant.Chat.TIME+"", textMsg.getMsgDate());
                values.put(""+Constant.Chat.FLAG+"" , status);
                sdb.insert(""+Constant.Chat.TABLE_NAME+"", null, values);
                sdb.close();
                break;
            case FILE:
                Log.d("存储文件" ,"文件————————————");
                return;
            case PHOTO:
                Log.d("存储图片" ,"图片————————————");
                return;
            case SOUNDS:
                Log.d("存储语音" ,"语音————————————");
                return;
         }
    }


    /**
     * 查询加载聊天记录
     * 匹配聊天记录的方式为，当且仅当left_name和right_name匹配时，取出聊天记录
     * right_name永远是使用者自身，因此不用查询
     * 将查询结果存在List<IMsg>中并返回
     * */
    public static List<IBaseMsg> QueryMsg(int dst_ID){
        List<IBaseMsg> mDataArrays = new ArrayList<IBaseMsg>();// 消息对象数组
        SQLiteDatabase sdb = helper.getReadableDatabase();
        //查询获得游标
        Cursor cursor = sdb.query (""+Constant.Chat.TABLE_NAME+"",null,null,null,null,null,null);

        //判断游标是否为空
        if(cursor.moveToFirst()) {
        //遍历游标
            while(!cursor.isAfterLast()){
                int leftID = cursor.getColumnIndex(Constant.Chat.LEFT_ID);
                int l_ID = cursor.getInt(leftID);
                if(dst_ID == l_ID){
                    int i_status = cursor.getColumnIndex(Constant.Chat.FLAG);
                    String i_flag = cursor.getString(i_status);
                    //执行取出聊天记录的操作
                    int i_time = cursor.getColumnIndex(Constant.Chat.TIME);
                    String time = cursor.getString(i_time);
                    int i_msg = cursor.getColumnIndex(Constant.Chat.MSG);
                    String msg = cursor.getString(i_msg);
                    if(i_flag.equals(Constant.SEND)){
                        //加载发出去的信息
                        /**************先以文本信息为例******************/
                        ITextMsg entity = new TextMsgModel();
                        entity.setMsgDate(time);
                        entity.setText(msg);
                        mDataArrays.add(entity);
                        entity.sendOrRecv(false);// 自己发送的消息
                        cursor.moveToNext();
                    }
                    else{
                        //加载收到的信息
                        ITextMsg entity = new TextMsgModel();
                        entity.setMsgDate(time);
                        entity.setText(msg);
                        mDataArrays.add(entity);
                        entity.sendOrRecv(true);// 收到的消息
                        cursor.moveToNext();
                    }

                }
                else cursor.moveToNext();
            }
        }
        cursor.close();
        sdb.close();
        return mDataArrays;
    }
    /*
    保存用户登陆信息到本地
     */
    public static void saveLoginUser(ILoginUser loginUser){
        int id = loginUser.getId();
        String username = loginUser.getUsername();
        String password = loginUser.getPassword();
        SQLiteDatabase sdb = helper.getReadableDatabase();
        String sql = String.format("INSERT INTO %s (%s,%s,%s)VALUES(%s,'%s','%s')",Constant.Login.TABLE_NAME,
                Constant.Login.ID,Constant.Login.USERNAME,Constant.Login.PASSWORD,
                id,username,password);

        sdb.execSQL(sql);
        sdb.close();
    }


    //下次打开软件检查是否已经登陆过，若有，直接跳过登录界面
    public static ILoginUser queryLoginUser(){
        ILoginUser loginUserModel = new LoginUserModel();
        int id = 0;
        String username = "";
        String password = "";
        SQLiteDatabase sdb = helper.getReadableDatabase();
        Cursor cursor = sdb.rawQuery("SELECT * FROM " + Constant.Login.TABLE_NAME,null);

        if(cursor.moveToFirst()){
            id = cursor.getInt(cursor.getColumnIndex(Constant.Login.ID));
            username = cursor.getString(cursor.getColumnIndex(Constant.Login.USERNAME));
            password = cursor.getString(cursor.getColumnIndex(Constant.Login.PASSWORD));

        }
        loginUserModel.setId(id);
        loginUserModel.setUsername(username);
        loginUserModel.setPassword(password);
        cursor.close();
        sdb.close();
        return loginUserModel;
    }

    public static void deleteLoginUser(String username){
        SQLiteDatabase sdb = helper.getWritableDatabase();

        String sql = String.format("DELETE  FROM %s WHERE %s = '%s'",Constant.Login.TABLE_NAME,Constant.Login.USERNAME,username);
        //"DELETE * FROM " + Constant.Login.TABLE_NAME + " WHERE "+ Constant.Login.USERNAME  + " = "+ username;
        sdb.execSQL(sql);
    }

}
