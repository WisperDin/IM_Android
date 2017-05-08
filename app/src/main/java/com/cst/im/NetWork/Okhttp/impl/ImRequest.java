package com.cst.im.NetWork.Okhttp.impl;

import com.cst.im.FileAccess.FileSweet;

/**
 * Created by admin on 2017/5/7.
 */

public interface ImRequest {
    interface ProccessCallBack{
        /**
         * @param percent 进度百分比
         */
        void onProgress(double percent);
        void fail();
        void success();
    }
    interface ResultCallBack{
        void fail(int code,String msg);
        void success(int code,String msg);
    }
    /**
     *  在文件上传前做预备请求，返回结果在callback中
     *  callback包含标志信息：1.允许上传 2.文件已经存在 3.不允许上传
     * @param fileSweet
     * @param callback
     */
    void preRequest(FileSweet fileSweet, ResultCallBack callback);
    /**
     * 回调一次上传结果，成功或失败
     * @param resultCallback
     */
    void upLoadFile(FileSweet fileSweet, ResultCallBack resultCallback) ;
    /**
     * 回调上传状态和上传进度
     * @param fileSweet
     * @param processCallback
     */
    void upLoadFile(FileSweet fileSweet, ProccessCallBack processCallback) ;
}
