package com.example.miraj.blakittest.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.miraj.blakittest.R;
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
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EditInfoActivity extends AppCompatActivity {
    public static final String ARG_ID = "profileId";

    private static final int DIALOG_DATE = 1;
    private static final Locale LOCALE = Locale.ENGLISH;

    protected VKApiUserFull user;

    DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", LOCALE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.edit);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);

        findViewById(R.id.bdateSpinner).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                showDialog(DIALOG_DATE);
                return true;
            }
        });

        loadUser(getIntent().getIntExtra(ARG_ID, 0));
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_DATE:
                DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        ((Spinner) findViewById(R.id.bdateSpinner)).setAdapter(
                                new ArrayAdapter<>(
                                        EditInfoActivity.this,
                                        R.layout.support_simple_spinner_dropdown_item,
                                        new String[] {String.format(LOCALE, "%d.%d.%d", day, month, year)})
                        );
                    }
                };

                try {
                    Calendar calendar = new GregorianCalendar();
                    calendar.setTime(dateFormat.parse(user.bdate));
                    return new DatePickerDialog(this, listener,
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DATE));
                } catch (ParseException e) {}
        }
        return super.onCreateDialog(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_edit_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                saveUser();
                break;
            case android.R.id.home:
                finish();
                break;
        }

        return true;
    }

    protected void loadUser(int profileId) {
        String[] fields = new String[] {
                VKApiUserFull.BDATE, VKApiUserFull.CITY, VKApiUserFull.SEX,
                VKApiUserFull.RELATION, VKApiUserFull.FIELD_PHOTO_200
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
                EditInfoActivity.this.user = (VKApiUserFull) ((VKList) response.parsedModel).get(0);
                updateView();
            }
        });
    }

    protected void saveUser() {
        VKRequest request = new VKRequest(
                "account.saveProfileInfo",
                VKParameters.from(
                        VKApiUserFull.SEX, getSex(),
                        VKApiUserFull.BDATE, ((Spinner) findViewById(R.id.bdateSpinner)).getSelectedItem().toString(),
                        VKApiUserFull.RELATION, ((Spinner) findViewById(R.id.familyStatusSpinner)).getSelectedItemId()
                ));

        request.executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                finish();
            }
        });
    }

    protected int getSex() {
        if (((RadioButton) findViewById(R.id.maleRadioButton)).isChecked())
            return VKApiUserFull.Sex.MALE;
        if (((RadioButton) findViewById(R.id.femaleRadioButton)).isChecked())
            return VKApiUserFull.Sex.FEMALE;
        return 0;
    }

    protected void updateView() {
        ((TextView) findViewById(R.id.nameText)).setText(String.format("%s %s", user.first_name, user.last_name));

        if (user.sex == VKApiUserFull.Sex.MALE)
            ((RadioButton) findViewById(R.id.maleRadioButton)).setChecked(true);
        else if (user.sex == VKApiUserFull.Sex.FEMALE)
            ((RadioButton) findViewById(R.id.femaleRadioButton)).setChecked(true);

        ((Spinner) findViewById(R.id.bdateSpinner)).setAdapter(
                new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, new String[] {user.bdate})
        );

        Spinner familyStatusSpinner = findViewById(R.id.familyStatusSpinner);
        familyStatusSpinner.setAdapter(
                new ArrayAdapter<>(
                        this,
                        R.layout.support_simple_spinner_dropdown_item,
                        VKHelper.getAvailableFamilyStatuses(this, user))
        );
        familyStatusSpinner.setSelection(user.relation);

        updatePhoto();
    }

    protected void updatePhoto() {
        Picasso.with(this)
                .load(user.photo_200)
                .transform(new CircleTransformation())
                .into((ImageView) findViewById(R.id.image));
    }
}
