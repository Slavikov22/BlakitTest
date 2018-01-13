package com.example.miraj.blakittest.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miraj.blakittest.R;
import com.example.miraj.blakittest.activity.UpdateFragmentListener;
import com.example.miraj.blakittest.adapter.ProfileInfoAdapter;
import com.example.miraj.blakittest.helper.VKHelper;
import com.squareup.picasso.Picasso;
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
import butterknife.OnClick;

public class ProfileInfoFragment extends Fragment {
    private static final String ARG_PROFILE_ID = "profileId";

    @BindView(R.id.photoImage) ImageView photoImage;
    @BindView(R.id.nameText) TextView nameText;
    @BindView(R.id.onlineText) TextView onlineText;
    @BindView(R.id.statusText) TextView statusText;
    @BindView(R.id.friendsText) TextView friendsText;
    @BindView(R.id.followersText) TextView followersText;
    @BindView(R.id.cityText) TextView cityText;
    @BindView(R.id.educationText) TextView educationText;
    @BindView(R.id.showInfo) TextView showInfo;
    @BindView(R.id.infoList) RecyclerView infoList;

    private String profileId;
    private VKApiUserFull user;
    private UpdateFragmentListener updateFragmentListener;

    public ProfileInfoFragment() {}

    public static ProfileInfoFragment newInstance(String profileId) {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PROFILE_ID, profileId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileId = getArguments().getString(ARG_PROFILE_ID);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_info, container, false);
        ButterKnife.bind(this, view);
        loadUser();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        updateFragmentListener = (UpdateFragmentListener) context;
    }

    @OnClick(R.id.editButton)
    public void displayEditInfoFragment() {
        updateFragmentListener.updateFragmentWithBackStack(EditInfoFragment.newInstance());
    }

    @OnClick(R.id.showInfo)
    public void displayInfoList() {
        if (infoList.getVisibility() == View.VISIBLE) {
            infoList.setVisibility(View.GONE);
            showInfo.setText(getString(R.string.show_info));
        }
        else {
            infoList.setVisibility(View.VISIBLE);
            showInfo.setText(getString(R.string.hide_info));
        }
    }

    private void loadUser() {
        String fields = VKHelper.getVKUserFieldsAsString(
                VKApiUserFull.ACTIVITY, VKApiUserFull.CITY, VKApiUserFull.COUNTERS,
                VKApiUserFull.UNIVERSITIES, VKApiUserFull.FIELD_ONLINE,
                VKApiUserFull.FIELD_PHOTO_400_ORIGIN, VKApiUserFull.BDATE,
                VKApiUserFull.ACTIVITIES, VKApiUserFull.ABOUT, VKApiUserFull.BOOKS,
                VKApiUserFull.GAMES, VKApiUserFull.INTERESTS, VKApiUserFull.MOVIES,
                VKApiUserFull.CONTACTS, VKApiUserFull.PERSONAL,
                VKApiUserFull.SCHOOLS, VKApiUserFull.SITE, VKApiUserFull.OCCUPATION,
                VKApiUserFull.TV, VKApiUserFull.QUOTES, VKApiUserFull.SEX,
                VKApiUserFull.RELATION, VKApiUserFull.OCCUPATION, VKApiUserFull.RELATIVES,
                VKApiUserFull.CONNECTIONS, VKApiUserFull.COUNTERS
        );
        VKRequest request = VKApi.users().get(VKParameters.from(
                VKApiConst.USER_ID, Integer.valueOf(profileId),
                VKApiConst.FIELDS, fields
        ));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                user = (VKApiUserFull) ((VKList) response.parsedModel).get(0);
                updateUserView();
            }
        });
    }

    private void updateUserView() {
        updatePhotoImage();
        updateNameText();
        updateOnlineText();
        updateStatusText();
        updateFriendsText();
        updateFollowersText();
        updateCityText();
        updateEducationText();
        updateInfoFields();
    }

    private void updatePhotoImage() {
        Picasso.with(getActivity())
                .load(user.photo_400_orig)
                .into(photoImage);
    }

    private void updateNameText() {
        nameText.setText(String.format("%s %s", user.first_name, user.last_name));
    }

    private void updateOnlineText() {
        if (user.online)
            onlineText.setText(R.string.online);
    }

    private void updateStatusText() {
        if (!user.activity.isEmpty())
            statusText.setText(user.activity);
        else
            statusText.setVisibility(View.GONE);
    }

    private void updateFriendsText() {
        friendsText.setText(String.valueOf(user.counters.friends));
    }

    private void updateFollowersText() {
        followersText.setText(String.valueOf(user.counters.followers));
    }

    private void updateCityText() {
        cityText.setText(String.valueOf(user.city.title));
    }

    private void updateEducationText() {
        int universitiesCount = user.universities.getCount();
        if (universitiesCount > 0)
            educationText.setText(user.universities.get(universitiesCount - 1).name);
        else
            educationText.setVisibility(View.GONE);
    }

    private void updateInfoFields() {
        List<Pair<String, String>> fields = getUserInfoFields();
        ProfileInfoAdapter adapter = new ProfileInfoAdapter(getContext(), fields);
        infoList.setAdapter(adapter);
    }

    private List<Pair<String, String>> getUserInfoFields() {
        List<Pair<String, String>> fields = new ArrayList<>();
        fields.add(getUserInfoField(R.string.birth_day, user.bdate));
        fields.add(getUserInfoField(R.string.city, user.city.title));
        fields.add(getUserInfoField(R.string.family_status, VKHelper.getFamilyStatus(getContext(), user)));
        fields.add(getUserInfoField(R.string.phone_number, user.mobile_phone));
        fields.add(getUserInfoField(R.string.opt_phone_number, user.home_phone));
        fields.add(getUserInfoField(R.string.skype, user.skype));
        fields.add(getUserInfoField(R.string.website, user.site));
        return fields;
    }

    private Pair<String, String> getUserInfoField(int resource, String text) {
        String title = getString(resource);
        return new Pair<>(title, text);
    }
}
