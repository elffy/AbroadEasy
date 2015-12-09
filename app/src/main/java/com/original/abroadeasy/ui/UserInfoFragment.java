package com.original.abroadeasy.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.original.abroadeasy.R;
import com.original.abroadeasy.model.UserInfo;
import com.original.abroadeasy.network.weibo.AccessTokenKeeper;
import com.original.abroadeasy.network.weibo.WBConstants;
import com.original.abroadeasy.util.LogUtil;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;

import java.text.SimpleDateFormat;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    private UserInfo mUserInfo;

    private AuthInfo mAuthInfo;
    /** 显示认证后的信息，如 AccessToken */
    private TextView mTokenText;


    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken mAccessToken;

    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler mSsoHandler;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user_info, container, false);
        ButterKnife.bind(this, mView);
        mUserInfo = UserInfo.getLoggedInUser(mActivity);
        if (mUserInfo == null) {
            mUserInfoPage.setVisibility(View.GONE);
            inflateLoginView();
        } else {
            mUserNameView.setText(mUserInfo.getName());
        }

        // 快速授权时，请不要传入 SCOPE，否则可能会授权不成功
        mAuthInfo = new AuthInfo(mActivity, WBConstants.APP_KEY, WBConstants.REDIRECT_URL, WBConstants.SCOPE);
        mSsoHandler = new SsoHandler(mActivity, mAuthInfo);
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
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mUserInfo = UserInfo.getLoggedInUser(mActivity);
        if (mUserInfo == null) {
            mUserInfoPage.setVisibility(View.GONE);
            if (mLoginView == null) {
                inflateLoginView();
            }
            mLoginView.setVisibility(View.VISIBLE);
        } else {
            mUserInfoPage.setVisibility(View.VISIBLE);
            mUserNameView.setText(mUserInfo.getName());
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
        }
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
                mSsoHandler.authorize(new AuthListener());
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
                mUserInfo = new UserInfo(userName, password);
                UserInfo.saveLoggedInUser(mActivity,mUserInfo);
                mUserNameView.setVisibility(View.VISIBLE);
                mUserNameView.setText(mUserInfo.getName());
                dialog.dismiss();
            }
        });
    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {
            // 从 Bundle 中解析 Token
            mAccessToken = Oauth2AccessToken.parseAccessToken(values);
            //从这里获取用户输入的 电话号码信息
            String  phoneNum =  mAccessToken.getPhoneNum();
            if (mAccessToken.isSessionValid()) {
                // 显示 Token
                updateTokenView(false);

                // 保存 Token 到 SharedPreferences
                AccessTokenKeeper.writeAccessToken(mActivity, mAccessToken);
                Toast.makeText(mActivity,
                        R.string.weibosdk_demo_toast_auth_success, Toast.LENGTH_SHORT).show();
            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = getString(R.string.weibosdk_demo_toast_auth_failed);
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(mActivity,
                    R.string.weibosdk_demo_toast_auth_canceled, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(mActivity,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 显示当前 Token 信息。
     *
     * @param hasExisted 配置文件中是否已存在 token 信息并且合法
     */
    private void updateTokenView(boolean hasExisted) {
        String date = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(
                new java.util.Date(mAccessToken.getExpiresTime()));
        String format = getString(R.string.weibosdk_demo_token_to_string_format_1);
        mTokenText.setText(String.format(format, mAccessToken.getToken(), date));

        String message = String.format(format, mAccessToken.getToken(), date);
        if (hasExisted) {
            message = getString(R.string.weibosdk_demo_token_has_existed) + "\n" + message;
        }
        mTokenText.setText(message);
    }
}
