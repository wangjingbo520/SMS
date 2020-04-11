package com.tools.sms.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author wjb
 * describe
 */
public class XLSUserBean implements Serializable {
    private String phoneNumber;

    //扩展字段 1,2,3,4
    private String xlsOne;
    private String xlsTwo;
    private String xlsThree;
    private String xlsFour;
    private String xlsFive;

    public XLSUserBean() {
        this.xlsOne = "扩展字段1";
        this.xlsTwo = "扩展字段2";
        this.xlsThree = "扩展字段3";
        this.xlsFour = "扩展字段4";
        this.xlsFive = "扩展字段5";

        phoneNumber = "空号码";

    }


    public String getXlsOne() {
        return xlsOne;
    }

    public void setXlsOne(String xlsOne) {
        this.xlsOne = xlsOne;
    }

    public String getXlsTwo() {
        return xlsTwo;
    }

    public void setXlsTwo(String xlsTwo) {
        this.xlsTwo = xlsTwo;
    }

    public String getXlsThree() {
        return xlsThree;
    }

    public void setXlsThree(String xlsThree) {
        this.xlsThree = xlsThree;
    }

    public String getXlsFour() {
        return xlsFour;
    }

    public void setXlsFour(String xlsFour) {
        this.xlsFour = xlsFour;
    }

    public String getXlsFive() {
        return xlsFive;
    }

    public void setXlsFive(String xlsFive) {
        this.xlsFive = xlsFive;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
