package com.cst.im.DeEncode;

import com.cst.im.NetWork.proto.BuildFrame;
import com.cst.im.model.IMsg;
import com.cst.im.model.MsgModel;
import com.cst.im.presenter.Tools;

import org.junit.Before;
import org.junit.Test;

import protocol.Protocol;

/**
 * Created by ASUS on 2017/5/1.
 */

public class BuildFrameTest {


    @Before
    public void setup()throws Exception{

    }
    @Test
    public void encodeChatMsgFrameSuccessTest()throws Exception{
        //初始化帧
        BuildFrame chatFrame = new BuildFrame(BuildFrame.ChatMsg);
        //准备帧需要的实体
        IMsg entity = new MsgModel();
        entity.setRight_name("lzy");
        entity.setDate(Tools.getDate());
        entity.setMessage("testMsg");
        entity.setMsgType(false);

        entity.setLeft_name("abc");
        //构建帧

        Protocol.Frame frame = chatFrame.GetChatMsgFrame(entity);

        System.out.println(frame.getProtoSign());
        System.out.println(frame.getMsgLength());
        System.out.println(frame.getMsgType());
        System.out.println(frame.getSenderTime());
        System.out.println(frame.getSrc().getUserName());
        System.out.println(frame.getDst().getDst(0).getUserName());
        System.out.println(frame.getMsg());
    }

    //错误样例
    @Test
    public void encodeChatMsgFrameFailedTest()throws Exception{
        //初始化帧
        BuildFrame chatFrame = new BuildFrame(BuildFrame.ChatMsg);
        //准备帧需要的实体
        IMsg entity = new MsgModel();
        //entity.setRight_name("lzy");
        entity.setDate(Tools.getDate());
        entity.setMessage("testMsg");
        entity.setMsgType(false);

        entity.setLeft_name("abc");
        //构建帧

        Protocol.Frame frame = chatFrame.GetChatMsgFrame(entity);

        System.out.println(frame.getProtoSign());
        System.out.println(frame.getMsgLength());
        System.out.println(frame.getMsgType());
        System.out.println(frame.getSenderTime());
        System.out.println(frame.getSrc().getUserName());
        System.out.println(frame.getDst().getDst(0).getUserName());
        System.out.println(frame.getMsg());
    }
}
