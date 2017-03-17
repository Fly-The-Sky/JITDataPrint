package com.dicot.jitprint.activity.login;

import android.animation.ValueAnimator;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.dd.CircularProgressButton;
import com.dicot.jitprint.R;
import com.dicot.jitprint.activity.main.MainActivity;
import com.dicot.jitprint.base.BaseActivity;
import com.dicot.jitprint.utils.DialogUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


/**
 * Created by Hunter
 * Describe 登录功能界面
 * on 2017/3/11.
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends BaseActivity {

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{"foo:hello", "bar:world"};

    // UI references.
    @ViewInject(value = R.id.username)
    private AutoCompleteTextView mUserNameView;
    @ViewInject(value = R.id.password)
    private EditText mPasswordView;
    @ViewInject(value = R.id.email_sign_in_button)
    private CircularProgressButton mSignInButton;


    @Override
    public void init() {
        super.init();
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
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        mSignInButton.setIndeterminateProgressMode(true);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //                attemptLogin();
                int post = mSignInButton.getProgress();
                if (mSignInButton.getProgress() == 0) {
                    //                    simulateSuccessProgress(mSignInButton);
                    mSignInButton.setProgress(50);
                } else {
                    mSignInButton.setProgress(0);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }


    private void simulateSuccessProgress(final CircularProgressButton button) {
        ValueAnimator widthAnimation = ValueAnimator.ofInt(1, 100);
        widthAnimation.setDuration(1500);
        widthAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                button.setProgress(value);
            }
        });
        widthAnimation.start();
    }

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

