package com.cst.im;

import org.junit.Test;

/**
 * Created by ASUS on 2017/5/8.
 */

public class downcast {
    @Test
    public void downCasting(){
        //MsgModelBase baseMsg = new MsgModelBase();
        //TestMsgModel txtMsg = ((TestMsgModel) baseMsg);

        BaseModel b = new BaseModel();
        TestMsgModel t = ((TestMsgModel) b);
    }
    public class TestMsgModel extends BaseModel{

    }
    public class BaseModel{

    }

}
