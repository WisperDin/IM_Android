package com.cst.im.DeEncode;

import com.cst.im.NetWork.proto.BuildFrame;
import com.cst.im.model.IUser;
import com.cst.im.model.UserModel;

import org.junit.Before;
import org.junit.Test;

import protocol.Protocol;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ASUS on 2017/5/13.
 */

public class UserInfoRequestFrame {
    @Before
    public void setup()throws Exception{

    }

    //正常情况
    @Test
    public void TestEncodePullUserInfoFrameSuccess(){
        //初始化帧
        BuildFrame pullFrame = new BuildFrame(BuildFrame.PullUserInfo);
        //准备帧需要的实体
        IUser user = new UserModel("abc","",1);
        //构建帧
        Protocol.Frame frame = pullFrame.GetPullUserInfoFrame(user);
        //打印
        System.out.println(frame);
        //assert
        assertEquals(1,frame.getSrc().getUserID());
    }
}
