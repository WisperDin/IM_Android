package com.cst.im.view;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sun on 2017/5/4.
 */

public interface IFriendView {
    void onRecvMsg(ArrayList<String> list,HashMap<String ,Integer> NameAndID);
    void onReaultCode(int code,String name);
}
