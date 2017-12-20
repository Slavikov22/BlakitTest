package com.example.miraj.blakittest.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.miraj.blakittest.R;
import com.example.miraj.blakittest.fragment.NavigatorFragment;
import com.example.miraj.blakittest.fragment.ProfileInfoFragment;

public class ProfileActivity extends AppCompatActivity {
    public static final String ARG_ID = "profileId";

    protected int profileId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        profileId = intent.getIntExtra(ARG_ID, 0);

        setNavigatorFragment();
        setProfileInfoFragment();
    }

    protected void setNavigatorFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.navigatorLayout, NavigatorFragment.newInstance(false))
                .commit();
    }

    protected void setProfileInfoFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.profileLayout, ProfileInfoFragment.newInstance(profileId))
                .commit();
    }
}
