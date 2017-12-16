package com.example.miraj.blakittest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.miraj.blakittest.R;
import com.vk.sdk.VKSdk;

public class ProfileActivity extends AppCompatActivity {
    private static final int LOGIN_ACTIVITY_R_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        if (!VKSdk.isLoggedIn())
            login();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGIN_ACTIVITY_R_CODE){
            if (resultCode == RESULT_CANCELED)
                finish();
        }
    }

    protected void login(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_ACTIVITY_R_CODE);
    }
}
