package com.cst.im.model;

import java.util.ArrayList;


/**
 * Created by sun on 2017/5/4.
 */

public class IFriendModel implements IFriend{


    private String ownername;

    private ArrayList <String> friendlist=new ArrayList<String>();

    public IFriendModel(String name){
        this.ownername = name;
    }
    public IFriendModel(ArrayList<String> friendlist){
        this.friendlist = friendlist;
    }

    //用于获取好友列表
    @Override
    public String getname(){
        return ownername;
    }
    @Override
    public ArrayList <String> getfriendlist(){
        return friendlist;
    }

}
