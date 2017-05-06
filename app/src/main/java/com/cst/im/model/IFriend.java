package com.cst.im.model;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by sun on 2017/5/4.
 */

public interface IFriend {
    String getname();
    int getId();
    HashMap<String, Integer> getFriendNameAndID();
    ArrayList<String> getfriendlist();
}
