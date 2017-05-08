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
    private int ID;    //唯一标识用户的ID
    private int RedIcon;  //简单红色消息提示

    private boolean hasTop=false;  //消息是否顶置
    private boolean IsRead=false;  //消息是否已读

    //初始化所有数据
    public ChatItem(int icon,String lastTime,String name ,String lastMsg,int redicon)
    {
        Icon=icon;
        LastTime=lastTime;
        //ID=id;
        LastMsg=lastMsg;
        RedIcon=redicon;
        Name = name;
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
    public int getRedIcon() {
        return RedIcon;
    }
    public void setRedIcon(int redIcon) {
        RedIcon = redIcon;
    }
    public void setID(int ID) {this.ID = ID;}
    public int getID() {
        return ID;
    }
    public boolean isRead() {
        return IsRead;
    }
    public void setRead(boolean read) {
        IsRead = read;
    }

    public boolean isHasTop() {
        return hasTop;
    }
    public void setHasTop(boolean hasTop) {
        this.hasTop = hasTop;
    }
}
