package com.example.miraj.blakittest.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.miraj.blakittest.R;
import com.example.miraj.blakittest.activity.EditInfoActivity;
import com.example.miraj.blakittest.activity.ProfileInfoActivity;
import com.squareup.picasso.Picasso;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

public class ProfileInfoFragment extends Fragment {
    private static final String ARG_PROFILE_ID = "profileId";

    private VKApiUserFull user;

    public ProfileInfoFragment() {}

    public static ProfileInfoFragment newInstance(int profileId) {
        ProfileInfoFragment fragment = new ProfileInfoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PROFILE_ID, profileId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_info, container, false);

        view.findViewById(R.id.backImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        view.findViewById(R.id.editButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EditInfoActivity.class);
                intent.putExtra(EditInfoActivity.ARG_ID, user.getId());
                startActivity(intent);
            }
        });

        view.findViewById(R.id.showInfoText).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ProfileInfoActivity.class);
                intent.putExtra(ProfileInfoActivity.ARG_ID, user.getId());
                startActivity(intent);
            }
        });

        loadUser(getArguments().getInt(ARG_PROFILE_ID));

        return view;
    }

    protected void loadUser(int profileId) {
        String[] fields = new String[] {
                VKApiUserFull.ACTIVITY, VKApiUserFull.CITY, VKApiUserFull.COUNTERS,
                VKApiUserFull.UNIVERSITIES, VKApiUserFull.FIELD_ONLINE,
                VKApiUserFull.FIELD_PHOTO_400_ORIGIN
        };

        StringBuilder sb = new StringBuilder();
        for (String s : fields)
            sb.append(s).append(",");

        VKRequest request = VKApi.users().get(VKParameters.from(
                VKApiConst.USER_ID, profileId,
                VKApiConst.FIELDS, sb
        ));

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                ProfileInfoFragment.this.user = (VKApiUserFull) ((VKList) response.parsedModel).get(0);
                updateView();
            }
        });
    }

    protected void updateView() {
        updateStatusView();
        updateFriendsCountView();
        updateFollowersCountView();
        updateCityNameView();
        updateEducationNameView();
        updatePhoto();

        if (user.id == Integer.valueOf(VKAccessToken.currentToken().userId))
            if (getView() != null)
                getView().findViewById(R.id.editButton).setVisibility(View.VISIBLE);
    }

    protected void updatePhoto() {
        if (getView() != null)
            Picasso.with(getActivity())
                    .load(user.photo_400_orig)
                    .into((ImageView) getView().findViewById(R.id.photoImage));

        updateNameView();
        updateOnlineView();
    }

    protected void updateNameView() {
        if (getView() != null) {
            ((TextView) getView().findViewById(R.id.nameText))
                    .setText(String.format("%s %s", user.first_name, user.last_name));
        }
    }

    protected void updateOnlineView() {
        if (getView() != null)
            if (user.online)
                ((TextView) getView().findViewById(R.id.onlineText)).setText(R.string.online);
    }

    protected void updateStatusView() {
        if (getView() != null) {
            if (!user.activity.isEmpty()) {
                TextView v = getView().findViewById(R.id.statusText);
                v.setText(user.activity);
                getView().findViewById(R.id.statusLayout).setVisibility(View.VISIBLE);
            }
        }
    }

    protected void updateFriendsCountView () {
        if (getView() != null) {
            TextView v = getView().findViewById(R.id.friendsText);
            v.setText(String.valueOf(user.counters.friends));
            getView().findViewById(R.id.friendsLayout).setVisibility(View.VISIBLE);
        }
    }

    protected void updateFollowersCountView () {
        if (getView() != null) {
            TextView v = getView().findViewById(R.id.followersText);
            v.setText(String.valueOf(user.counters.followers));
            getView().findViewById(R.id.followersLayout).setVisibility(View.VISIBLE);
        }
    }

    protected void updateCityNameView () {
        if (getView() != null) {
            TextView v = getView().findViewById(R.id.cityText);
            v.setText(user.city.title);
            getView().findViewById(R.id.cityLayout).setVisibility(View.VISIBLE);
        }
    }

    protected void updateEducationNameView () {
        if (getView() != null) {
            if (user.universities.getCount() > 0) {
                TextView v = getView().findViewById(R.id.educationText);
                v.setText(user.universities.get(user.universities.getCount() - 1).name);
                getView().findViewById(R.id.educationLayout).setVisibility(View.VISIBLE);
            }
        }
    }
}
