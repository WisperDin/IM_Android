package com.cst.im.NetWork.Okhttp.impl;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.cst.im.NetWork.Okhttp.listener.impl.model.ProgressModel;

import java.lang.ref.WeakReference;


/**
 * User:lizhangqu(513163535@qq.com)
 * Date:2015-10-02
 * Time: 15:25
 */
public abstract class MProgressHandler extends Handler {
    public static final int UPDATE = 0x01;
    public static final int START = 0x02;
    public static final int FINISH = 0x03;
    //弱引用
    private final WeakReference<UiImRequest> MuiImRequest;

    public MProgressHandler(UiImRequest uiImRequest) {
        super(Looper.getMainLooper());
        MuiImRequest = new WeakReference<UiImRequest>(uiImRequest);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case UPDATE: {
                UiImRequest uiProgessListener = MuiImRequest.get();
                if (uiProgessListener != null) {
                    //获得进度实体类
                    ProgressModel progressModel = (ProgressModel) msg.obj;
                    //回调抽象方法
                    progress(uiProgessListener, progressModel.getCurrentBytes(), progressModel.getContentLength(), progressModel.isDone());
                }
                break;
            }
            case START: {
                UiImRequest UiImRequest = MuiImRequest.get();
                if (UiImRequest != null) {
                    //获得进度实体类
                    ProgressModel progressModel = (ProgressModel) msg.obj;
                    //回调抽象方法
                    start(UiImRequest, progressModel.getCurrentBytes(), progressModel.getContentLength(), progressModel.isDone());

                }
                break;
            }
            case FINISH: {
                UiImRequest UiImRequest = MuiImRequest.get();
                if (UiImRequest != null) {
                    //获得进度实体类
                    ProgressModel progressModel = (ProgressModel) msg.obj;
                    //回调抽象方法
                    finish(UiImRequest, progressModel.getCurrentBytes(), progressModel.getContentLength(), progressModel.isDone());
                }
                break;
            }
            default:
                super.handleMessage(msg);
                break;
        }
    }

    public abstract void start(UiImRequest uiImRequest,long currentBytes, long contentLength, boolean done);
    public abstract void progress(UiImRequest uiImRequest,long currentBytes, long contentLength, boolean done);
    public abstract void finish(UiImRequest uiImRequest,long currentBytes, long contentLength, boolean done);

}
