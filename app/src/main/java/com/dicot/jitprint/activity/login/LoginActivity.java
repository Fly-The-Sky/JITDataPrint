package com.dicot.jitprint.activity.login;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;

import com.dd.CircularProgressButton;
import com.dicot.jitprint.R;
import com.dicot.jitprint.activity.main.MainActivity;
import com.dicot.jitprint.base.BaseActivity;
import com.dicot.jitprint.utils.AppConst;
import com.dicot.jitprint.utils.DialogUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Random;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.gui.RegisterPage;


/**
 * Created by Hunter
 * Describe 登录功能界面
 * on 2017/3/11.
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {


    ActivtyHandler mHandler;
    private static final String[] DUMMY_CREDENTIALS = new String[]{"foo:hello", "bar:world"};

    @ViewInject(value = R.id.login_user_username)
    private EditText mUserNameView;
    @ViewInject(value = R.id.login_user_password)
    private EditText mPasswordView;
    @ViewInject(value = R.id.sign_in_button_submit)
    private CircularProgressButton mSignInButton;
    @ViewInject(value = R.id.sign_in_button_register)
    private Button mSmsButton;
    // 短信注册，随机产生头像
    private static final String[] AVATARS = {
            "http://tupian.qqjay.com/u/2011/0729/e755c434c91fed9f6f73152731788cb3.jpg",
            "http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
            "http://img1.touxiang.cn/uploads/allimg/111029/2330264224-36.png",
            "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
            "http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
            "http://img1.touxiang.cn/uploads/20121224/24-054837_708.jpg",
            "http://img1.touxiang.cn/uploads/20121212/12-060125_658.jpg",
            "http://img1.touxiang.cn/uploads/20130608/08-054059_703.jpg",
            "http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
            "http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
            "http://img1.touxiang.cn/uploads/20130515/15-080722_514.jpg",
            "http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg"
    };

    @Override
    public void init() {
        super.init();
        SMSSDK.initSDK(this, AppConst.APPKEY, AppConst.APPSECRET);
        x.view().inject(this);
        setActionTitle("登录");
    }

    @Override
    public void initData() {
        super.initData();
        mUserNameView.setText("foo");
        mPasswordView.setText("hello");
    }

    @Override
    public void initListener() {
        super.initListener();

        mSignInButton.setIndeterminateProgressMode(true);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSignInButton.getProgress() == 0) {
                    mSignInButton.setProgress(50);
                }


                mHandler = new ActivtyHandler();
                Message message = Message.obtain();
                message.what = 0x01;
                mHandler.sendMessageDelayed(message, 2000);

            }
        });
        mSmsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                registerPageSms();
            }
        });
    }

    private void registerPageSms() {
        // 打开注册页面
        RegisterPage registerPage = new RegisterPage();
        registerPage.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // 解析注册结果
                if (result == SMSSDK.RESULT_COMPLETE) {
                    @SuppressWarnings("unchecked")
                    HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country");
                    String phone = (String) phoneMap.get("phone");
                    // 提交用户信息
                    registerUser(country, phone);
                }
            }
        });
        registerPage.show(this);
    }

    // 提交用户信息
    private void registerUser(String country, String phone) {
        Random rnd = new Random();
        int id = Math.abs(rnd.nextInt());
        String uid = String.valueOf(id);
        String nickName = "SmsSDK_User_" + uid;
        String avatar = AVATARS[id % 12];
        SMSSDK.submitUserInfo(uid, nickName, avatar, country, phone);
    }

    EventHandler eh = new EventHandler() {

        @Override
        public void afterEvent(int event, int result, Object data) {

            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                }
            } else {
                ((Throwable) data).printStackTrace();
            }
        }
    };


    private void attemptLogin() {
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        String username = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;//焦点所在控件

        if (TextUtils.isEmpty(username)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    class ActivtyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            switch (what) {
                case 0x01:
                    mSignInButton.setProgress(0);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                break;
            case KeyEvent.KEYCODE_HOME:
                break;
            case KeyEvent.KEYCODE_BACK:
                new DialogUtils(this).showDialogExit(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                //                View layout = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                //                TextView textView = (TextView) layout.findViewById(R.id.txt_dialog_layout_before);
                //                textView.setText("验证码：");
                //                EditText editText = new EditText(this);
                //                CustomDialog dialog = new CustomDialog(this, editText, R.style.dialog);
                //                dialog.show();
                break;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}

