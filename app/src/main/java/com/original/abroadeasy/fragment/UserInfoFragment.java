package com.original.abroadeasy.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.TextView;

import com.original.abroadeasy.R;
import com.original.abroadeasy.login.LoginApi;
import com.original.abroadeasy.login.OnLoginListener;
import com.original.abroadeasy.model.UserAccount;
import com.original.abroadeasy.util.LogUtil;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by zengjinlong on 15-10-29.
 */
public class UserInfoFragment extends BaseFragment implements View.OnClickListener {

    private static final String SERVICE_TEL = "10010";
    private View mView;
    @Bind(R.id.user_info_page)
    View mUserInfoPage;

    @Bind(R.id.user_name)
    TextView mUserNameView;

    @Bind(R.id.login_page)
    ViewStub mLoginViewStub;

    @Bind(R.id.signup_page)
    ViewStub mSignUpViewStub;

    private View mLoginView;
    private View mSignUpView;

    private UserAccount mUserAccount;

    public final static int LOGIN_TYPE_WEIBO = 1;
    public final static int LOGIN_TYPE_FACEBOOK = 2;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user_info, container, false);
        ButterKnife.bind(this, mView);
        mUserAccount = UserAccount.getLoggedInUser(mActivity);
        if (mUserAccount == null) {
            mUserInfoPage.setVisibility(View.GONE);
            inflateLoginView();
        } else {
            mUserNameView.setText(mUserAccount.getName());
        }

        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private View mLoginBtn;
    private View mSignUpTip;
    private View mForgetPwTip;
    private void inflateLoginView() {
        if (mLoginView == null) {
            mLoginView = mLoginViewStub.inflate();
            mLoginBtn = mLoginView.findViewById(R.id.btn_login);
            mSignUpTip = mLoginView.findViewById(R.id.signup_tip);
            mForgetPwTip = mLoginView.findViewById(R.id.forget_password_tip);
            mLoginBtn.setOnClickListener(this);
            mSignUpTip.setOnClickListener(this);
            mForgetPwTip.setOnClickListener(this);
        }
    }

    private void inflateSignUpView() {
        if (mSignUpView == null) {
            mSignUpView = mSignUpViewStub.inflate();
            mSignUpView.findViewById(R.id.btn_singup).setOnClickListener(this);
            mSignUpView.findViewById(R.id.signup_back).setOnClickListener(this);
            //Add by yangli
            mSignUpView.findViewById(R.id.weibo_singup).setOnClickListener(this);
            mSignUpView.findViewById(R.id.wechat_singup).setOnClickListener(this);
            //Add by yangli end
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mUserAccount = UserAccount.getLoggedInUser(mActivity);
        if (mUserAccount == null) {
            mUserInfoPage.setVisibility(View.GONE);
            if (mLoginView == null) {
                inflateLoginView();
            }
            mLoginView.setVisibility(View.VISIBLE);
        } else {
            mUserInfoPage.setVisibility(View.VISIBLE);
            mUserNameView.setText(mUserAccount.getName());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        ButterKnife.unbind(this);
        super.onDestroyView();
    }

    @Override
    public void onClick(View view) {
        LogUtil.d("onClicked!!!!");
        switch (view.getId()) {
            case R.id.btn_login:
                break;
            case R.id.btn_singup:
                break;
            case R.id.signup_tip:
                inflateSignUpView();
                mSignUpView.setVisibility(View.VISIBLE);
                mLoginView.setVisibility(View.GONE);
                break;
            case R.id.forget_password_tip:
                break;
            case R.id.signup_back:
                backToLogin();
                break;
            case R.id.weibo_singup:
                login(SinaWeibo.NAME);
                break;
            case R.id.wechat_singup:
                login(Wechat.NAME);
                break;
        }
    }

    private void login(String platformName) {
        LoginApi api = new LoginApi();
        //设置登陆的平台后执行登陆的方法
        api.setPlatform(platformName);
        api.setOnLoginListener(new OnLoginListener() {
            public boolean onLogin(String platform, HashMap<String, Object> res) {
                // 在这个方法填写尝试的代码，返回true表示还不能登录，需要注册
                // 此处全部给回需要注册
                return true;
            }

            public boolean onRegister(UserAccount info) {
                // 填写处理注册信息的代码，返回true表示数据合法，注册页面可以关闭
                return true;
            }
        });
        api.login(getActivity());
    }

    @Override
    public boolean handleBackKey() {
        if (mSignUpView.getVisibility() == View.VISIBLE) {
            backToLogin();
            return true;
        }
        return false;
    }

    private void backToLogin() {
        mSignUpView.setVisibility(View.GONE);
        mLoginView.setVisibility(View.VISIBLE);
    }

    private void popDialog(int titleResId) {
        final View loginLayout = mActivity.getLayoutInflater().inflate(R.layout.third_log_in_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(mActivity)
                .setTitle(titleResId)
                .setView(loginLayout)
                .show();
        Button weiboBtn = (Button) loginLayout.findViewById(R.id.button);
        weiboBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button weixinBtn = (Button) loginLayout.findViewById(R.id.button2);
        weixinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = ((TextView) loginLayout.findViewById(R.id.username_edit))
                        .getText().toString().trim();
                String password = ((TextView) loginLayout.findViewById(R.id.password_edit))
                        .getText().toString().trim();
                if ("".equals(userName)) {
                    return;
                }
                if ("".equals(password)) {
                    return;
                }
                mUserAccount = new UserAccount(userName, password);
                UserAccount.saveLoggedInUser(mActivity, mUserAccount);
                mUserNameView.setVisibility(View.VISIBLE);
                mUserNameView.setText(mUserAccount.getName());
                dialog.dismiss();
            }
        });
    }




}
