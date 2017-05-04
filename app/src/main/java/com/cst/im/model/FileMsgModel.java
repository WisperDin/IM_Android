package com.cst.im.model;

import java.io.File;

/**
 * Created by ASUS on 2017/5/4.
 */

public class FileMsgModel extends MsgModelBase implements IFileMsg {
    File file;
    //文件
    public File getFile(){return file;}
    public void setFile(File file){
        this.file=file;
    }
}
