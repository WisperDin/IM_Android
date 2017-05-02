package com.cst.im.presenter;

/**
 * Created by PolluxLee on 2017/4/25.
 */

public final class Status {

    public static final class Login{
        public static final int USERNAME_INVALID = 0; // 用户名无效
        public static final int USERNAME_PHONE = 1; // 用户名为手机号
        public static final int USERNAME_EMAIL = 2; // 用户名为邮箱
        public static final int USERNAME_ACCOUNT = 3; // 用户名为普通账号
    }

    public static final class Register{
        public static final int USERNAME_INVALID = 0; // 用户名无效
        public static final int USERNAME_PHONE = 1; // 用户名为手机号
        public static final int USERNAME_EMAIL = 2; // 用户名为邮箱
        public static final int USERNAME_ACCOUNT = 3; // 用户名为普通账号

        public static final int REGISTER_SUCCESS = 4; // 注册成功
        public static final int REGISTER_FAIL = 5; // 注册失败
    }
}
