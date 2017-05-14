package com.cst.im.model;

/**
 * Created by wzb on 2017/5/7.
 */

public interface ITextMsg extends IBaseMsg {
    //文本信息
    String getText();
    void setText(String text);
}
