package com.cst.im.UI.main.me;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cst.im.R;

import java.util.List;

/**
 * Created by HuangYiXin on 2017/4/22.
 */

public class UserSettingAdapter extends ArrayAdapter<Settings> {
    private int resourceId;

    public UserSettingAdapter(Context context,int textViewResourceId,
                              List<Settings> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        Settings settings = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        ImageView image = (ImageView)view.findViewById(R.id.image);
        TextView name = (TextView)view.findViewById(R.id.name);
        image.setImageResource(settings.getImageId());

        name.setText(settings.getName());
        return view;
    }
}
