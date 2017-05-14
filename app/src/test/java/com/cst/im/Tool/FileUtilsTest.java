package com.cst.im.Tool;

import com.cst.im.tools.FileUtils;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created by ASUS on 2017/5/14.
 */

public class FileUtilsTest {

    //获取文件后缀名测试
    @Test
    public void TestGetFilExName(){
        assertEquals("txt",FileUtils.getFileExName("123.txt"));
    }
}
