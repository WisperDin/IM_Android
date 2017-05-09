package com.cst.im.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by wzb on 2017/4/27.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    /**
     * 每次点进聊天窗口加载本地聊天信息，根据获取到的用户name，在数据库进行匹配初始化
     * right_name表示本机用户的姓名
     * left_name 表示与本机用户聊天的姓名
     * */

    public static final String CREATE_MESSAGE = "create table "+Constant.Chat.TABLE_NAME+" ("
            + "id integer primary key autoincrement, "
            + ""+Constant.Chat.FLAG+" text,"
            + ""+Constant.Chat.LEFT_ID+" integer, "
            + ""+Constant.Chat.RIGHT_ID+" integer,"
            + ""+Constant.Chat.MSG+" text, "
            + ""+Constant.Chat.TIME+" text，)";
    /**
     * 登录表
     * 用户信息包括id，用户名，密码
     */
    public static final String CREATE_LOGININF = String.format("CREATE TABLE IF NOT EXISTS %s " +
            "(%s INT32 ," +
            " %s VARCHAR(30), " +
            " %s VARCHAR(30))",
            Constant.Login.TABLE_NAME,
            Constant.Login.USERNAME,
            Constant.Login.PASSWORD,
            Constant.Login.ID);
    /**
     * 用户信息表
     * 保存用户的基本信息包括
     * id，用户头像的本地保存位置，用户名，性别，真实姓名，手机号码，邮箱，地址，个性签名
     */
    public static final String CREATE_USERINFO = String.format("CREATE TABLE IF NOT EXISTS %s(" +
            "%s INT32 NOT NULL," +
            "%s VARCHAR(128)," +
            "%s VARCHAR(30)," +
            "%s VARCHAR(2) ," +
            "%s VARCHAR(30)," +
            "%s VARCHAR(20)," +
            "%s VARCHAR(30)," +
            "%s VARCHAR(256)," +
            "%s VARCHAR(128))",
            Constant.UserInfo.TABLE_NAME,
            Constant.UserInfo.ID,
            Constant.UserInfo.USER_PICTURE,
            Constant.UserInfo.USER_NAME,
            Constant.UserInfo.USER_SEX,
            Constant.UserInfo.USER_REAL_NAME,
            Constant.UserInfo.USER_PHONE,
            Constant.UserInfo.USER_EMAIL,
            Constant.UserInfo.USER_ADDRESS,
            Constant.UserInfo.USER_SIGN);


    public static final String CREATE_MSGLISTINF = String.format("CREATE TABLE IF NOT EXISTS %s " +
                    "(%s INT32 primary key," +
                    " %s INT32,"+
                    " %s VARCHAR(30))  ",
            Constant.MsgList.TABLE_NAME,
            Constant.MsgList.MSGID,//消息按照msgid排序
            Constant.MsgList.DSTID,//消息目的用户(组)
            Constant.MsgList.MSG);//最新一条消息
    /**
     * @param context  上下文环境（例如，一个 Activity）
     * @param name   数据库名字
     * @param factory  一个可选的游标工厂（通常是 Null）
     * @param version  数据库模型版本的整数
     * 会调用父类 SQLiteOpenHelper的构造函数
     */
    private Context mContext;
    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    //重写
    public DatabaseHelper(Context context){
        super(context , Constant.DATABASE_NAME , null , Constant.DATABASE_VERSION);
    }

    /**
     *  在数据库第一次创建的时候会调用这个方法
     *根据需要对传入的SQLiteDatabase 对象填充表和初始化数据。
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("Database","onCreate");
        db.execSQL(CREATE_MESSAGE);
        db.execSQL(CREATE_LOGININF);
        db.execSQL(CREATE_USERINFO);
        db.execSQL(CREATE_MSGLISTINF);
    }

    /**
     * 当数据库需要修改的时候（两个数据库版本不同），Android系统会主动的调用这个方法。
     * 一般我们在这个方法里边删除数据库表，并建立新的数据库表.
     */

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //三个参数，一个 SQLiteDatabase 对象，一个旧的版本号和一个新的版本号
        if(newVersion>oldVersion)
        {
            String sql1 = "DROP TABLE IF EXISTS " + Constant.Login.TABLE_NAME;
            db.execSQL(sql1);
            String sql2 = "DROP TABLE IF EXISTS " + Constant.Chat.TABLE_NAME;
            db.execSQL(sql2);
            String sql3 = "DROP TABLE IF EXISTS " + Constant.UserInfo.TABLE_NAME;
            db.execSQL(sql3);
            String sql4 = " drop table if exists " + Constant.MsgList.TABLE_NAME;
            db.execSQL(sql4);
            this.onCreate(db);
        }
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        // 每次成功打开数据库后首先被执行
        super.onOpen(db);
    }
}