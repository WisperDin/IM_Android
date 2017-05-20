package com.cst.im.DeEncode;

import com.cst.im.NetWork.proto.BuildFrame;
import com.cst.im.model.FriendModel;
import com.cst.im.model.IFriend;

import org.junit.Before;
import org.junit.Test;

import protocol.Protocol;

import static junit.framework.Assert.assertEquals;

/**
 * Created by sun on 2017/5/20.
 */
public class AddFriendFrameTest {
    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void encodeAddFriendFrame() throws Exception {
        //构建请求添加好友frame，并输出
        BuildFrame AddfriendFrame = new BuildFrame(BuildFrame.AddFriend);
        IFriend addfriend = new FriendModel(1,2);
        Protocol.Frame frame = AddfriendFrame.IsFriend(addfriend);
        System.out.println(frame);
        assertEquals(1,frame.getSrc().getUserID());
    }

}