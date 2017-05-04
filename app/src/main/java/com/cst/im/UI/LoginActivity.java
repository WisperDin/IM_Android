package com.cst.im.UI;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cst.im.NetWork.ComService;
import com.cst.im.R;
import com.cst.im.UI.main.MainActivity;
import com.cst.im.presenter.ILoginPresenter;
import com.cst.im.presenter.LoginPresenterCompl;
import com.cst.im.presenter.Status;
import com.cst.im.view.ILoginView;

public class LoginActivity extends AppCompatActivity implements ILoginView,View.OnClickListener,View.OnFocusChangeListener{
    //显示动画
    final TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
            -1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
    private EditText editUser;
    private EditText editPwd;
    private Button btnLogin;
    private ImageView topPicture ;
    private ImageView showPassword;

    private ImageView ivLoginByQQ;
    private ImageView ivLoginByWechat;
    private ImageView ivLoginByWeibo;
    private TextView tvForgetPassword;
    private LinearLayout activityMain ;
    private TextInputLayout tilUsername;
    private TextInputLayout tilPassword;

    // 注册按钮
    Button btnRegister;

    //登录业务逻辑
    ILoginPresenter loginPresenter;

    ComService comService;
    ServiceConnection serviceConn;
    ////////////////////////////////////////////////////////////////////////////////获取权限
    // 要申请的权限
    private String[] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private AlertDialog dialog;
    // 提示用户该请求权限的弹出框
    private void showDialogTipUserRequestPermission() {
        new AlertDialog.Builder(this)
                .setTitle("存储权限不可用")
                .setMessage("由于IM需要访问存储空间，获取您需要发送的文件；\n否则，您将无法正常使用IM")
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

        if (requestCode == 321) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    // 判断用户是否 点击了不再提醒。(检测该权限是否还可以申请)
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    if (!b) {
                        // 用户还是想用我的 APP 的
                        // 提示用户去应用设置界面手动开启权限
                        showDialogTipUserGoToAppSettting();
                    } //else
                    //finish();
                } else {
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // 提示用户去应用设置界面手动开启权限
    private void showDialogTipUserGoToAppSettting() {

        dialog = new AlertDialog.Builder(this)
                .setTitle("存储权限不可用")
                .setMessage("请在-应用设置-权限-中，允许IM使用存储权限来保存用户数据")
                .setPositiveButton("立即开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 跳转到应用设置界面
                        goToAppSetting();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                }).setCancelable(false).show();
    }

    // 跳转到当前应用的设置界面
    private void goToAppSetting() {
        Intent intent = new Intent();

        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);

        startActivityForResult(intent, 123);
    }

    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123) {

            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // 检查该权限是否已经获取
                int i = ContextCompat.checkSelfPermission(this, permissions[0]);
                // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
                if (i != PackageManager.PERMISSION_GRANTED) {
                    // 提示用户应该去应用设置界面手动开启权限
                    showDialogTipUserGoToAppSettting();
                } else {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Toast.makeText(this, "权限获取成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
///////////////////////////////////////////////////////////////////////////////////获取权限内容


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.cst.im.R.layout.activity_login);

        initView();

        //init login presenter
        loginPresenter = new LoginPresenterCompl(this);
        mShowAction.setDuration(500);//动画运行时间

        btnLogin.setOnClickListener(this); // 登录按钮
        btnRegister.setOnClickListener(this); // 注册按钮
        editPwd.setOnFocusChangeListener(this); // 输入密码时图标消失
        onEditTip();

        //不知道为什么，在聊天界面放这个会报错，，，
        //需要获取存储空间访问权限
        // 版本判断。当手机系统大于 23 时，才有必要去判断权限是否获取
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // 检查该权限是否已经获取
            int i = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (i != PackageManager.PERMISSION_GRANTED) {
                // 如果没有授予该权限，就去提示用户请求
                showDialogTipUserRequestPermission();
            }
        }
//网络接口测试部分
////////////////////////////////////////////////////////////////////
        /*
        serviceConn =new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                comService = ((ComService.MyBind)service).getService();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        bindService(new Intent(this, ComService.class), serviceConn, BIND_AUTO_CREATE);
        */
        //启动服务
        Intent startIntent = new Intent(this, ComService.class);
        startService(startIntent);//记得最后结束
        // TODO: 2017/4/30 service还未写结束，可以写一个Service管理类,现在会有个bug,就是关闭不了service
//////////////////////////////////////////////////////////////////////////////

    }

    // 初始化控件
    private void initView(){
        //find Control
        topPicture = (ImageView) findViewById(R.id.top_picture);
        editUser = (EditText) findViewById(R.id.username);
        editPwd = (EditText) findViewById(R.id.password);
        btnLogin = (Button) findViewById(R.id.login_button);
        ivLoginByQQ = (ImageView) findViewById(R.id.qq_login);
        ivLoginByWechat = (ImageView) findViewById(R.id.wechat_login);
        ivLoginByWeibo = (ImageView) findViewById(R.id.weibo_login);
        activityMain = (LinearLayout) findViewById(R.id.activity_main);
        tilUsername = (TextInputLayout) findViewById(R.id.usernameWrapper);
        tilPassword = (TextInputLayout) findViewById(R.id.passwordWrapper);
        btnRegister = (Button) findViewById(R.id.register_action);
    }

    //处理登录事件的UI提示
    @Override
    public void onLoginResult(int rslCode){

        if (rslCode==Status.Login.LOGINSUCCESS){
            //页面跳转
            Intent it = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(it);
            LoginActivity.this.finish();
            Toast.makeText(this,"登录成功", Toast.LENGTH_SHORT).show();
        }
        else if(rslCode == Status.Login.LOGINFAILED)
            Toast.makeText(this,"用户名或密码不正确",Toast.LENGTH_SHORT).show();

    }

    //网络错误提示
    @Override
    public void onNetworkError() {

    }
    //输入时的提醒
    @Override
    public void onEditTip() {
        editUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Drawable drawable = getResources().getDrawable(R.drawable.login_warning);
                drawable.setBounds(0,0,56,56);
                switch (loginPresenter.judgeUsername(editUser.getText().toString())){
                    case Status.Login.USERNAME_INVALID:
                        editUser.setError("=.=",drawable);
                        break;
                    case Status.Login.USERNAME_PHONE:
                        editUser.setError("手机号",drawable);
                        break;
                    case Status.Login.USERNAME_EMAIL:
                        editUser.setError("邮箱",drawable);
                        break;
                    case Status.Login.USERNAME_ACCOUNT:
                        editUser.setError("用户名",drawable);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Drawable drawable = getResources().getDrawable(R.drawable.login_warning);
                drawable.setBounds(0,0,56,56);
                if(loginPresenter.judgePassword(editPwd.getText().toString())){
                    editPwd.setError("√",drawable);
                }
                else {
                    editPwd.setError("x",drawable);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.login_button:
                String user = editUser.getText().toString();
                String pwd = editPwd.getText().toString();
                //TODO: 暂时版本
                loginPresenter.doLogin(editUser.getText().toString(),editPwd.getText().toString());
                /*if(loginPresenter.canLogin(user,pwd)) //判断是否符合登录条件
                    loginPresenter.doLogin(editUser.getText().toString(),editPwd.getText().toString());
                else
                    Toast.makeText(this,"用户名或密码不规范", Toast.LENGTH_LONG).show();*/
                break;
            case R.id.register_action:
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus){
            topPicture.setVisibility(View.GONE);
        }
        else{
            topPicture.startAnimation(mShowAction);
            topPicture.setVisibility(View.VISIBLE);
        }
    }



}
