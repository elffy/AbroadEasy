package com.original.abroadeasy.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.original.abroadeasy.R;
import com.original.abroadeasy.data.UserInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zengjinlong on 15-10-29.
 */
public class UserInfoFragment extends BaseFragment {

    private static final String SERVICE_TEL = "10010";
    private View mView;
    @Bind(R.id.user_avatar)
    ImageView mUserAvatar;
    @Bind(R.id.user_name)
    TextView mUserNameView;
    @Bind(R.id.login_layout)
    View mLoginLayout;

    private UserInfo mUserInfo;

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
            mUserNameView.setVisibility(View.GONE);
            mLoginLayout.setVisibility(View.VISIBLE);
        } else {
            mUserNameView.setText(mUserInfo.getName());
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mUserInfo = UserInfo.getLoggedInUser(mActivity);
        if (mUserInfo == null) {
            mUserNameView.setVisibility(View.GONE);
            mLoginLayout.setVisibility(View.VISIBLE);
        } else {
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

    @OnClick(R.id.setting)
    void onSettingClick(View v) {
        startActivity(new Intent(mActivity, SettingActivity.class));
    }

    @OnClick(R.id.service_tel)
    void onServiceClick(View v) {
        Uri uri = Uri.parse("tel:" + SERVICE_TEL);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        startActivity(intent);
    }

    @OnClick(R.id.user_avatar)
    void onUserAvatarClick(View v) {
    }

    @OnClick(R.id.btn_login)
    void onLogingBtnClick(View v) {
        popDialog(R.string.login);
    }

    @OnClick(R.id.btn_register)
    void onRegisterBtnClick(View v) {
        popDialog(R.string.register);
    }

    /*private void popDialog(int titleResId) {
        final View loginLayout = mActivity.getLayoutInflater().inflate(R.layout.log_in_dialog_layout, null);
        final AlertDialog dialog = new AlertDialog.Builder(mActivity)
                .setTitle(titleResId)
                .setView(loginLayout)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        *//* User clicked OK so do some stuff *//*
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                })
                .show();
        Button okBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        okBtn.setOnClickListener(new View.OnClickListener() {
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
    }*/

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
}
