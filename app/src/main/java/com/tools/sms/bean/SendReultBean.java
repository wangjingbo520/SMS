package com.tools.sms.bean;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.tools.sms.AppDatabase;

/**
 * @author wjb（H）
 * @date describe
 */

@Table(database = AppDatabase.class)
public class SendReultBean extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private long id;

    @Column
    private String content;

    @Column
    private String time;

    @Column
    private int tag;

    @Column
    private int mianId;

    @Column
    private String phoneNumber;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    public int getMianId() {
        return mianId;
    }

    public void setMianId(int mianId) {
        this.mianId = mianId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
