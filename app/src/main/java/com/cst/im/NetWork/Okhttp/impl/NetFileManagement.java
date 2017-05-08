package com.cst.im.NetWork.Okhttp.impl;

import android.app.Activity;
import android.os.Environment;
import android.util.Log;

import com.cst.im.NetWork.Okhttp.helper.ProgressHelper;
import com.cst.im.NetWork.Okhttp.listener.impl.UIProgressListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by admin on 2017/5/7.
 */

public class NetFileManagement {
    private final Activity activity;
    private ImRequest imRequest;
    private static final OkHttpClient client = new OkHttpClient.Builder()
            //设置超时，不设置可能会报异常
            .connectTimeout(1000, TimeUnit.MINUTES)
            .readTimeout(1000, TimeUnit.MINUTES)
            .writeTimeout(1000, TimeUnit.MINUTES)
            .build();
    public NetFileManagement(Activity activity,ImRequest imRequest){
        this.imRequest=imRequest;
        this.activity=activity;
    }
    public NetFileManagement(Activity activity){
        this.activity=activity;
    }
    //获得返回路径 传入网络文件地址
    public String Downlad(String url, final String filename){
        //文件所在路径
        final String SDPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        //这个是ui线程回调，可直接操作UI
        final UIProgressListener uiProgressResponseListener = new UIProgressListener() {
            @Override
            public void onUIProgress(long bytesRead, long contentLength, boolean done) {

            }

            @Override
            public void onUIStart(long bytesRead, long contentLength, boolean done) {
                super.onUIStart(bytesRead, contentLength, done);
            }

            @Override
            public void onUIFinish(long bytesRead, long contentLength, boolean done) {
                super.onUIFinish(bytesRead, contentLength, done);
            }
        };
        final Request request1 = new Request.Builder()
                .url(url)
                .build();
        //包装Response使其支持进度回调
        ProgressHelper.addProgressResponseListener(client, uiProgressResponseListener).newCall(request1).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("TAG", "error ", e);
            }

            @Override
            public void onResponse(Response response) throws IOException {
//                    String bodystr = response.body().string();
//                    Log.e("TAG","response.body:"+bodystr);
                //以上两行代码祸害了我两天  因为response的数据已经读出来并且释放掉了 我要是再次掉用的话就会发生错误
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;

                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(SDPath, filename);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.d("h_bl", "progress=" + progress);
                    }
                    fos.flush();
                    Log.d("h_bl", "文件下载成功");
                     file.getAbsolutePath();
                } catch (Exception e) {
                    Log.d("h_bl", "文件下载失败");
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }
        });
        return SDPath+filename;
    }
    //传入文件绝对路径 网络文件存储地址  上传到指定路由 回调
    public void UpLoad(String filepath, String url, final ImRequest imRequest){
        File file = new File(filepath);
        final FileSweet fileSweet=new FileSweet(1,filepath);
        //构造上传请求，类似web表单
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("fileType","1")//文件类型
                .addFormDataPart("param", "626428 240 32")//具体文件参数
                .addFormDataPart("file", file.getName(), RequestBody.create(null, file))//文件内容
                .build();
        //这个是ui线程回调，可直接操作UI
        final UIProgressListener uiProgressRequestListener = new UIProgressListener() {
            @Override
            public void onUIProgress(long bytesWrite, long contentLength, boolean done) {
                Log.e("TAG", "bytesWrite:" + bytesWrite);
                Log.e("TAG", "contentLength" + contentLength);
                Log.e("TAG", (100 * bytesWrite) / contentLength + " % done ");
                Log.e("TAG", "done:" + done);
                Log.e("TAG", "================================");
                imRequest.upLoadFile(fileSweet,true,(100 * bytesWrite) / contentLength);
                //ui层回调
            }

            @Override
            public void onUIStart(long bytesWrite, long contentLength, boolean done) {
                super.onUIStart(bytesWrite, contentLength, done);
                imRequest.upLoadFile(fileSweet,true,(100 * bytesWrite) / contentLength);
            }

            @Override
            public void onUIFinish(long bytesWrite, long contentLength, boolean done) {
                super.onUIFinish(bytesWrite, contentLength, done);
                imRequest.upLoadFile(fileSweet,true,(100 * bytesWrite) / contentLength);
            }
        };
        //进行包装，使其支持进度回调
        final Request request = new Request.Builder().url(url)
                .post(ProgressHelper.addProgressRequestListener(requestBody,uiProgressRequestListener)).build();
        Callback callback=new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.e("TAG", "error ", e);
                imRequest.upLoadFile(fileSweet,this);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Log.e("TAG","response.body: ");
                imRequest.upLoadFile(fileSweet,this);
            }
        };
        client.newCall(request).enqueue(callback);
    }


}
