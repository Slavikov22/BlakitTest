package com.example.miraj.blakittest.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.miraj.blakittest.R;
import com.example.miraj.blakittest.fragment.ChangeTab;
import com.example.miraj.blakittest.fragment.MenuFragment;
import com.example.miraj.blakittest.fragment.NavigatorFragment;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKList;

public class TabActivity
        extends AppCompatActivity
        implements ChangeTab {
    private static final int LOGIN_ACTIVITY_R_CODE = 1;
    private static final String ARG_TAB = "tab";
    private static final NavigatorFragment.Tab DEFAULT_TAB = NavigatorFragment.Tab.MENU;

    private Fragment tabFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);

        setNavigatorFragment();
        changeTab(DEFAULT_TAB);

        if (!VKSdk.isLoggedIn())
            login();
    }

    @Override
    public void changeTab(NavigatorFragment.Tab tab) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (tabFragment != null)
            transaction.remove(tabFragment);

        switch (tab) {
            case MENU:
                tabFragment = MenuFragment.newInstance();
                break;
            default:
                tabFragment = new Fragment();
        }

        transaction.add(R.id.tabLayout, tabFragment).commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LOGIN_ACTIVITY_R_CODE){
            if (resultCode == RESULT_CANCELED)
                finish();
        }
    }

    protected void setNavigatorFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.navigatorLayout, NavigatorFragment.newInstance(true))
                .commit();
    }

    protected void login(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivityForResult(intent, LOGIN_ACTIVITY_R_CODE);
    }
}
