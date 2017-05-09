package com.cst.im.NetWork.Okhttp.impl;

import com.cst.im.FileAccess.FileSweet;
import com.cst.im.NetWork.Okhttp.listener.ProgressListener;
import com.cst.im.NetWork.Okhttp.progress.ProgressRequestBody;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by admin on 2017/5/8.
 */

public class UiImRequest implements ImRequest {
    static String url="http://192.168.1.132:8123";
    OkHttpClient client=new OkHttpClient();
    static UiImRequest uiImRequest;
    private UiImRequest(){}
    public static UiImRequest Builder(){
        if(uiImRequest==null)
            uiImRequest=new UiImRequest();
        return uiImRequest;
    }
    @Override
    public void preRequest(FileSweet fileSweet, final ResultCallBack callback) {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name",fileSweet.getFileName())
                .addFormDataPart("type", ""+fileSweet.getFileType())
                .addFormDataPart("parem", fileSweet.getFileParam())
                .build();
        Request request = new Request.Builder()
                .url(this.url)
                .post(requestBody)
                .build();
        Call call= client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                callback.fail(-1,"网络连接失败，请检查网络！");
            }
            @Override
            public void onResponse(Response response) throws IOException {
                callback.success(response.code(),response.message());
            }
        });
        try {
            call.execute();
        } catch (IOException e) {
            callback.fail(-2,"IO错误！--"+e.toString());
        }
    }


    @Override
    public void upLoadFile(FileSweet fileSweet, final ResultCallBack resultCallback) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("file"), fileSweet.getFile());
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileSweet.getFileName(), fileBody)
                .addFormDataPart("name",fileSweet.getFileName())
                .addFormDataPart("type", ""+fileSweet.getFileType())
                .addFormDataPart("parem", fileSweet.getFileParam())
                .build();
        Request request = new Request.Builder()
                .url(this.url)
                .post(requestBody)
                .build();
        Call call= client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                resultCallback.fail(-1,"网络连接失败，请检查网络！");
            }
            @Override
            public void onResponse(Response response) throws IOException {
                resultCallback.success(response.code(),response.message());
            }
        });
        try {
            call.execute();
        } catch (Exception e) {
            resultCallback.fail(-2,"错误！--"+e.toString());
        }
    }

    @Override
    public void upLoadFile(FileSweet fileSweet, final ProccessCallBack processCallback) {
        RequestBody fileBody = RequestBody.create(MediaType.parse("file"), fileSweet.getFile());
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileSweet.getFileName(), fileBody)
                .addFormDataPart("name",fileSweet.getFileName())
                .addFormDataPart("type", ""+fileSweet.getFileType())
                .addFormDataPart("parem", fileSweet.getFileParam())
                .build();
        ProgressRequestBody progressRequestBody = new ProgressRequestBody(requestBody, new ProgressListener() {
            @Override
            public void onProgress(long currentBytes, long contentLength, boolean done) {
                if(done){
                    processCallback.success();
                    return;
                }
                processCallback.onProgress((double) currentBytes/contentLength);
            }
        });
        Request request = new Request.Builder()
                .url(this.url)
                .post(progressRequestBody)
                .build();
        try {
            client.newCall(request).execute();
        } catch (IOException e) {
            processCallback.fail();
        }
    }
}
