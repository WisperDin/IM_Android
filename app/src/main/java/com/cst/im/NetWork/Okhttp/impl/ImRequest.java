package com.cst.im.NetWork.Okhttp.impl;

import com.cst.im.FileAccess.FileSweet;

/**
 * Created by admin on 2017/5/7.
 */

public interface ImRequest {

    interface ProccessCallBack{
        /**
         * 进度回调
         * @param cur 已下载字节数
         * @param all 文件字节数
         */
        void onProgress(long cur,long all);
        void fail();
        void success();
    }
    interface ResultCallBack{
        void fail(int code, String msg);
        void success(int code, String msg);
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


    /**
     * 回调一次上传结果，成功或失败
     * @param resultCallback  失败： -1 ，错误信息  成功： 1，返回文件路径
     */
    void downLoadFile(final int type, String name, ResultCallBack resultCallback) ;
    /**
     * 回调上传状态和上传进度
     * @param type 文件类型
     * @param  name 文件名称
     * @param processCallback 进度回调
     */
    void downLoadFile(final int type, final String name, ProccessCallBack processCallback) ;



}