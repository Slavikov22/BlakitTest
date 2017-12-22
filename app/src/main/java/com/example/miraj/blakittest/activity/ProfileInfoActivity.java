package com.example.miraj.blakittest.activity;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;

import com.example.miraj.blakittest.R;
import com.example.miraj.blakittest.fragment.InfoBlockFragment;
import com.example.miraj.blakittest.helper.VKHelper;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiSchool;
import com.vk.sdk.api.model.VKApiUniversity;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ProfileInfoActivity extends AppCompatActivity {
    public static final String ARG_ID = "profileId";

    protected VKApiUserFull user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_info);

        loadUser(getIntent().getIntExtra(ARG_ID, 0));
    }

    protected void loadUser(int profileId) {
        String[] fields = new String[] {
                VKApiUserFull.ACTIVITY, VKApiUserFull.ACTIVITIES, VKApiUserFull.ABOUT,
                VKApiUserFull.BDATE, VKApiUserFull.BOOKS, VKApiUserFull.CITY,
                VKApiUserFull.GAMES, VKApiUserFull.INTERESTS, VKApiUserFull.MOVIES,
                VKApiUserFull.CONTACTS, VKApiUserFull.PERSONAL, VKApiUserFull.UNIVERSITIES,
                VKApiUserFull.SCHOOLS, VKApiUserFull.SITE, VKApiUserFull.OCCUPATION,
                VKApiUserFull.TV, VKApiUserFull.QUOTES, VKApiUserFull.SEX,
                VKApiUserFull.RELATION, VKApiUserFull.OCCUPATION, VKApiUserFull.RELATIVES,
                VKApiUserFull.CONNECTIONS, VKApiUserFull.COUNTERS
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
                ProfileInfoActivity.this.user = (VKApiUserFull) ((VKList) response.parsedModel).get(0);

                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.infoBlockContainer, getMainBlockFragment())
                        .add(R.id.infoBlockContainer, getContactBlockFragment())
                        .add(R.id.infoBlockContainer, getEducationBlockFragment())
                        .add(R.id.infoBlockContainer, getPersonalInfoBlockFragment())
                        .commit();
            }
        });
    }

    protected Fragment getMainBlockFragment() {
        ArrayList<Pair<String, String>> fields = new ArrayList<>();
        fields.add(new Pair<>(getString(R.string.status), user.activity));
        fields.add(new Pair<>(getString(R.string.birth_day), user.bdate));
        fields.add(new Pair<>(getString(R.string.family_status), VKHelper.getFamilyStatus(this, user)));

        return InfoBlockFragment.newInstance(null, fields);
    }

    protected Fragment getContactBlockFragment() {
        ArrayList<Pair<String, String>> fields = new ArrayList<>();
        fields.add(new Pair<>(getString(R.string.city), user.city.title));
        fields.add(new Pair<>(getString(R.string.phone_number), user.mobile_phone));
        fields.add(new Pair<>(getString(R.string.opt_phone_number), user.home_phone));
        fields.add(new Pair<>(getString(R.string.skype), user.skype));
        fields.add(new Pair<>(getString(R.string.status), user.activity));
        fields.add(new Pair<>(getString(R.string.website), user.site));

        return InfoBlockFragment.newInstance(getString(R.string.contact_info), fields);
    }

    protected Fragment getEducationBlockFragment() {
        ArrayList<Pair<String, String>> fields = new ArrayList<>();

        for (VKApiSchool school : user.schools)
            fields.add(new Pair<>(getString(R.string.school), school.name));

        for (VKApiUniversity university : user.universities)
            fields.add(new Pair<>(getString(R.string.university), university.name));

        return InfoBlockFragment.newInstance(getString(R.string.education), fields);
    }

    protected Fragment getPersonalInfoBlockFragment() {
        ArrayList<Pair<String, String>> fields = new ArrayList<>();
        fields.add(new Pair<>(getString(R.string.activities), user.activities));
        fields.add(new Pair<>(getString(R.string.interests), user.interests));
        fields.add(new Pair<>(getString(R.string.favorite_films), user.movies));
        fields.add(new Pair<>(getString(R.string.favorite_books), user.books));
        fields.add(new Pair<>(getString(R.string.favorite_games), user.games));
        fields.add(new Pair<>(getString(R.string.favorite_quotes), user.quotes));
        fields.add(new Pair<>(getString(R.string.about_yourself), user.about));

        return InfoBlockFragment.newInstance(getString(R.string.personal_info), fields);
    }
}
