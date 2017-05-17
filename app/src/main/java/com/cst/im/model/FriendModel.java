package com.cst.im.model;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by sun on 2017/5/4.
 */

public class FriendModel implements IFriend{


    private String ownername;

    private int id;

    private int searchId;

    public int getResultcode() {
        return resultcode;
    }

    public void setResultcode(int resultcode) {
        this.resultcode = resultcode;
    }

    private int resultcode;
    public static class Searchinfo {
        public  String keyword;
        public int age;
        public String sex;
        public String email;
        public String telephone;
        public String address;
    }



    private Searchinfo info=new Searchinfo();


    //好友姓名列表
    private ArrayList <String> friendlist=new ArrayList<String>();

    //好友ID，名字的map
    private HashMap<String ,Integer> friendNameAndID = new HashMap<String , Integer>();

    public static FriendModel friendModel=new FriendModel();
    public static void InitFriendModel(ArrayList<String> friendlist,HashMap<String ,Integer> NameAndID){
        friendModel =new FriendModel(friendlist,NameAndID);
    }


    public FriendModel(int ownerid, int friendid){
        this.id=ownerid;
        this.searchId=friendid;
    }
    public FriendModel(int ownerid, String name){
        this.id=ownerid;
        this.ownername=name;
    }
    public FriendModel(String name){
        this.ownername = name;
    }
    public FriendModel(int userid){
        this.id = userid;
    }
    public FriendModel(ArrayList<String> friendlist, HashMap<String ,Integer> NameAndID){
        this.friendlist = friendlist;
        this.friendNameAndID=NameAndID;
    }
    public FriendModel(){};
    public FriendModel(Searchinfo info){
        this.info=info;
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


    //用于判断是否为好友
    @Override
    public int SearchId(){
        return searchId;
    }
    @Override
    public int ReaultCode(){
        return resultcode;
    }
    @Override
    public void SetRealtCode(int code){
        this.resultcode=code;
    }


    //用于添加好友
    @Override
    public Searchinfo getinfo() {
        return info;
    }

}
