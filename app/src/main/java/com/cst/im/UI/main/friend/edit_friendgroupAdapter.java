package com.cst.im.UI.main.friend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cst.im.R;
import com.cst.im.model.GroupItemModel;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by jijinping on 2017/5/21.
 */

public class edit_friendgroupAdapter extends BaseAdapter{
    private ArrayList<GroupItemModel> group;
    private Context context;
    public edit_friendgroupAdapter(Context context,ArrayList<GroupItemModel> group)
    {
        this.context=context;
        this.group=group;
        this.group.add(new GroupItemModel("",R.drawable.add));
        this.group.add(new GroupItemModel("",R.drawable.delete));
    }
    @Override
    public int getCount() {
        return group.size();
    }

    @Override
    public Object getItem(int i) {
        return group.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Viewholder viewholder;
        if(view==null)
        {
            view= LayoutInflater.from(this.context).inflate(R.layout.edit_group_item,viewGroup,false);
            viewholder=new Viewholder();
            viewholder.name=(TextView)view.findViewById(R.id.group_item_name);
            viewholder.icon=(ImageView)view.findViewById(R.id.group_item_icon);
            view.setTag(viewholder);
        }else
        {
            viewholder=(Viewholder) view.getTag();
        }
        viewholder.icon.setImageResource(group.get(i).getIcon());
        viewholder.name.setText(group.get(i).getName());
        return view;
    }
    static class Viewholder
    {
        TextView name;
        ImageView icon;
    }
}
