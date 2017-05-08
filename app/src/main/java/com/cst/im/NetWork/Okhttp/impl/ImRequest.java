package com.cst.im.NetWork.Okhttp.impl;

import okhttp3.Callback;

/**
 * Created by admin on 2017/5/7.
 */

public interface ImRequest {

    interface ProccessCallBack{
        /**
         * @param state 是否上传中
         * @param percent 进度百分比
         */
        void onProgress(boolean state, long percent);
    }
    /**
     *  在文件上传前做预备请求，返回结果在callback中
     *  callback包含标志信息：1.允许上传 2.文件已经存在 3.不允许上传
     * @param fileSweet
     * @param callback
     */
    void preRequest(FileSweet fileSweet, Callback callback);
    /**
     * 回调一次上传结果，成功或失败
     * @param resultCallback
     */
    void upLoadFile(FileSweet fileSweet, Callback resultCallback) ;

    /**
     * 回调上传状态和上传进度
     * @param fileSweet
     * @param processCallback
     */
    void upLoadFile(FileSweet fileSweet, ProccessCallBack processCallback) ;
    void upLoadFile(FileSweet fileSweet, boolean b, long percent) ;
}
