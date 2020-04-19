package com.tools.sms.bean;

import java.lang.ref.PhantomReference;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wjb（C）
 * describe
 */
public class SendResultBean {

    public SendResultBean(String phoneNumber, String content, int tag, int mainId) {
        this.content = content;
        this.phoneNumber = phoneNumber;
        this.time = new SimpleDateFormat("yyyy/M/Mdd").format(new Date());
        this.tag = tag;
        this.mianId = mainId;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String userId;

    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String content;

    private String time;

    private int tag;

    private int mianId;


    private String phoneNumber;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }



    public int getMianId() {
        return mianId;
    }

    public void setMianId(int mianId) {
        this.mianId = mianId;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
