package com.cst.im.UI.main.chat;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.cst.im.R;
import com.cst.im.model.FileMsgModel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wzb on 2017/5/11.
 */

public class ShowNormalFileActivity extends Activity{
    private ProgressBar progressBar;
    private File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_file);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        //final FileMessageBody messageBody = getIntent().getParcelableExtra("msgbody");
        final FileMsgModel fileMsg = (FileMsgModel) getIntent().getSerializableExtra("msgbody");
        file = new File(fileMsg.getFileUrl());
        //set head map
        final Map<String, String> maps = new HashMap<String, String>();
        if (!TextUtils.isEmpty(fileMsg.getFileFeature())) {
            maps.put("share-secret", fileMsg.getFileFeature());
        }
        //下载文件
//        new Thread(new Runnable() {
//            public void run() {
//                HttpFileManager fileManager = new HttpFileManager(ShowNormalFileActivity.this, EMChatConfig.getInstance().getStorageUrl());
//                fileManager.downloadFile(messageBody.getRemoteUrl(), messageBody.getLocalUrl(), maps,
//                        new CloudOperationCallback() {
//
//                            @Override
//                            public void onSuccess(String result) {
//                                runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        DealFileTypeUtils.openFile(file, ShowNormalFileActivity.this);
//                                        finish();
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onProgress(final int progress) {
//                                runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        progressBar.setProgress(progress);
//                                    }
//                                });
//                            }
//
//                            @Override
//                            public void onError(final String msg) {
//                                runOnUiThread(new Runnable() {
//                                    public void run() {
//                                        if(file != null && file.exists()&&file.isFile())
//                                            file.delete();
//                                        Toast.makeText(ShowNormalFileActivity.this, "下载文件失败: "+msg, Toast.LENGTH_SHORT).show();
//                                        finish();
//                                    }
//                                });
//                            }
//                        });
//
//            }
//        }).start();

    }
}
