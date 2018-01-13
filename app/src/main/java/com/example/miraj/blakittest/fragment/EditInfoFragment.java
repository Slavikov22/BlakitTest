package com.example.miraj.blakittest.fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.example.miraj.blakittest.R;
import com.example.miraj.blakittest.fragment.dialog.DatePickerFragment;
import com.example.miraj.blakittest.helper.VKHelper;
import com.example.miraj.blakittest.helper.image.CircleTransformation;
import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditInfoFragment extends Fragment {
    private static final String DATE_PICKER_FRAGMENT = "datePickerFragment";
    private static final String SAVE_PROFILE_INFO_METHOD = "account.saveProfileInfo";
    private static final Locale LOCALE = Locale.ENGLISH;

    private VKApiUserFull user;
    private DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", LOCALE);

    @BindView(R.id.photoImage) ImageView photoImage;
    @BindView(R.id.nameText) TextView nameText;
    @BindView(R.id.maleRadioButton) RadioButton maleRadioButton;
    @BindView(R.id.femaleRadioButton) RadioButton femaleRadioButton;
    @BindView(R.id.bdateText) TextView bdateText;
    @BindView(R.id.familyStatusSpinner) Spinner familyStatusSpinner;
    @BindView(R.id.toolbar) Toolbar toolbar;

    public EditInfoFragment() {}

    public static EditInfoFragment newInstance() {
        return new EditInfoFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_info, container, false);
        ButterKnife.bind(this, view);
        loadUser();
        setupToolbar();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.edit_info_toolbar_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveUser();
                break;
            case android.R.id.home:
                closeFragment();
                break;
        }
        return true;
    }

    @OnClick(R.id.bdateText)
    public void showDataDialogFragment() {
        Calendar calendar = getBdateCalendar();
        DatePickerFragment fragment = DatePickerFragment.newInstance(calendar);
        fragment.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker,int year, int month, int day) {
                bdateText.setText(String.format(LOCALE, "%d.%d.%d", day, month, year));
            }
        });
        fragment.show(getActivity().getSupportFragmentManager(), DATE_PICKER_FRAGMENT);
    }

    private void setupToolbar() {
        toolbar.setTitle(R.string.edit);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
    }

    private void saveUser() {
        VKRequest request = new VKRequest(SAVE_PROFILE_INFO_METHOD,
                VKParameters.from(
                        VKApiUserFull.SEX, getChosenSex(),
                        VKApiUserFull.BDATE, bdateText.getText(),
                        VKApiUserFull.RELATION, familyStatusSpinner.getSelectedItemId()
                ));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                closeFragment();
            }
        });
    }

    private void loadUser() {
        String fields = VKHelper.getVKUserFieldsAsString(
                VKApiUserFull.BDATE, VKApiUserFull.CITY, VKApiUserFull.SEX,
                VKApiUserFull.RELATION, VKApiUserFull.FIELD_PHOTO_200
        );
        VKRequest request = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, fields));
        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                user = (VKApiUserFull) ((VKList) response.parsedModel).get(0);
                updateUserView();
            }
        });
    }

    private void updateUserView() {
        updateUserName();
        updateUserImage();
        updateUserSex();
        updateUserBdate();
        updateFamilyStatus();
    }

    private void updateUserName() {
        String name = String.format("%s %s", user.first_name, user.last_name);
        nameText.setText(name);
    }

    private void updateUserImage() {
        Picasso.with(getActivity())
                .load(user.photo_200)
                .transform(new CircleTransformation())
                .into(photoImage);
    }

    private void updateUserSex() {
        if (user.sex == VKApiUserFull.Sex.MALE)
            maleRadioButton.setChecked(true);
        else
            femaleRadioButton.setChecked(true);
    }

    private void updateUserBdate() {
        bdateText.setText(user.bdate);
    }

    private void updateFamilyStatus() {
        SpinnerAdapter adapter = new ArrayAdapter<>(
                getActivity(),
                R.layout.spinner_item,
                VKHelper.getAvailableFamilyStatuses(getContext(), user)
        );
        familyStatusSpinner.setAdapter(adapter);
        familyStatusSpinner.setSelection(user.relation);
    }

    private Calendar getBdateCalendar() {
        Calendar calendar = new GregorianCalendar();
        try {
            String bdate = bdateText.getText().toString();
            calendar.setTime(dateFormat.parse(bdate));
        }
        catch (ParseException e) {}
        return calendar;
    }

    private int getChosenSex() {
        if (maleRadioButton.isChecked())
            return VKApiUserFull.Sex.MALE;
        else
            return VKApiUserFull.Sex.FEMALE;
    }

    private void closeFragment() {
        getActivity().getSupportFragmentManager().popBackStack();
    }
}
