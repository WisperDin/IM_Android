package com.cst.im.model;

import java.io.File;

/**
 * Created by ASUS on 2017/5/4.
 */

public class FileMsgModel extends MsgModelBase implements IFileMsg {
    File file;
    String fileSize;
    String fileParam;
    String fileFeathre;
    //文件
    public File getFile(){return file;}
    public void setFile(File file){
        this.file=file;
    }

    @Override
    public String getFileName() {
        return file.getName();
    }

    //文件尺寸
    public String getFileSize(){return fileSize;}
    public void setFileSize(String fileSize){this.fileSize=fileSize;}

    @Override
    public String getFileParam() {
        return fileParam;
    }

    @Override
    public void setFileParam(String fileParam) {
        this.fileParam = fileParam;
    }

    @Override
    public String getFileFeature() {
        return fileFeathre;
    }

    @Override
    public void setFileFeature(String fileFeature) {
        this.fileFeathre = fileFeature;
    }
}
