package com.cst.im.UI;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.cst.im.NetWork.ComService;
import com.cst.im.R;
import com.cst.im.UI.main.MainActivity;
import com.cst.im.dataBase.DBManager;
import com.cst.im.dataBase.DatabaseHelper;
import com.cst.im.model.ILoginUser;
import com.cst.im.presenter.ILoginPresenter;

import static android.Manifest.permission.CAMERA;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class SplashActivity extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    ILoginPresenter loginPresenter;
    int flagJumpToLogin = 0;
    int flagJumpToMain = 1;
    Handler myhandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            if(msg.what == flagJumpToLogin){
                jumpToLogin();
            }
            else if(msg.what == flagJumpToMain){
                jumpToMain();
            }
        }
    };


    ////////////////////////////////////////////////////////////////////////////////获取权限
    // 要申请的权限
    private String[] permissions = {READ_EXTERNAL_STORAGE,WRITE_EXTERNAL_STORAGE,RECORD_AUDIO,CAMERA};
    private AlertDialog dialog;
    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {
        new AlertDialog.Builder(this)
                .setTitle("读写存储，访问摄像机权限不可用")
                .setMessage("由于IM需要读写存储空间，获取您需要发送的文件，或者使用摄像机拍摄照片发送；\n否则，您将无法正常使用IM")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startRequestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                }).setCancelable(false).show();
    }
    // 开始提交请求权限
    private void startRequestPermission() {
        ActivityCompat.requestPermissions(this, permissions, 321);
    }
    // 用户权限 申请 的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != 321)
            return;
        //API>=23
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int i;
            boolean successFlag = true;
            for (i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    continue;
                // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                boolean b = shouldShowRequestPermissionRationale(permissions[i]);
                if (!b) {
                    // 用户还是想用我的 APP 的
                    // 提示用户去应用设置界面手动开启权限
                    Toast.makeText(this, permissions[i].toString() + "设置失败，请手动设置", Toast.LENGTH_SHORT).show();
                    successFlag = false;
                } else
                    finish();
            }
            if (successFlag) {
                Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Message message = myhandler.obtainMessage();
        message.what = flagJumpToLogin;
        message.sendToTarget();

    }

///////////////////////////////////////////////////////////////////////////////////获取权限内容
    public void jumpToLogin(){
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void jumpToMain(){
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void Init(){
        //启动服务
        // TODO: 2017/4/30 service还未写结束，可以写一个Service管理类,现在会有个bug,就是关闭不了service
        Intent startIntent = new Intent(this, ComService.class);
        startService(startIntent);//记得最后结束
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ILoginUser loginUser = DBManager.queryLoginUser();


                if(loginUser.getId() == 0){
                    Log.d("Splash","need Login");
                    jumpToLogin();
                }
                else{
                    Log.d("Splash","don't need Login");
                    jumpToMain();
                }
            }
        }, 1500);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        databaseHelper = DBManager.getIntance(this);
        databaseHelper.getWritableDatabase();

        //需要获取存储空间访问权限
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //TODO:其他权限的申请还没有
            // 检查该权限是否已经获取
            boolean checkPermissionFlag=false;
            for (int i=0;i<permissions.length;i++){
                if (ContextCompat.checkSelfPermission(this, permissions[i])!=PackageManager.PERMISSION_GRANTED){
                    checkPermissionFlag=true;
                    break;
                }
            }
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (checkPermissionFlag) {
                // 如果没有授予该权限，就去提示用户请求
                showDialogTipUserRequestPermission();
            }else{
                Init();
            }
        }else {
            Init();
        }



    }
}