package com.cst.im.model;

import com.cst.im.UI.main.chat.LocationMessageBody;

/**
 * Created by wzb on 2017/5/16.
 */

public class LocationMsgModel extends MsgModelBase implements ILocationMsg {
    LocationMessageBody locationMessageBody;
    public LocationMessageBody getLocalBody(){
        return locationMessageBody;
    }
    public void setLocalBody(LocationMessageBody locationMessageBody){
        this.locationMessageBody = locationMessageBody;
    }
}
