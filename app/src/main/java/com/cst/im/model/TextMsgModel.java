package com.cst.im.model;


/**
 * Created by wzb on 2017/5/7.
 */

public class TextMsgModel extends MsgModelBase implements ITextMsg{
    String text;
    //文本信息
    public String getText(){return text;}
    public void setText(String text){
        this.text=text;
    }

}
