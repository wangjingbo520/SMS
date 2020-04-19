package com.tools.sms.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.Request;
import com.tools.sms.MyApp;
import com.tools.sms.R;
import com.tools.sms.base.BaseActivity;
import com.tools.sms.base.Constants;
import com.tools.sms.bean.Main;
import com.tools.sms.bean.SendResultBean;
import com.tools.sms.bean.UserBean;
import com.tools.sms.bean.XLSUserBean;
import com.tools.sms.db.dbs.DbManager;
import com.tools.sms.http.InterfaceMethod;
import com.tools.sms.http.RequestHandler;
import com.tools.sms.service.AppConstants;
import com.tools.sms.service.EndSendIntentService;
import com.tools.sms.service.SendSMSService;
import com.tools.sms.tools.DialogToastUtil;
import com.tools.sms.tools.FileUtils;
import com.tools.sms.tools.SPUtils;
import com.tools.sms.tools.ToastUtil;
import com.tools.sms.tools.Utils;
import com.tools.sms.views.BubbleTextView;
import com.tools.sms.views.CustomizedProgressBar;
import com.tools.sms.views.TitleView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

import static com.tools.sms.base.Constants.NUMBER_SELECTOR_TEMPLATE;


/**
 * @author wjb
 * describe
 */
public class ExcellSendActivity extends BaseActivity {

    @BindView(R.id.titleView)
    TitleView titleView;

    @BindView(R.id.tvStatus)
    TextView tvStatus;
    @BindView(R.id.et_status)
    TextView et_status;
    @BindView(R.id.progress)
    CustomizedProgressBar customizedProgressBar;
    @BindView(R.id.etInterval)
    EditText etInterval;
    @BindView(R.id.tvPath)
    TextView tvPath;
    @BindView(R.id.bubble)
    BubbleTextView bubble;
    @BindView(R.id.tv_send_result)
    TextView tv_send_result;
    @BindView(R.id.Pause)
    TextView Pause;
    @BindView(R.id.btnSend)
    TextView btnSend;

    private String path = "";

    private ArrayList<XLSUserBean> beans;


    private Intent service;

    private SMSStatusReceiver receiver;

    private HashMap<String, String> infos;

    private String deviceId = "";


    /**
     * 是否正在发送短信
     */
    private boolean isSendSms = false;
    private SQLiteDatabase db;
    private int mainId;

    //是否发送了
    private volatile boolean hasSendsMS = false;

    private int sucessSize = 0;
    private int failedSize = 0;

    private Intent mIntent;

    private ProgressDialog progressDialog;

    public static void start(Context context, String path) {
        Intent starter = new Intent(context, ExcellSendActivity.class);
        starter.putExtra("path", path);
        context.startActivity(starter);
    }

    @Override
    protected void initData() {
        postcheckIsOpenning();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // showExitDialog(this);
    }

