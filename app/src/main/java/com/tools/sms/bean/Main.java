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
public class Main extends BaseModel {

    @PrimaryKey(autoincrement = true)
    private long id;

    @Column
    private String time;

    @Column
    private int sucess;

    @Column
    private int failed;

    @Column
    private int mainId;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSucess() {
        return sucess;
    }

    public void setSucess(int sucess) {
        this.sucess = sucess;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getMainId() {
        return mainId;
    }

    public void setMainId(int mainId) {
        this.mainId = mainId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Main{" +
                "id=" + id +
                ", time='" + time + '\'' +
                ", sucess=" + sucess +
                ", failed=" + failed +
                ", mainId=" + mainId +
                '}';
    }
}
