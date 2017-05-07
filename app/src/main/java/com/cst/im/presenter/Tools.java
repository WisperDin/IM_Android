package com.cst.im.presenter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ASUS on 2017/4/23.
 */

public class Tools {
    /**
     * 发送消息时，获取当前事件
     *
     * @return 当前时间
     */
    public static String getDate() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(new Date());
    }
}
