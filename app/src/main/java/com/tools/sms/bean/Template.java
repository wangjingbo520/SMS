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
public class Template extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private long id;


    @Column
    private String content;

    @Column
    private String time;


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
}
