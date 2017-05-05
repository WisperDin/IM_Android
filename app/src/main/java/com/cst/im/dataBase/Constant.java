package com.cst.im.dataBase;

/**
 * Created by wzb on 2017/4/29.
 * 定义数据库字段
 */

public class Constant {
    public static final String DATABASE_NAME = "Info.db";//数据库名
    public static final int DATABASE_VERSION = 2;//数据库版本名，如果修改或添加了本地数据库的表请加一
    public class Chat{
        public static final String TABLE_NAME = "LocalMessage"; //数据库表名
        public static final String LEFT_NAME = "left_name";
        public static final String RIGHT_NAME = "right_name";
        public static final String MSG = "msg";
        public static final String TIME = "time";
        public static final String FLAG = "flag";//用于标志是收到的信息还是发送的信息
    }
    public class Login{
        public static final String TABLE_NAME = "LocalLoginUser";
        public static final String ID = "user_id";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String LOGINTYPE = "login_type";
    }


    //接收状态/发送状态
    public static final String SEND = "send";
    public static final String RECEIVE = "receive";
}

