package com.cst.im.UI.main.friend;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.cst.im.R;


/**
 * Created by sun on 2017/4/28.
 */

public class Customdialog extends Dialog {

    public Customdialog(Context context) {
        super(context);
    }

    public Customdialog(Context context, int theme) {
        super(context, theme);
    }

    public static class Builder {
        private Context context;
        public TextView tv;
        private View contentView;
        private Dialog mdialog;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }


        public TextView gettextview(){
            return tv;
        }
        public Customdialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final Customdialog dialog = new Customdialog(context,R.style.Dialog);
            View layout = inflater.inflate(R.layout.frienddialog, null);
            tv=(TextView)layout.findViewById(R.id.frienddialogitem);
            dialog.addContentView(layout, new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            dialog.setContentView(layout);
            mdialog=dialog;
            return dialog;
        }
    }
}