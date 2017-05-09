package com.cst.im.FileAccess;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.cst.im.tools.FileUtils;
import com.cst.im.tools.Md5Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidParameterException;

/**
 *  文件甜点
 *  by cjwddz-jia 2017/5/8
 */

public class FileSweet {

    /**
     * 文件固有类型
     */
    final static public int FILE_TYPE_PICTURE=1;
    final static public int FILE_TYPE_MUSIC=2;
    final static public int FILE_TYPE_VIDEO=3;
    final static public int FILE_TYPE_FILE=4;
    /**
     * 文件名
     */
    String fileName;
    /**
     * 文件参数
     */
    String fileParam;
    /**
     * 文件类型
     */
    int fileType;
    /**
     * 文件后缀
     */
    String filePostfix;
    /**
     * 文件指纹
     */
    String feature;

    public String getFileName() {
        return fileName;
    }

    public String getFileParam() {
        return fileParam;
    }

    public int getFileType() {
        return fileType;
    }

    public String getFilePostfix() {
        return filePostfix;
    }

    public String getFeature() {
        return feature;
    }

    public File getFile() {
        return file;
    }

    /**
     * 文件

     */
    File file;
    /**
     * 生成一个file对象，
     * @param fileType{@link FILE_TYPE_PICTURE,FILE_TYPE_MUSIC,FILE_TYPE_VIDEO,FILE_TYPE_FILE}.
     * @param filePath
     * @throws InvalidParameterException
     */
    public FileSweet(int fileType, String filePath)  throws FileNotFoundException{
        if(filePath==null || fileType<1 || fileType>4 ||!(new File(filePath).exists()))
            throw new FileNotFoundException("文件类型不符，或找不到文件");
        this.file=new File(filePath);
        try {
            initParam(fileType,this.file);
        } catch (IOException e) {
            throw new FileNotFoundException(e.toString());
        }
    }
    public FileSweet(int fileType, File file)  throws FileNotFoundException{
        if(file==null || fileType<1 || fileType>4 ||!file.exists())
            throw new FileNotFoundException("文件类型不符，或找不到文件");
        this.file=file;
        try {
            initParam(fileType,file);
        } catch (IOException e) {
            throw new FileNotFoundException(e.toString());
        }
    }

    void initParam(int fileType, File file) throws IOException {
        this.fileType=fileType;
        this.fileName =  file.getName();
        this.fileParam= FileUtils.getAutoFileOrFilesSize(file);
        filePostfix=fileName.substring(fileName.lastIndexOf(".")+1);
        feature= Md5Utils.getMd5(getDataCharacteristic(file));
        StringBuffer sb=new StringBuffer((filePostfix!=null && !filePostfix.isEmpty())?(filePostfix+" "):"");
        sb.append(file.length()+" ");
        switch (fileType) {
            case FILE_TYPE_PICTURE:
                Bitmap bm;
                try {
                    bm= BitmapFactory.decodeStream(new FileInputStream(file));
                    sb.append(bm.getWidth()+" "+bm.getHeight());
                } catch (IOException e) {
                    throw new FileNotFoundException(e.toString());
                }
                break;
            case FILE_TYPE_MUSIC:
                sb.append(this.fileName);
                break;
            case FILE_TYPE_VIDEO:
                sb.append(this.fileName);
                break;
            case FILE_TYPE_FILE:
                sb.append(this.fileName);
                break;
        }
    }

    /**
     * 获取文件特征
     * @param file
     * @return
     */
    byte[] getDataCharacteristic(File file) throws IOException {
        FileInputStream fin=new FileInputStream(file);
        int length=fin.available();
        byte[] start=new byte[32],con=new byte[32],end=new byte[32],all=new byte[96];
        if(length<100){
            fin.read(start);
            return start;
        }
        fin.skip(length/2-16);
        fin.read(con);
        fin.skip(fin.available()-32);
        fin.read(end);
        System.arraycopy(start,0,all,0,32);
        System.arraycopy(con,0,all,32,32);
        System.arraycopy(end,0,all,64,32);
        return all;
    }


}
