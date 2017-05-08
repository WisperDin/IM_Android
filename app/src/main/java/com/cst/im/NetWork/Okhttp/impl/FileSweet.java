package com.cst.im.NetWork.Okhttp.impl;

import java.io.File;
import java.security.InvalidParameterException;

/**
 * Created by admin on 2017/5/7.
 */

public class FileSweet {
    /**
     * 文件类型
     */
    static public int FILE_TYPE_PICTURE=1;
    static public int FILE_TYPE_MUSIC=2;
    static public int FILE_TYPE_VIDEO=3;
    static public int FILE_TYPE_FILE=4;
    /**
     * 文件名
     */
    String fileName;
    /**
     * 文件参数
     */
    String fileParam;
    /**
     * 文件指纹
     */
    String feature;

    /**
     * 生成一个file对象，
     * @param fileType{@link FILE_TYPE_PICTURE,FILE_TYPE_MUSIC,FILE_TYPE_VIDEO,FILE_TYPE_FILE}.
     * @param filePath
     * @throws InvalidParameterException
     */
    public FileSweet(int fileType, String filePath) throws InvalidParameterException {
        // TODO: 2017/5/7 先判断文件是否合法否则抛出异常，初始化成员

    }
    public FileSweet(int fileType, File file) throws InvalidParameterException{

    }

    /**
     * 获取文件指纹
     * 根据文件类型和文件生成一个文件指纹
     * @param fileType
     * @param file
     * @return
     */
    private String getFeature(int fileType,File file){

        //TODO: 2017/5/7 该方法内部调用，switch 根据文件类型选择不同生成方法
        return null;
    }

    public String getFileName() {
        return fileName;
    }
    public String getFileParam() {
        return fileParam;
    }
    public String getFeature() {
        return feature;
    }
}
