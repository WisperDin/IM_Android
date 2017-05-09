package com.cst.im.UI.main.friend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cst.im.R;
import com.cst.im.model.IFriendModel;
import com.cst.im.presenter.IFriendPresenter;
import com.cst.im.presenter.IFriendPresenterCompl;
import com.cst.im.view.IFriendView;

import java.util.ArrayList;
import java.util.HashMap;

import static com.cst.im.model.IFriendModel.iFriendModel;
import static com.cst.im.model.UserModel.localUser;

public class SearchFriend extends AppCompatActivity implements IFriendView {

    private int reaultcode;
    private String name;
    EditText et;
    Button bt;
    TextView tv;
    ImageView img;
    private IFriendPresenter IsFriend=new IFriendPresenterCompl(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ativity_ssearch_friend);
        et=(EditText)findViewById(R.id.et_search);
        bt=(Button)findViewById(R.id.bt_search);
        img=(ImageView)findViewById(R.id.img_friend_add);
        tv=(TextView)findViewById(R.id.tv_searchname);
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(judgetextIsNum(et.getText().toString())==0){
                IsFriend.Isfriend(localUser.getId(),Integer.parseInt(et.getText().toString()));
                }
                else{
                    tv.setVisibility(View.VISIBLE);
                    tv.setText("输入不合法");
                }
            }
        });
    }


    public int  judgetextIsNum(String text){
        if(text.length()==0){
            return -1;
        }
        for(int i=0;i<text.length();i++){
            if(text.charAt(i)-'0'>9||text.charAt(i)-'0'<0){
                return -1;
            }
        }
        return 0;
    }


    @Override
    public void onReaultCode(int code,String username){
        this.reaultcode=code;
        this.name=username;
        tv.setVisibility(View.VISIBLE);
        if(code==0){
            img.setVisibility(View.INVISIBLE);
            tv.setText("用户不存在");
            return;
        }
        if(iFriendModel.getfriendlist().contains(username)){
            img.setVisibility(View.INVISIBLE);
            tv.setText("你们已经是好友");
            return;
        }
        img.setVisibility(View.VISIBLE);
        tv.setText(username);
    }
    @Override
    public void onRecvMsg(ArrayList<String> list, HashMap<String ,Integer> NameAndID){

    }

}
