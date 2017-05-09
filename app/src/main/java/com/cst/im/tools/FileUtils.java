package com.cst.im.tools;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;

/**
 * Created by ASUS on 2017/5/9.
 */

public class FileUtils {
    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     * @param file 文件
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(File file){
        long blockSize=0;
        try {
            if(!file.exists())
                throw new FileNotFoundException();
            if(file.isDirectory()){
                blockSize = getFileBlockSize(file);
            }else{
                blockSize = getFileBlockSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小","获取失败!");
        }
        return FormetFileSize(blockSize);
    }
    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath){
        return  getAutoFileOrFilesSize(new File(filePath));
    }

    /**
     * 获取指定文件夹占用的空间大小
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFloderBlockSizes(File f) throws Exception
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++){
            if (flist[i].isDirectory()){
                size = size + getFloderBlockSizes(flist[i]);
            }
            else{
                size =size + getFileBlockSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 获取指定文件占用的单元大小
     * @param file
     * @return size
     * @throws Exception
     */
    private static long getFileBlockSize(File file) throws Exception {
        long size = 0;
        if (file.exists()){
            FileInputStream fis = new FileInputStream(file);
            size = fis.available();
        }
        else{
            Log.e("获取文件大小","文件不存在!");
        }
        return size;
    }

    /**
     * 自动转换文件大小
     * @param fileS
     * @return
     */
    private static String FormetFileSize(long fileS)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize="0B";
        if(fileS==0){
            return wrongSize;
        }
        if (fileS < 1024){
            fileSizeString = df.format((double) fileS) + "B";
        }
        else if (fileS < 1048576){
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        }
        else if (fileS < 1073741824){
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        }
        else{
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
}
