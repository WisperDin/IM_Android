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

public class IsFriendFrameTest {
    @Before
    public void setup()throws Exception{

    }

    //正常情况
    @Test
    public void TestEncodeIsFriendFrameSuccess(){
        //初始化帧
        BuildFrame isFriendFrame = new BuildFrame(BuildFrame.IsFriend);
        //准备帧需要的实体
        IFriend friend = new IFriendModel(1,1);
        //构建帧
        Protocol.Frame frame = isFriendFrame.IsFriend(friend);
        //打印
        System.out.println(frame);
        //assert
        assertEquals(1,frame.getSrc().getUserID());
        assertEquals(1,frame.getDst().getDst(0).getUserID());
    }
}
