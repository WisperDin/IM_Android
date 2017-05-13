package com.cst.im.DeEncode;

import com.cst.im.NetWork.proto.BuildFrame;
import com.cst.im.model.ILoginUser;
import com.cst.im.model.LoginUserModel;

import org.junit.Before;
import org.junit.Test;

import protocol.Protocol;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ASUS on 2017/5/13.
 */

public class LoginFrameTest {
    @Before
    public void setup()throws Exception{

    }

    //正常情况下的登录编码
    @Test
    public void TestEncodeLoginFrameSuccess(){
        //初始化帧
        BuildFrame loginFrame = new BuildFrame(BuildFrame.Login);
        //准备帧需要的实体
        ILoginUser loginUser = new LoginUserModel();
        loginUser.setUsername("abc");
        loginUser.setPassword("123");
        //构建帧
        Protocol.Frame frame = loginFrame.GetLoginFrame(loginUser);
        //打印
        System.out.println(frame);
        //assert
        assertEquals("abc",frame.getSrc().getUserName());
        assertEquals("123",frame.getSrc().getUserPwd());
    }
}
