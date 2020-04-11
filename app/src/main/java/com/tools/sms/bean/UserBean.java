package com.tools.sms.bean;

/**
 * @author wjb
 * describe
 */
public class UserBean extends BaseResponse {


    /**
     * data : {"id":null,"username":"bhufdiguiuhui","password":"3251561456"}
     */


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : null
         * username : bhufdiguiuhui
         * password : 3251561456
         */

        private String user_id;
        private String username;
        private String password;

        private String login_status;
        private String register_date;
        private String start_date = "";
        private String end_date = "";
        private int opening;
        private int remaining_day;
        private String device_id;
        //卡密****//
        private String camilo;


        public String getCamilo() {
            return camilo;
        }

        public void setCamilo(String camilo) {
            this.camilo = camilo;
        }

        public String getDevice_id() {
            return device_id;
        }

        public void setDevice_id(String device_id) {
            this.device_id = device_id;
        }

        public int getRemaining_day() {
            return remaining_day;
        }

        public void setRemaining_day(int remaining_day) {
            this.remaining_day = remaining_day;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        /**
         * user_id : 2
         * login_status : 已登录
         * register_date : 2019-12-22
         * start_date : null
         * end_date : null
         * opening : 0
         */


        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }


        public String getLogin_status() {
            return login_status;
        }

        public void setLogin_status(String login_status) {
            this.login_status = login_status;
        }

        public String getRegister_date() {
            return register_date;
        }

        public void setRegister_date(String register_date) {
            this.register_date = register_date;
        }

        public String getStart_date() {
            return start_date;
        }

        public void setStart_date(String start_date) {
            this.start_date = start_date;
        }

        public String getEnd_date() {
            return end_date;
        }

        public void setEnd_date(String end_date) {
            this.end_date = end_date;
        }

        public int getOpening() {
            return opening;
        }

        public void setOpening(int opening) {
            this.opening = opening;
        }
    }


}
