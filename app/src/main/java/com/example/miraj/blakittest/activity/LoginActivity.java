package com.example.miraj.blakittest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.miraj.blakittest.R;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKError;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {
    String[] scope = new String[] {VKScope.FRIENDS, VKScope.GROUPS, VKScope.MESSAGES,
            VKScope.PHOTOS, VKScope.WALL};

    @BindView(R.id.loadingText) TextView loadingView;
    @BindView(R.id.refreshText) TextView refreshView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        VKSdk.login(this, scope);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                setResult(RESULT_OK);
                finish();
            }
            @Override
            public void onError(VKError error) {
                loadingView.setVisibility(View.INVISIBLE);
                refreshView.setVisibility(View.VISIBLE);
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
    }

    @OnClick(R.id.refreshText)
    public void refresh(View view) {
        VKSdk.login(this, scope);
    }
}
