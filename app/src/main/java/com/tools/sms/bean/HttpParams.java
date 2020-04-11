package com.tools.sms.bean;

/**
 * @author wjb
 * describe
 */
public class HttpParams {
//    userid	企业id	企业ID(不验证)
//    account	发送用户帐号	用户帐号，由系统管理员
//    password	发送接口密码	用md5加密方式，md5采用32位大写
//            如abc123加密后为 E99A18C428CB38D5F260853678922E03
//    mobile	全部被叫号码	短信发送的目的号码.多个号码之间用半角逗号隔开
//    content	发送内容	短信的内容，内容需要UTF-8编码，提交内容格式：内容+【签名】。签名是公司的名字或者公司项目名称。示例：您的验证码：1439【腾飞】。【】是签名的标识符。请按照正规的格式提交内容测试，请用正规内容下发，最好不要当成是测试，就当是正式使用在给自己的客户发信息，签名字数3-8个字
//    sendTime	定时发送时间	为空表示立即发送，定时发送格式2018-02-02 09:08:10（可选）
//    action	发送任务命令	设置为固定的:send
//    extno	扩展子号	请先询问配置的通道是否支持扩展子号，如果不支持，请填空。子号只能为数字，且最多5位数。（可选）

    private String userid;
    private String account;
    private String password;
    private String mobile;
    private String content;
    private String sendTime;
    private String action;
    private String extno;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getExtno() {
        return extno;
    }

    public void setExtno(String extno) {
        this.extno = extno;
    }
}