    @Override
    protected void initView() {
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                , WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        path = getIntent().getStringExtra("path");

        tvPath.setText("文件路径：" + path);

        beans = FileUtils.getDataFromXlsFile(path);

        if (beans != null) {
            customizedProgressBar.setMaxCount(beans.size());
            tvStatus.setText("发送进度: 0/" + beans.size());
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在退出中...");
        mIntent = new Intent(this, EndSendIntentService.class);

        receiver = new SMSStatusReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(AppConstants.ACTION_SMS_SEND_NUMBER);
        filter.addAction(AppConstants.ACTION_SMS_DELIVERED_ACTION);
        filter.addAction(AppConstants.ACTION_SMS_SEND_ACTIOIN);
        filter.addAction("pause");
        registerReceiver(receiver, filter);

        titleView.setTitle("群发短信");
        titleView = findViewById(R.id.titleView);
        titleView.setBackImageGone(false);
        titleView.getBackView().setOnClickListener(v -> {
            if (isSendSms) {
                ToastUtil.showMessage("后台正在发送短信，请不要退出此页面！！");
                return;
            }
            progressDialog.show();
            myHandler.postDelayed(mRunnable, 1000);
            finish();
        });

        db = DbManager.getInstance(this).getReadableDatabase();

        mainId = DbManager.creatMainId(db);
        infos = new HashMap<>();
    }

    private Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            progressDialog.dismiss();
            finish();
        }
    };

    @Override
    protected int getContentLayout() {
        return R.layout.activity_excell_send;
    }

    private Handler myHandler = new Handler();

    @OnClick({R.id.btnSend, R.id.stopSend, R.id.bubble, R.id.Pause})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSend:
                checkIsOpenning();
                break;
            case R.id.stopSend:
                progressDialog.show();
                if (service != null) {
                    stopService(service);
                }

                if (hasSendsMS) {
                    //updatedMain();
                }
                myHandler.postDelayed(mRunnable, 1000);
                break;
            case R.id.bubble:
                if (isSendSms) {
                    ToastUtil.showMessage("短信正在发送中，请勿进行其他操作！");
                    return;
                }

                if (beans.size() > 0) {
                    TemplateTextActivity.start(this, 2, true, beans.get(0));
                } else {
                    ToastUtil.showMessage("您选择的excell表没有数据或者数据有异常");
                }
                break;
            case R.id.Pause:
                if (service != null) {
                    if (SendSMSService.pause) {
                        SendSMSService.resumeThread();
                        Pause.setText("暂停发送");
                    } else {
                        Pause.setText("继续发送");
                        SendSMSService.pauseThread();
                    }
                } else {
                    ToastUtil.showMessage("您还没开启发送任务...");
                }
                break;
            default:
                break;
        }
    }

    private void checkIsOpenning() {
        String content = bubble.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            ToastUtil.showMessage("请先添加短信模板");
            return;
        }

        if (TextUtils.isEmpty(path)) {
            ToastUtil.showMessage("请先选择.xls格式文件导入");
            return;
        }

        int status = MyApp.openning;
        if (status == 0) {
            showExitDialog(this);
            return;
        }

        if (TextUtils.isEmpty(device_id)) {
            reLogin("未获取到您的设备id,无法进行登录！您可以检查下是否设置了权限！");
            return;
        }


        if (!deviceId.equals(device_id)) {
            reLogin("您的账号在其他设备上已登录，请退出重新登录");
            return;
        }

        if (isSendSms) {
            ToastUtil.showMessage("短信正在发送中，请勿进行其他操作！");
            return;
        }

        btnSend.setBackground(getResources().getDrawable(R.drawable.btn_shape2));
        sendSMS(content);
    }


    private void postcheckIsOpenning() {
        MyApp.openning = 0;
        Map<String, String> params = new HashMap<>();
        String userName = SPUtils.getInstance().getString(Constants.USER_NAME, "");
        if (TextUtils.isEmpty(userName)) {
            ToastUtil.showMessage("用户名为空，不能发送，请重新登录！！！");
            return;
        }

        params.put("username", userName);
        RequestHandler.addRequest(Request.Method.POST, this, mHandler, Constants.CODE_RESULT,
                params, null, false, InterfaceMethod.QUERY_USER_INFO);
    }


    private void sendSMS(String content) {
        if (beans == null) {
            ToastUtil.showMessage("xls文件存在异常！！！");
            return;
        }

        int time = Integer.parseInt(etInterval.getText().toString().trim());
        if (time < 5) {
            ToastUtil.showMessage("发送间隔不能低于5秒");
            return;
        }

        SPUtils.getInstance().put(Constants.TIEM_INTERVAL, time);

        service = new Intent(this, SendSMSService.class);
        SendSMSService.pause = false;
        Bundle bundle = new Bundle();
        bundle.putSerializable("beans", beans);//序列化
        bundle.putString("content", content);
        service.putExtras(bundle);//发送数据

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(service);
        } else {
            startService(service);
        }

    }


    public class SMSStatusReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (sucessSize == 0 && failedSize == 0) {
                if (!hasSendsMS) {
                    Main main = new Main();
                    main.setMainId(mainId);
                    DbManager.insertMain(db, main);
                    hasSendsMS = true;
                }
            }

            if (AppConstants.ACTION_SMS_SEND_NUMBER.equals(action)) {
                isSendSms = true;
                titleView.setTitle("正在发送中...");
                titleView.setTextColor(Color.RED);
                if (failedSize + sucessSize == customizedProgressBar.getMaxCount()) {
                    titleView.setTitle("发送结束");
                    isSendSms = false;
                }
            } else if (AppConstants.ACTION_SMS_SEND_ACTIOIN.equals(action)) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        String content_s = intent.getStringExtra("content");
                        String phoneNumber = intent.getStringExtra("number");
                        if (!infos.containsKey(phoneNumber)) {
                            infos.put(phoneNumber, "成功");
                            et_status.append(Html.fromHtml("向 "
                                    + phoneNumber + " 发送短信<font color='green'>成功</font><br>"));
                            new DBTask().execute(new SendResultBean(phoneNumber, content_s, 1, mainId));
                        }

                        sucessSize++;
                        tv_send_result.setText("发送结果：成功" + sucessSize + "条  失败" + failedSize + "条");
                        if (failedSize + sucessSize == customizedProgressBar.getMaxCount()) {
                            isSendSms = false;
                            titleView.setTitle("发送结束");

                            //  updatedMain();

                            DialogToastUtil.showDialogToast(ExcellSendActivity.this, "短信已全部发送，总共"
                                    + customizedProgressBar.getMaxCount() + "条，其中成功" + sucessSize + "条,失败" + failedSize + "条");
                        }
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                    default:
                        String content_f = intent.getStringExtra("content");
                        String phoneNumber_1 = intent.getStringExtra("number");
                        if (!infos.containsKey(phoneNumber_1)) {
                            infos.put(phoneNumber_1, "失败");
                            et_status.append(Html.fromHtml("向 " + phoneNumber_1 + " 发送短信<font color='red'>失败</font><br>"));
                            new DBTask().execute(new SendResultBean(phoneNumber_1, content_f, 0, mainId));
                        }
                        failedSize++;
                        tv_send_result.setText("成功:" + sucessSize + "条  失败" + failedSize + "条");
                        if (failedSize + sucessSize == customizedProgressBar.getMaxCount()) {
                            isSendSms = false;
                            titleView.setTitle("发送结束");
                            Toast.makeText(ExcellSendActivity.this.getApplicationContext(), "短信全部发送结束", Toast.LENGTH_LONG).show();
                        }
                        break;
                }
                int numberIndex = intent.getIntExtra("numberIndex", 0);
                customizedProgressBar.setCurrentCount((numberIndex));
                tvStatus.setText("发送进度: " + numberIndex + "/" + beans.size());

                if (numberIndex == customizedProgressBar.getMaxCount() - 1) {
                    //  updatedMain();
                    et_status.append("所有号码已发送完毕\n");
                    et_status.append("关闭短信群发服务. \n ----End----\n");
                    titleView.setTitle("发送结束");
                    isSendSms = false;
                }
            } else if (AppConstants.ACTION_SMS_DELIVERED_ACTION.equals(action)) {
                et_status.append(Html.fromHtml(intent.getStringExtra("number")
                        + " 接收短信<font color='green'>成功</font><br>"));
            } else if ("pause".equals(action)) {
                titleView.setTitle("暂停发送..");
                titleView.setTitle("已暂停发送...");
                titleView.setTextColor(Color.BLACK);
            }
        }
    }

    private class DBTask extends AsyncTask<SendResultBean, Void, Void> {

        @Override
        protected Void doInBackground(SendResultBean... sendResultBeans) {
            try {
                DbManager.insertSendResult(db, sendResultBeans[0]);
                DbManager.updatedMain(db, mainId, sucessSize, failedSize);
            } catch (Exception ex) {

            }
            return null;
        }
    }


    @Override
    public void onDestroy() {
        //发送过短信
        if (receiver != null) {
            unregisterReceiver(receiver);
        }

        if (service != null) {
            stopService(service);
        }

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (isSendSms) {
            ToastUtil.showMessage("后台正在发送短信，请不要退出此页面！！");
            return;
        }
        progressDialog.show();
        myHandler.postDelayed(mRunnable, 1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NUMBER_SELECTOR_TEMPLATE) {
            if (data != null) {
                if (data.getStringExtra("content") != null) {
                    String content = data.getStringExtra("content");
                    bubble.setText(Utils.htmlTextUtils(content));
                    int contentLength = content.length();
                    if (contentLength > 70) {
                        ToastUtil.showMessage("短信长度超过70个文字，将分开发送......");
                    }
                }
            }
        }
    }


    private void reLogin(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.logo);
        builder.setTitle("提示！");
        builder.setMessage(message);
        builder.setPositiveButton("确定", (dialog, which) -> {
            String username = SPUtils.getInstance().getString(Constants.USER_NAME);
            String password = SPUtils.getInstance().getString(Constants.USER_PASSWORD);
            Map<String, String> params = new HashMap<>();
            params.put("username", username);
            params.put("password", password);
            RequestHandler.addRequest(Request.Method.POST, this, mHandler, Constants.CODE_RESULT,
                    params, null, false, InterfaceMethod.EXIT_LOGIN);
        });

        builder.setNegativeButton("取消", (dialog, which) -> dialog.dismiss());
        builder.show();
    }


    @Override
    public void onSuceess(String response, String interfaceMethod) {
        super.onSuceess(response, interfaceMethod);
        if (interfaceMethod.equals(InterfaceMethod.EXIT_LOGIN)) {
            ToastUtil.showMessage("成功退出");
            SPUtils.getInstance().put(Constants.USER_PASSWORD, "");
            SPUtils.getInstance().put(Constants.XIEYI, 0);
            MyApp.getInstance().exit();
            startActivity(new Intent(this, LoginActivity.class));
        } else if (interfaceMethod.equals(InterfaceMethod.QUERY_USER_INFO)) {
            UserBean userBean = gson.fromJson(response, UserBean.class);
            deviceId = userBean.getData().getDevice_id();
            int status = userBean.getData().getOpening();
            MyApp.openning = status;
            deviceId = userBean.getData().getDevice_id();
        }
    }

//    private void updatedMain() {
//        mIntent.putExtra("mainId", mainId);
//        mIntent.putExtra("sucessSize", sucessSize);
//        mIntent.putExtra("failedSize", failedSize);
//        startService(mIntent);
//    }


}
