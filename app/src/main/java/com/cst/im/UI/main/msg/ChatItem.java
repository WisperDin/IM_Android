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
    private String ID;    //唯一标识用户的ID

    public int getRedIcon() {
        return RedIcon;
    }

    public void setRedIcon(int redIcon) {
        RedIcon = redIcon;
    }

    private int RedIcon;
    public boolean IsRead=false;  //消息是否已读
    public String getID() {
        return ID;
    }
    public boolean isRead() {
        return IsRead;
    }
    public void setRead(boolean read) {
        IsRead = read;
    }
    public void setID(String ID) {
        this.ID = ID;
    }

    //初始化所有数据
    public ChatItem(int icon,String lastTime,String name,String lastMsg,int redicon)
    {
        Icon=icon;
        LastTime=lastTime;
        Name=name;
        LastMsg=lastMsg;
        RedIcon=redicon;
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
