package com.cst.im.model;

import com.cst.im.UI.main.chat.LocationMessageBody;

/**
 * Created by wzb on 2017/5/16.
 */

public interface ILocationMsg extends IBaseMsg {
    LocationMessageBody getLocalBody();
    void setLocalBody(LocationMessageBody localBody);

}
