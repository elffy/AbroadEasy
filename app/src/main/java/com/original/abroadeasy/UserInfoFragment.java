package com.original.abroadeasy;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by zengjinlong on 15-10-29.
 */
public class UserInfoFragment extends BaseFragment {

    private static final String SERVICE_TEL = "10010";
    private Activity mActivity;
    private View mView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_user_info, container, false);
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
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
    }
}
