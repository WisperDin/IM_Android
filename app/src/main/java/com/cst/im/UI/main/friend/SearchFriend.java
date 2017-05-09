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
        tv=(TextView)findViewById(R.id.tv_searchname);
        img=(ImageView)findViewById(R.id.img_friend_add);
        bt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(et.getText().toString()!=""){
                IsFriend.Isfriend(5,Integer.parseInt(et.getText().toString()));
                }
            }
        });
    }
    @Override
    public void onReaultCode(int code,String username){
        this.reaultcode=code;
        this.name=username;
        tv.setVisibility(View.VISIBLE);
        if(code==0){
            tv.setText("用户不存在");
            return;
        }
        if(iFriendModel.getfriendlist().contains("username")){
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
