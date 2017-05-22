package com.cst.im.UI.main.friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cst.im.R;
import com.cst.im.model.FriendgroupModel;

import java.util.ArrayList;

/**
 * Created by jijinping on 2017/5/17.
 */

public class friendgroupAdapter extends BaseAdapter{
    private ArrayList<FriendgroupModel> mGroupItem;
    private Context mContext;
    public friendgroupAdapter(ArrayList<FriendgroupModel> mGroupItem, Context mContext)
    {
        this.mGroupItem=mGroupItem;
        this.mContext=mContext;
    }
    @Override
    public int getCount() {
        return mGroupItem.size();
    }

    @Override
    public Object getItem(int i) {
        return mGroupItem.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null)
        {
            view= LayoutInflater.from(this.mContext).inflate(R.layout.friend_group_item,viewGroup,false);
            viewHolder=new ViewHolder();
            viewHolder.content=(TextView) view.findViewById(R.id.group_content);
            view.setTag(viewHolder);
        }else {
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.content.setText(mGroupItem.get(i).GetTextContent());
        return view;
    }
    static class ViewHolder
    {
        TextView content;
    }

}
