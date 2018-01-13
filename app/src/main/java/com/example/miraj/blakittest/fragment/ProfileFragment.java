package com.example.miraj.blakittest.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.miraj.blakittest.R;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {
    public static final String ARG_PROFILE_ID = "profileId";

    private String profileId;
    private VKApiUserFull user;

    @BindView(R.id.toolbar) Toolbar toolbar;

    public ProfileFragment() {}

    public static ProfileFragment newInstance(String profileId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PROFILE_ID, profileId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        profileId = getArguments().getString(ARG_PROFILE_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        setupToolbar();
        loadUser();
        addDefaultFragments();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeFragment();
                break;
        }
        return true;
    }

    private void setupToolbar() {
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle("");
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    private void loadUser() {
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.USER_ID, profileId));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                user = (VKApiUserFull) ((VKList) response.parsedModel).get(0);
                updateUserView();
            }
        });
    }

    private void updateUserView() {
        updateToolbar();
    }

    private void updateToolbar() {
        String name = String.format("%s %s", user.first_name, user.last_name);
        toolbar.setTitle(name);
    }

    private void addDefaultFragments() {
        for (Fragment fragment : getDefaultFragments())
            addFragment(fragment);
    }

    private List<Fragment> getDefaultFragments() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ProfileInfoFragment.newInstance(profileId));
        return fragments;
    }

    private void addFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        manager.beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit();
    }

    private void closeFragment() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
