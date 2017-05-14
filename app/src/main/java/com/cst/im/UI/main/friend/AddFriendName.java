package com.cst.im.UI.main.friend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.cst.im.UI.main.friend.SortByPinYin.getPinYinFirstLetter;


/**
 * Created by sun on 2017/4/30.
 */

public class AddFriendName {
    public String[] getTittle() {
        return tittle;
    }

    private String[] tittle = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    public Map<String, ArrayList<String>> getFriendname() {
        return friendname;
    }

    private Map<String,ArrayList<String>> friendname=new HashMap<String,ArrayList<String>>();
    private char upperletter;
    private String key;


     AddFriendName(){

        for(int i=0;i<tittle.length;i++){
            ArrayList<String> temp;
            temp=new ArrayList<String>();
            friendname.put(tittle[i],temp);
        }
    }

    public void SortAndAdd(String name) {
        ArrayList<String> data;
        data=new ArrayList<String>();
        upperletter=Character.toUpperCase(getPinYinFirstLetter(name).charAt(0));
        key=String.valueOf(upperletter);
        data=friendname.get(key);
        data.add(name);
        friendname.remove(key);
        friendname.put(key,data);
    }
}
