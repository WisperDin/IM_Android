package com.cst.im.FileAccess;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by ASUS on 2017/5/5.
 */

public class FileAccess {
    static Context context=null;
    public static void InitContext(Context context){
        FileAccess.context=context;
    }
    //在程序的Cache中创建文件并写入数据，返回这个文件的引用
    public static File WriteFile(String fileName,byte[] fileData){
        if(context==null)
        {
            Log.w("write file","context null");
            System.out.println("context null");
            return null;
        }
        File cacheDir = context.getCacheDir();
        File file = new File(cacheDir, fileName);
        try{
            FileOutputStream fos =new FileOutputStream(file);
            fos.write(fileData);
            fos.close();
        }catch (IOException ioe){
            Log.w("write file",ioe.getMessage());
            System.out.println(ioe.getMessage());
        }
        return file;
    }
}
