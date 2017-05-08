package com.cst.im.NetWork.Okhttp.impl;

import android.os.Handler;


/**
 * Created by admin on 2017/5/8.
 */

public abstract class UiImRequest implements ImRequest {
    private boolean isFirst = false;

    //处理UI层的Handler子类
    private static class UIHandler extends MProgressHandler {
        public UIHandler(UiImRequest uiImRequest) {
            super(uiImRequest);
        }

        @Override
        public void start(UiImRequest uiImRequest, long currentBytes, long contentLength, boolean done) {
            if (uiImRequest!=null) {
                uiImRequest.onUIStart(currentBytes, contentLength, done);
            }
        }

        @Override
        public void progress(UiImRequest uiImRequest, long currentBytes, long contentLength, boolean done) {
            if (uiImRequest!=null){
                uiImRequest.onUIProgress(currentBytes, contentLength, done);
            }
        }

        @Override
        public void finish(UiImRequest uiImRequest, long currentBytes, long contentLength, boolean done) {
            if (uiImRequest!=null){
                uiImRequest.onUIFinish(currentBytes, contentLength,done);
            }
        }

    }
    //主线程Handler
    private final Handler mHandler = new UIHandler(this);


    /**
     * UI层回调抽象方法
     *
     * @param currentBytes    当前的字节长度
     * @param contentLength 总字节长度
     * @param done          是否写入完成
     */
    public abstract void onUIProgress(long currentBytes, long contentLength, boolean done);

    /**
     * UI层开始请求回调方法
     * @param currentBytes 当前的字节长度
     * @param contentLength 总字节长度
     * @param done 是否写入完成
     */
    public void onUIStart(long currentBytes, long contentLength, boolean done) {

    }

    /**
     * UI层结束请求回调方法
     * @param currentBytes 当前的字节长度
     * @param contentLength 总字节长度
     * @param done 是否写入完成
     */
    public void onUIFinish(long currentBytes, long contentLength, boolean done) {

    }
}
