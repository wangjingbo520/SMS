package com.tools.sms.bean;

/**
 * @author wjb（C）
 * describe
 */


public class VersionApp extends BaseResponse {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public static class DataBean {
        private int versionCode;
        private String versionName;
        private String appName;
        private String versionDescribed;
        private String downUrl;
        private String apkName;

        public String getDownUrl() {
            return downUrl;
        }

        public void setDownUrl(String downUrl) {
            this.downUrl = downUrl;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public String getAppName() {
            return appName;
        }

        public void setAppName(String appName) {
            this.appName = appName;
        }

        public String getVersionDescribed() {
            return versionDescribed;
        }

        public void setVersionDescribed(String versionDescribed) {
            this.versionDescribed = versionDescribed;
        }


        public String getApkName() {
            return apkName;
        }

        public void setApkName(String apkName) {
            this.apkName = apkName;
        }

        @Override
        public String toString() {
            return "DataBean{" +
                    "versionCode=" + versionCode +
                    ", versionName='" + versionName + '\'' +
                    ", appName='" + appName + '\'' +
                    ", versionDescribed='" + versionDescribed + '\'' +
                    ", downUrl='" + downUrl + '\'' +
                    ", apkName='" + apkName + '\'' +
                    '}';
        }
    }


}
