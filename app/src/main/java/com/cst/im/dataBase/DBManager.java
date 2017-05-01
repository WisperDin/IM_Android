package com.cst.im.dataBase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.cst.im.model.IMsg;
import com.cst.im.model.IUser;
import com.cst.im.model.MsgModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by wzb on 2017/4/29.
 * 主要是对数据库操作的工具类
 */

public class DBManager {
    private static DatabaseHelper helper;

    public static DatabaseHelper getIntance(Context context){
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
    public static void InsertMsg(MsgModel msg){
        //判断接受/发送状态
        String status;
        if(!msg.getMsgType()){
             status = Constant.SEND;
        }
        else {
             status = Constant.RECEIVE;
        }
        SQLiteDatabase sdb = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(""+Constant.LEFT_NAME+"", msg.getLeft_name());
        values.put(""+Constant.RIGHT_NAME+"",msg.getRight_name());
        values.put(""+Constant.MSG+"", msg.getMessage());
        values.put(""+Constant.TIME+"", msg.getDate());
        values.put(""+Constant.FLAG+"" , status);
        sdb.insert(""+Constant.TABLE_NAME+"", null, values);
        sdb.close();
    }


    /**
     * 查询加载聊天记录
     * 匹配聊天记录的方式为，当且仅当left_name和right_name匹配时，取出聊天记录
     * right_name永远是使用者自身，因此不用查询
     * 将查询结果存在List<IMsg>中并返回
     * */
    public static List<IMsg> QueryMsg(String left_name){
        List<IMsg> mDataArrays = new ArrayList<IMsg>();// 消息对象数组
        SQLiteDatabase sdb = helper.getReadableDatabase();
        //查询获得游标
        Cursor cursor = sdb.query (""+Constant.TABLE_NAME+"",null,null,null,null,null,null);

        //判断游标是否为空
        if(cursor.moveToFirst()) {
        //遍历游标
            while(!cursor.isAfterLast()){
                int l_nameColumnIndex = cursor.getColumnIndex(Constant.LEFT_NAME);
                String l_name = cursor.getString(l_nameColumnIndex);
                int r_nameColumnIndex = cursor.getColumnIndex(Constant.RIGHT_NAME);
                String r_name = cursor.getString(r_nameColumnIndex);
                if(l_name.equals(left_name)  ){
                    int i_status = cursor.getColumnIndex(Constant.FLAG);
                    String i_flag = cursor.getString(i_status);
                    //执行取出聊天记录的操作
                    int i_time = cursor.getColumnIndex(Constant.TIME);
                    String time = cursor.getString(i_time);
                    int i_msg = cursor.getColumnIndex(Constant.MSG);
                    String msg = cursor.getString(i_msg);
                    if(i_flag.equals(Constant.SEND)){
                        //加载发出去的信息
                        IMsg entity = new MsgModel();
                        entity.setDate(time);
                        entity.setMessage(msg);
                        mDataArrays.add(entity);
                        entity.setMsgType(false);// 自己发送的消息
                        cursor.moveToNext();
                    }
                    else{
                        //加载收到的信息
                        IMsg entity = new MsgModel();
                        entity.setDate(time);
                        entity.setMessage(msg);
                        mDataArrays.add(entity);
                        entity.setMsgType(true);// 收到的消息
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
}
