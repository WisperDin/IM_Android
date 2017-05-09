package com.cst.im.model;

import java.io.File;

/**
 * Created by ASUS on 2017/5/4.
 */

public interface IFileMsg extends IBaseMsg {
    //文件
    File getFile();
    void setFile(File file);

    //文件名
    String getFileName();
    void setFileName(String fileName);

    //文件尺寸
    String getFileSize();
    void setFileSize(String fileSize);

    //文件参数
    String getFileParam();
    void setFileParam(String fileParam);

    //文件指纹
    String getFileFeature();
    void setFileFeature(String fileFeature);
}
