package com.cst.im.UI.main.me;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cst.im.R;

import java.util.List;

/**
 * Created by HuangYiXin on 2017/5/2.
 */

public class SettingDetailsAdapter extends ArrayAdapter<SettingDetails> {

    private int resourceId;

    public SettingDetailsAdapter(Context context, int textViewResourceId,
                                 List<SettingDetails>objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SettingDetails settingDetails = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView name = (TextView)view.findViewById(R.id.setting_name);
        TextView value = (TextView)view.findViewById(R.id.setting_value);
        ImageView image = (ImageView)view.findViewById(R.id.setting_image);

        String key = settingDetails.getName();

        name.setText(key);

        if(key.equals("头像")){ // 用户头像
            image.setVisibility(View.VISIBLE);
            value.setVisibility(View.INVISIBLE);
            if(settingDetails.getValue().equals("")) //没有默认头像
            {
                image.setImageResource(R.drawable.profile_icon);
            }else{//TODO 在本地文件中加载用户头像

            }
        }else{
            value.setText(settingDetails.getValue());
        }

        return view;
    }
}
