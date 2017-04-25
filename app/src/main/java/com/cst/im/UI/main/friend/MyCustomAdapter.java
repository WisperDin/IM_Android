package com.cst.im.UI.main.friend;

/**
 * Created by sun on 2017/4/23.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cst.im.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

public class MyCustomAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private static final int TYPE_MAX_COUNT = TYPE_SEPARATOR + 1;
    public List<Map<String, Object>> data=new ArrayList<Map<String, Object>>();
    private LayoutInflater inflater;
    private TreeSet<Integer> set = new TreeSet<Integer>();

    public MyCustomAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void addItem(String item,Object icon) {
        Map<String, Object> map;
        map = new HashMap<String, Object>();
        map.put("img", icon);
        map.put("name", item);
        data.add(map);;
    }

    public void addSeparatorItem(String item,Object icon) {
        Map<String, Object> map;
        map = new HashMap<String, Object>();
        map.put("img", icon);
        map.put("name", item);
        data.add(map);
        set.add(data.size() - 1);
    }

    public int getItemViewType(int position) {
        return set.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int type = getItemViewType(position);
        if (convertView == null) {
            holder = new ViewHolder();
            switch (type) {
                case TYPE_ITEM:
                    convertView = inflater.inflate(R.layout.friend_item1, null);
                    holder.textView = (TextView) convertView
                            .findViewById(R.id.UserName);
                    holder.icon=(ImageView)convertView.findViewById(R.id.icon);
                    break;
                case TYPE_SEPARATOR:
                    convertView = inflater.inflate(R.layout.friend_item2, null);
                    holder.icon=(ImageView)convertView.findViewById(R.id.icon);
                    holder.textView = (TextView) convertView
                            .findViewById(R.id.item2);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.icon.setImageResource((Integer) data.get(position).get("img"));
        holder.textView.setText((String) data.get(position).get("name"));
        //holder.textView.setText(data.get(position));
        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
        public ImageView icon;
    }
}
