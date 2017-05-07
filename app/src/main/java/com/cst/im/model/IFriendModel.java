package com.cst.im.model;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by sun on 2017/5/4.
 */

public class IFriendModel implements IFriend{


    private String ownername;

    private int id;

    //好友姓名列表
    private ArrayList <String> friendlist=new ArrayList<String>();

    //好友ID，名字的map
    private HashMap<String ,Integer> friendNameAndID = new HashMap<String , Integer>();

    public static  IFriendModel iFriendModel;
    public static void InitFriendModel(ArrayList<String> friendlist,HashMap<String ,Integer> NameAndID){
        iFriendModel=new  IFriendModel(friendlist,NameAndID);
    }

    public IFriendModel(String name){
        this.ownername = name;
    }
    public IFriendModel(int userid){
        this.id = userid;
    }
    public IFriendModel(ArrayList<String> friendlist,HashMap<String ,Integer> NameAndID){
        this.friendlist = friendlist;
        this.friendNameAndID=NameAndID;
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
    @Override
    public int getId() {return id;}
    @Override
    public HashMap<String, Integer> getFriendNameAndID() {
        return friendNameAndID;
    }

}
