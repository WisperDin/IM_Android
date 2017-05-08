package com.cst.im.NetWork.Okhttp;

import android.app.Activity;
import android.util.Log;

import com.cst.im.NetWork.Okhttp.impl.FileSweet;
import com.cst.im.NetWork.Okhttp.impl.ImRequest;
import com.cst.im.NetWork.Okhttp.impl.NetFileManagement;

import okhttp3.Callback;

/**
 * Created by ASUS on 2017/5/8.
 */

public class FileTranslate {
    private Activity activity;
    private NetFileManagement netFileManagement;
    private String fileServeUrl="http://192.168.1.100:8123";
    public FileTranslate(Activity activity){
        this.activity = activity;
        netFileManagement = new NetFileManagement(activity);
    }
    public void UploadFile(String filepath){
        netFileManagement.UpLoad(filepath, fileServeUrl, new ImRequest() {
            @Override
            public void preRequest(FileSweet fileSweet, Callback callback) {
                Log.e("TAG",fileSweet.getFileName());
            }

            @Override
            public void upLoadFile(FileSweet fileSweet, Callback resultCallback) {

                Log.e("TAG",fileSweet.getFileName());
            }

            @Override
            public void upLoadFile(FileSweet fileSweet, ProccessCallBack processCallback) {

            }

            @Override
            public void upLoadFile(FileSweet fileSweet, boolean b, long percent) {

            }
        });
    }
}
