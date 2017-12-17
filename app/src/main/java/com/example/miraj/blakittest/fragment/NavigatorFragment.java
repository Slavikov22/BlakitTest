package com.example.miraj.blakittest.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.miraj.blakittest.R;

public class NavigatorFragment
        extends Fragment
        implements ChangeTab{
    public enum Tab { WALL, MESSAGES, MENU }

    private static final String ARG_CALL_ACTIVITY = "callActivity";

    private ChangeTab changeTabListener;
    private boolean callActivity;

    public NavigatorFragment() {}

    public static NavigatorFragment newInstance(boolean callActivity) {
        NavigatorFragment fragment = new NavigatorFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ARG_CALL_ACTIVITY, callActivity);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callActivity = getArguments().getBoolean(ARG_CALL_ACTIVITY);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_navigator, container, false);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.menuImage:
                        changeTabListener.changeTab(Tab.MENU);
                        break;
                }
            }
        };

        view.findViewById(R.id.menuImage).setOnClickListener(listener);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (callActivity)
            changeTabListener = (ChangeTab) context;
        else
            changeTabListener = this;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        changeTabListener = null;
    }

    public void changeTab(Tab tab) {

    }
}
