package com.cst.im.dataBase;

/**
 * Created by wzb on 2017/4/29.
 * 定义数据库字段
 */

public class Constant {
    public static final String DATABASE_NAME = "Info.db";//数据库名
    public static final int DATABASE_VERSION = 4;//数据库版本名，如果修改或添加了本地数据库的表请加一
    /*
    数据库版本3:修改了用户信息的表保存用户的简单个人信息
     */
    public class Chat{
        public static final String TABLE_NAME = "LocalMessage"; //数据库表名
        public static final String LEFT_ID = "left_id";
        public static final String RIGHT_ID = "right_id";
        public static final String MSG = "msg";
        public static final String TIME = "time";
        public static final String FLAG = "flag";//用于标志是收到的信息还是发送的信息
    }
    public class Login{
        public static final String TABLE_NAME = "LocalLoginUser";
        public static final String ID = "user_id";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
        public static final String LOGIN_TYPE = "login_type";
    }
    public class UserInfo{
        public static final String TABLE_NAME = "LocalUserInfo";
        public static final String ID = "user_id";                  // 用户ID，查询用户唯一凭证
        public static final String USER_PICTURE = "user_picture";    //用户头像,保存一个本地地址
        public static final String USER_NAME = "username";
        public static final String USER_SEX = "user_sex";
        public static final String USER_AGE = "user_age";
        public static final String USER_REAL_NAME = "user_real_name"; //用户真实姓名,附带实名认证功能
        public static final String USER_PHONE = "user_phone";        // 用户手机
        public static final String USER_EMAIL = "user_email";
        public static final String USER_ADDRESS = "user_address";  // 用户地址
        public static final String USER_SIGN = "user_sign";          // 用户个性签名
    }
    //如果加入群聊之后可以将id变成组的id,消息按照msg_id排序
    public class MsgList{
        public static final String TABLE_NAME = "LocalMsgList";
        public static final String DSTID = "dst_id";
        public static final String MSGID = "msg_id";
        public static final String MSG = "msg";
    }


    //接收状态/发送状态
    public static final String SEND = "send";
    public static final String RECEIVE = "receive";
}

