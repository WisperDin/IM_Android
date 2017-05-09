package com.cst.im.NetWork.Okhttp.impl;

import android.os.Environment;
import android.util.Log;

import com.cst.im.FileAccess.FileSweet;
import com.cst.im.NetWork.Okhttp.helper.ProgressHelper;
import com.cst.im.NetWork.Okhttp.listener.ProgressListener;
import com.cst.im.NetWork.Okhttp.progress.ProgressRequestBody;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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


public class FileImRequest implements ImRequest {
    static String url="http://192.168.191.1:8123/file";
    OkHttpClient client=new OkHttpClient();
    static FileImRequest fileImRequest;
    private FileImRequest(){}
    public static FileImRequest Builder(){
        if(fileImRequest ==null)
            fileImRequest =new FileImRequest();
        return fileImRequest;
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
        } catch (IOException e) {
            resultCallback.fail(-2,"io错误！--"+e.toString());
        }catch (IllegalStateException ise){
            Log.w("execute","already call"+ise.getMessage());
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
                processCallback.onProgress(currentBytes,contentLength);
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

    @Override
    public void downLoadFile(final int type, final String name, final ResultCallBack resultCallback) {
        Request request1 = new Request.Builder()
                .url(url+String.format("?name=%s&&type=%d",name,type))
                .get()
                .build();
        //.url(url+"/"+name)
        Call call= client.newCall(request1);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                resultCallback.fail(-1,"网络连接失败，请检查网络！");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[512];
                int len = 0;
                FileOutputStream fos = null;

                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(getFilePath(type),name);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.d("h_bl", "progress=" + progress);
                    }
                    fos.flush();
                    resultCallback.success(1,file.getAbsolutePath());
                } catch (Exception e) {
                    resultCallback.fail(-3,"文件下载失败");
                } finally {
                    try {
                        if (is != null)
                            is.close();
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        resultCallback.success(-2,"文件关闭失败");
                    }
                }

            }
        });
        try {
            call.execute();
        } catch (IOException e) {
            resultCallback.fail(-2,"io错误！--"+e.toString());
        }catch (IllegalStateException ise){
            Log.w("execute","already call"+ise.getMessage());
        }
    }

    @Override
    public void downLoadFile(final int type,final String name, final ProccessCallBack processCallback) {
        Request request1 = new Request.Builder()
                .url(url+String.format("?name=%s&&type=%d",name,type))
                .get()
                .build();
        Call call=  ProgressHelper.addProgressResponseListener(client, new ProgressListener() {
            @Override
            public void onProgress(long currentBytes, long contentLength, boolean done) {
                if(done)processCallback.success();
                Log.i("DOWN1 ","ALL:"+contentLength+" cur;"+currentBytes);
                processCallback.onProgress(currentBytes,contentLength);
            }
        }).newCall(request1);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                processCallback.fail();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[512];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(getFilePath(type),name);
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.d("h_bl", "progress=" + progress);
                    }
                    fos.flush();
                    processCallback.success();
                } catch (Exception e) {
                    processCallback.fail();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        processCallback.fail();
                    }
                }

            }
        });
        try {
            call.execute();
        } catch (IOException e) {
            processCallback.fail();
        }catch (IllegalStateException ise){
            processCallback.fail();
            Log.w("execute","already call"+ise.getMessage());
        }
    }
    private String getFilePath(int MedioType){
        String Root = Environment.getExternalStorageDirectory().getAbsolutePath()+"/IM";
        File file=null;
        switch (MedioType){
            case FileSweet.FILE_TYPE_FILE:
                file =new File(Root+"/files");
                break;
            case FileSweet.FILE_TYPE_MUSIC:
                file =new File(Root+"/musics");
                break;
            case FileSweet.FILE_TYPE_PICTURE:
                file =new File(Root+"/pictures");
                break;
            case FileSweet.FILE_TYPE_VIDEO:
                file =new File(Root+"/videos");
                break;
        }
        if(file!=null && !file.exists())
            file.mkdirs();
        else
            return "";
        return file.getAbsolutePath();
    }
}
