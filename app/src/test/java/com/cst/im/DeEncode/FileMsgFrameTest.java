package com.cst.im.DeEncode;

import com.cst.im.NetWork.proto.BuildFrame;
import com.cst.im.model.FileMsgModel;
import com.cst.im.model.IBaseMsg;
import com.cst.im.model.IFileMsg;
import com.cst.im.presenter.Tools;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import protocol.Protocol;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ASUS on 2017/5/13.
 */

public class FileMsgFrameTest {
    @Before
    public void setup()throws Exception{

    }
    @Test
    public void encodeFileMsgFrameSuccessTest()throws Exception{
        //初始化帧
        BuildFrame fileInfoFrame = new BuildFrame(BuildFrame.FileInfo);
        //准备帧需要的实体
        IFileMsg fileInfoMsg = new FileMsgModel();
        fileInfoMsg.setSrc_ID(1);
        fileInfoMsg.setMsgDate(Tools.getDate());
        fileInfoMsg.sendOrRecv(false);
        fileInfoMsg.setDst_ID(new int[]{1});
        fileInfoMsg.setMsgType(IBaseMsg.MsgType.FILE);
        fileInfoMsg.setFile(new File(""));
        fileInfoMsg.setFileName("abc");
        fileInfoMsg.setFileSize("123MB");
        fileInfoMsg.setFileParam("a b r");
        fileInfoMsg.setFileFeature("23s445");
        //构建帧
        Protocol.Frame frame = fileInfoFrame.GetFileInfoFrame(fileInfoMsg);
        //打印
        System.out.println(frame);

        //assert
        assertEquals(1,frame.getSrc().getUserID());
        assertEquals(1,frame.getDst().getDst(0).getUserID());
        //...
    }
}
