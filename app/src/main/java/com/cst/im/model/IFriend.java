package com.cst.im.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sun on 2017/5/4.
 */

public interface IFriend {
    //用户获取好友列表
    String getname();
    int getId();
    HashMap<String, Integer> getFriendNameAndID();
    ArrayList<String> getfriendlist();



    //判断是否为好友
    int SearchId();
    int ReaultCode();
    void SetRealtCode(int code);



    //添加好友


}
