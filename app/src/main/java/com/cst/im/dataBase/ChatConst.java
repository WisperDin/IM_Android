package com.cst.im.dataBase;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by wzb on 2017/5/9.
 */

public class ChatConst {
    public static final String LISTVIEW_DATABASE_NAME = "listview.db";
    public static final int SENDING = 0;
    public static final int COMPLETED = 1;
    public static final int SENDERROR = 2;

    @IntDef({SENDING, COMPLETED, SENDERROR})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SendState {
    }
}
