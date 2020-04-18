package com.tools.sms.bean;

/**
 * @author wjb（H）
 * @date describe
 */
public class Main {

    private String data;
    private long sucess;
    private long failed;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long getSucess() {
        return sucess;
    }

    public void setSucess(long sucess) {
        this.sucess = sucess;
    }

    public long getFailed() {
        return failed;
    }

    public void setFailed(long failed) {
        this.failed = failed;
    }
}
