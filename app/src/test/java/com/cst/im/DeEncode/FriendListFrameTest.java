package com.cst.im.DeEncode;

import com.cst.im.NetWork.proto.BuildFrame;
import com.cst.im.model.IFriend;
import com.cst.im.model.IFriendModel;

import org.junit.Before;
import org.junit.Test;

import protocol.Protocol;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ASUS on 2017/5/13.
 */

public class FriendListFrameTest {
    @Before
    public void setup()throws Exception{

    }

    //正常情况下的获取好友列表
    @Test
    public void TestEncodeGetFriendListFrameSuccess(){
        //初始化帧
        BuildFrame friendFrame = new BuildFrame(BuildFrame.GetFriend);
        //准备帧需要的实体
        IFriend friend = new IFriendModel(1);

        //构建帧
        Protocol.Frame frame = friendFrame.GetFriendList(friend);
        //打印
        System.out.println(frame);
        //assert
        assertEquals(1,frame.getSrc().getUserID());
    }
}
