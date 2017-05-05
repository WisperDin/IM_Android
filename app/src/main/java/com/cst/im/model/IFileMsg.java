package com.cst.im.model;

import java.io.File;

/**
 * Created by ASUS on 2017/5/4.
 */

public interface IFileMsg extends IBaseMsg {
    //文件
    File getFile();
    void setFile(File file);
}
