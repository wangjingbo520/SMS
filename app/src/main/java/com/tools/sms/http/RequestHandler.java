package com.tools.sms.http;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.tools.sms.BuildConfig;
import com.tools.sms.MyApp;
import com.tools.sms.bean.BaseResponse;
import com.tools.sms.tools.ToastUtil;
import com.tools.sms.tools.Utils;
import com.tools.sms.views.LoadingDialog;

import java.util.HashMap;
import java.util.Map;


/**
 * @author wjb
 * describe
 */
public class RequestHandler {

    /**
     * 开始请求
     *
     * @param method  Request.Method.GET 或 Request.Method.POST
     * @param handler 请求结束后将结果作为Message.obj发送到该Handler
     * @param what    请求结束后发送的Message.what
     * @param params  请求参数
     * @param header  请求头
     */
    public static void addRequest(
            final int method, Context context, final Handler handler, final int what,
            final Map<String, String> params, final Map<String, String> header, boolean isShowLoadingDialog, String interfaceMethod) {
        if (isShowLoadingDialog) {
            addRequest(method, handler, what, params, header, new DefaultDialogRequestListener(context), interfaceMethod);
        } else {
            addRequest(method, handler, what, params, header, new DefaultRequestListener(), interfaceMethod);
        }

    }

    /**
     * @param method   Request.Method.GET 或 Request.Method.POST
     * @param handler  传递消息
     * @param what     message.what
     * @param params   请求携带参数
     * @param header   请求头
     * @param listener 请求回调监听
     */
    private static void addRequest(
            int method,
            final Handler handler, final int what,
            final Map<String, String> params, final Map<String, String> header,
            final NetWorkRequestListener listener, String interfaceMethod) {
        listener.onPreRequest();
        StringRequest request = new StringRequest(method, InterfaceMethod.base_url + interfaceMethod, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("onsucess", "onVolleyResponse: " + response);
                onVolleyResponse(response, handler, what, interfaceMethod);
                listener.onResponse();
            }
        }, volleyError -> {
            listener.onFailed();
            onVolleyErrorResponse(volleyError, handler);
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = header;
                if (map == null) {
                    map = new HashMap<>();
                }
                // 在此统一添加header
                //  map.put("Content-type", "application/x-www-form-urlencoded");
//                if (TextUtils.isEmpty(MyApp.getDevice_id())) {
//                    MyApp.initDeviceId(Utils.getIMEI(MyApp.myApp.getApplicationContext()));
//                }
//                String device_id = MyApp.getDevice_id();
//                map.put("device_id", device_id);
                return map;
            }


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return params;
            }
        };

        request.setRetryPolicy(new DefaultRetryPolicy(
                //连接超时设置
                10 * 1000,
                //重新尝试连接次数
                3,
                //曲线增长因子
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        RequestManager.getInstance(MyApp.getInstance()).getRequestQueue().add(request);
    }


    /**
     * 请求成功的回调
     *
     * @param response 网络返回的数据
     * @param handler  请求结束后将结果作为Message.obj发送到该Handler
     * @param what     请求结束后发送的Message.what
     *                 （请求结束后，通过Message.setData设置到Message对象，数据原样返回）
     */
    private static void onVolleyResponse(String response, Handler handler, int what, String interfaceMethod) {
        BaseResponse httpResult = new Gson().fromJson(response, BaseResponse.class);
        if (httpResult.getCode() == 200) {
            //10000 单点登录处理,跳转到某个页面单独处理
            Bundle bundle = new Bundle();
            bundle.putString("response", response);
            bundle.putString("interfaceMethod", interfaceMethod);
            Message message = new Message();
            message.what = what;
            message.setData(bundle);
            handler.sendMessage(message);
        } else if (httpResult.getCode() == 400) {
            ToastUtil.showMessage(httpResult.getMsg());
        }
    }


    private static void onVolleyErrorResponse(VolleyError error, Handler handler) {
        Log.e("onfail", "onVolleyResponse: " + error.getMessage());
        if (error instanceof NoConnectionError || error instanceof com.android.volley.NetworkError) {
            ToastUtil.showMessage("服务器异常！！");
            handler.sendEmptyMessage(0);
        } else if (error instanceof TimeoutError) {
            ToastUtil.showMessage("连接超时");
            handler.sendEmptyMessage(1);
        } else if (error instanceof AuthFailureError) {
            ToastUtil.showMessage("身份验证失败！");
            handler.sendEmptyMessage(2);
        } else if (error instanceof ParseError) {
            handler.sendEmptyMessage(3);
            ToastUtil.showMessage("解析错误！");
        } else if (error instanceof ServerError) {
            ToastUtil.showMessage("服务器响应错误！");
            handler.sendEmptyMessage(4);
        }
    }


    /**
     * 请求过程中显示加载对话框，且自动处理其生命周期
     */
    private static class DefaultDialogRequestListener extends DefaultRequestListener {

        Context context;
        LoadingDialog dialog;

        private DefaultDialogRequestListener(Context context) {
            this.context = context;
            dialog = new LoadingDialog(context);
        }

        @Override
        public void onPreRequest() {
            dialog.show();
        }

        @Override
        public void onResponse() {
            dialog.dismiss();
        }

        @Override
        public void onFailed() {
            dialog.dismiss();
        }
    }


    /**
     * 请求过程中没有加载进度框
     */
    private static class DefaultRequestListener implements NetWorkRequestListener {


        @Override
        public void onPreRequest() {

        }

        @Override
        public void onResponse() {

        }

        @Override
        public void onFailed() {

        }

    }

    /**
     * 用于所有网络请求，在不同时机回调的接口
     */
    private interface NetWorkRequestListener {
        void onPreRequest();

        void onResponse();

        void onFailed();

    }
}
