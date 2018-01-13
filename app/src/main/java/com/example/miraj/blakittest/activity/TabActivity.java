package com.example.miraj.blakittest.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.miraj.blakittest.R;
import com.example.miraj.blakittest.fragment.ProfileFragment;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabActivity extends AppCompatActivity implements UpdateFragmentListener{
    public enum Tab { FRIENDS, MESSAGES, PHOTO, PROFILE }

    private static final int LOGIN_ACTIVITY_R_CODE = 1;
    private static Tab DEFAULT_TAB = Tab.PROFILE;

    @BindView(R.id.bottomNavigationView) BottomNavigationView bottomNavigation;

    @Override
    public void updateFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.tabLayout, fragment)
                .commit();
    }

    @Override
    public void updateFragmentWithBackStack(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(R.id.tabLayout, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        ButterKnife.bind(this);
        setupBottomNavigatorListener();
        checkLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_ACTIVITY_R_CODE){
            if (resultCode == RESULT_CANCELED) {
                finish();
            }
            else if (resultCode == RESULT_OK) {
                setupDefaultFragment();
            }
        }
    }

    private void setupBottomNavigatorListener() {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_profile:
                        String profileId = VKAccessToken.currentToken().userId;
                        updateFragment(ProfileFragment.newInstance(profileId));
                        break;
                }
                return true;
            }
        });
    }

    private void checkLogin(){
        if (!VKSdk.isLoggedIn()) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_ACTIVITY_R_CODE);
        } else {
            setupDefaultFragment();
        }
    }

    private void setupDefaultFragment() {
        switch (DEFAULT_TAB) {
            case PROFILE:
                bottomNavigation.setSelectedItemId(R.id.action_profile);
                break;
        }
    }
}