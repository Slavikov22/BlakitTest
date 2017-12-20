package com.example.miraj.blakittest.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.miraj.blakittest.R;
import com.example.miraj.blakittest.activity.ProfileActivity;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.VKUIHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiModel;
import com.vk.sdk.api.model.VKApiUser;
import com.vk.sdk.api.model.VKList;

import org.json.JSONException;
import org.json.JSONObject;

public class MenuFragment extends Fragment {

    public MenuFragment() {}

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_menu, container, false);

        view.findViewById(R.id.profileLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startProfileActivity();
            }
        });

        setProfileInfo();

        return view;
    }

    protected void setProfileInfo() {
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "first_name, last_name"));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                VKApiUser user = (VKApiUser) ((VKList) response.parsedModel).get(0);
                if (getView() != null)
                    ((TextView) getView().findViewById(R.id.userNameText))
                            .setText(String.format("%s %s", user.first_name, user.last_name));
            }
        });
    }

    protected void startProfileActivity() {
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        intent.putExtra(ProfileActivity.ARG_ID, Integer.valueOf(VKAccessToken.currentToken().userId));
        startActivity(intent);
    }
}
