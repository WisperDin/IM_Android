package com.cst.im.UI.main.msg;

/**
 * Created by jijinping on 2017/4/24.
 */
//消息列表的每一行
public class ChatItem {
    private int Icon;   //图标
    private String LastTime;   //发送最后一条消息的时间
    private String Name;    //好友昵称
    private String LastMsg;   //用于显示最新的一条消息
    //初始化所有数据
    public ChatItem(int icon,String lastTime,String name,String lastMsg)
    {
        Icon=icon;
        LastTime=lastTime;
        Name=name;
        LastMsg=lastMsg;
    }
    public int getIcon() {
        return Icon;
    }
    public void setIcon(int icon) {
        Icon = icon;
    }
    public String getLastTime() {
        return LastTime;
    }
    public void setLastTime(String lastTime) {
        LastTime = lastTime;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getLastMsg() {
        return LastMsg;
    }
    public void setLastMsg(String lastMsg) {
        LastMsg = lastMsg;
    }
}
