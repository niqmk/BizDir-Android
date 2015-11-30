package id.bizdir.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rey.material.app.DatePickerDialog;
import com.rey.material.app.Dialog;
import com.rey.material.app.DialogFragment;
import com.rey.material.widget.Button;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.modelhelper.CommonHelper;
import id.bizdir.modelhelper.InterestHelper;
import id.bizdir.modelhelper.MemberBizdirHelper;
import id.bizdir.model.Common;
import id.bizdir.model.Interest;
import id.bizdir.model.MemberBizdir;
import id.bizdir.model.ResultObject;
import id.bizdir.model.ResultObjectHelper;
import id.bizdir.service.ProfileService;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;

public class ProfileActivity extends AppCompatActivity {

    private MemberBizdir memberBizdir;
    private EditText editBirthday;
    private EditText editInterest;
    List<Interest> interestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_detail);
        Helpers.setLockOrientation(ProfileActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Helpers.setMainActionBar(actionBar);
        }
        setBindButton();
        getData();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void getData() {
        memberBizdir = new MemberBizdir();
        MemberBizdirHelper memberBizdirHelper = new MemberBizdirHelper();
        memberBizdir = memberBizdirHelper.get();
        if (memberBizdir != null) {
            interestList = new ArrayList<>(memberBizdir.getInterest());
            bindDataForm();
        }
    }

    private void bindDataForm() {
        final EditText editFullName = (EditText) findViewById(R.id.editFullName);
        final EditText editEmail = (EditText) findViewById(R.id.editEmail);
        final EditText editNoKta = (EditText) findViewById(R.id.editNoKta);
        final EditText editBizDirId = (EditText) findViewById(R.id.editBizDirId);
        final EditText editPhone = (EditText) findViewById(R.id.editPhone);

        editFullName.setText(memberBizdir.getName());
        editEmail.setText(memberBizdir.getEmail());
        setGender(memberBizdir.getGender());
        editNoKta.setText(memberBizdir.getNokta());
        editBizDirId.setText(memberBizdir.getBizdirid());
        editPhone.setText(memberBizdir.getPhone());
        setEditTextBirthday();
        setInterest();
    }

    private void setInterest() {
        if (interestList.size() > 0) {
            final EditText editInterest = (EditText) findViewById(R.id.editInterest);
            String selectedInterest = "";
            //List<Interest> interestList = new ArrayList<>(memberBizdir.getInterest());
            for (Interest obj : interestList) {
                if (obj.getStatus() == 1) {
                    if (selectedInterest.equals("")) {
                        selectedInterest = obj.getName();
                    } else {
                        selectedInterest = selectedInterest + ", " + obj.getName();
                    }
                }
            }
            editInterest.setText(selectedInterest);
        }
    }

    private void updateMemberBizdir() {
        final EditText editFullName = (EditText) findViewById(R.id.editFullName);
        final RadioButton radioFemale = (RadioButton) findViewById(R.id.radioFemale);
        final EditText editPhone = (EditText) findViewById(R.id.editPhone);
        final EditText editNoKta = (EditText) findViewById(R.id.editNoKta);

        memberBizdir.setName(editFullName.getText().toString());
        if (radioFemale.isChecked()) {
            memberBizdir.setGender(1);
        } else {
            memberBizdir.setGender(0);
        }
        memberBizdir.setPhone(editPhone.getText().toString());
        memberBizdir.setNokta(editNoKta.getText().toString());
        String selectedInterest = "";
        if (interestList.size() > 0) {
            for (Interest obj : interestList) {
                if (obj.getStatus() == 1) {
                    Integer id = obj.getId();
                    if (selectedInterest.equals("")) {
                        selectedInterest = id.toString();
                    } else {
                        selectedInterest = selectedInterest + ", " + id.toString();
                    }
                }
            }
        }
        boolean isavailable = Helpers.isInternetConnected(ProfileActivity.this);
        if (isavailable) {
            updateUserWs(selectedInterest);
        } else {
            new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title(R.string.dialog_update_failed)
                    .content(R.string.no_internet_connection)
                    .positiveText(R.string.button_ok)
                    .show();
        }
    }

    private void updateUserWs(String interest) {

        class MyTask extends AsyncTask<String, Void, String> {
            String response = "";
            final MaterialDialog progressDialog = new MaterialDialog.Builder(
                    new ContextThemeWrapper(ProfileActivity.this,
                            R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title("Loading")
                    .content("Please wait...")
                    .progress(true, 0)
                    .cancelable(false)
                    .show();

            @Override
            protected String doInBackground(String... param) {
                String idMember = param[0];
                String fullName = param[1];
                String phone = param[2];
                String gender = param[3];
                String birthday = param[4];
                String noKTA = param[5];
                String interest = param[6];
                response = "";
                try {
                    ProfileService service = new ProfileService();
                    response = service.updateMember(idMember, fullName, phone, gender,
                            birthday, noKTA, interest);
                } catch (IOException e) {
                    response = e.getMessage();
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(String resultJson) {
                progressDialog.dismiss();
                ResultObject resultObject = ResultObjectHelper.getResult(resultJson);
                if (resultObject != null) {
                    int status = resultObject.getStatus();
                    if (status == 1) {
                        String jsonString = resultObject.getResult();
                        MemberBizdir newMemberBizdir = App.getGson().fromJson(jsonString,
                                MemberBizdir.class);
                        if (newMemberBizdir != null) {
                            MemberBizdirHelper helper = new MemberBizdirHelper();
                            helper.add(newMemberBizdir);
                            getData();
                            String title = getResources().getString(R.string.dialog_update);
                            String content = getResources().getString(R.string.dialog_update_message);
                            Helpers.showDialog(ProfileActivity.this, title, content);
                        }
                    } else {
                        String title = getResources().getString(R.string.dialog_update_failed);
                        String content = getResources().getString(R.string.dialog_update_failed_message);
                        Helpers.showDialog(ProfileActivity.this, title, content);
                    }
                }
            }
        }

        Integer idMember = memberBizdir.getId();
        Integer getGender = memberBizdir.getGender();
        String[] myTaskParams = {
                idMember.toString(),
                memberBizdir.getName(),
                memberBizdir.getPhone(),
                getGender.toString(),
                Helpers.DateToString(memberBizdir.getBirthday()),
                memberBizdir.getNokta(),
                interest
        };
        MyTask task = new MyTask();
        task.execute(myTaskParams);
    }

    private void setGender(int gender) {
        final RadioButton radioMale = (RadioButton) findViewById(R.id.radioMale);
        final RadioButton radioFemale = (RadioButton) findViewById(R.id.radioFemale);
        if (gender == 0) {
            radioMale.setChecked(true);
            radioFemale.setChecked(false);
        } else if (gender == 1) {
            radioMale.setChecked(false);
            radioFemale.setChecked(true);
        } else {
            radioMale.setChecked(false);
            radioFemale.setChecked(false);
        }
    }

    private void setBirthdayObject(Date dateBirthday) {
        memberBizdir.setBirthday(dateBirthday);
        editBirthday.setText(Helpers.DateToStringDate(dateBirthday));
    }

    private void setEditTextBirthday() {
        Date selectedDate = memberBizdir.getBirthday();
        if (selectedDate != null) {
            String textBirthday = Helpers.DateToStringDate(selectedDate);
            if (!TextUtils.isEmpty(textBirthday)) {
                editBirthday.setText(textBirthday);
            }
        }
    }

    private void setBindButton() {
        final Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        editBirthday = (EditText) findViewById(R.id.editBirthday);
        editInterest = (EditText) findViewById(R.id.editInterest);
        EditText editBizDirId = (EditText) findViewById(R.id.editBizDirId);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });

        editBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        editBirthday.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showDatePickerDialog();
                }
            }
        });

        editInterest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInterest();
            }
        });

        editInterest.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showInterest();
                }
            }
        });

        editBizDirId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBizDirIdInfo();
            }
        });

        editBizDirId.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showBizDirIdInfo();
                }
            }
        });

    }

    private void showBizDirIdInfo() {
        CommonHelper commonHelper = new CommonHelper();
        Common common = commonHelper.getBizDirId();
        String bizdirInfo = common.getDescription();
        final String bizdirId = memberBizdir.getBizdirid();
        if (!TextUtils.isEmpty(bizdirId)) {
            new MaterialDialog.Builder(this)
                    .title("BizDir ID: " + memberBizdir.getBizdirid())
                    .content(bizdirInfo)
                    .positiveText(R.string.button_ok)
                    .negativeText(R.string.button_copy)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onNegative(MaterialDialog dialog) {
                            Helpers.setClipboard(bizdirId);
                            Toast.makeText(ProfileActivity.this,
                                    Helpers.getString(R.string.dialog_bizdirid_success_copy),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }).build().show();
        } else {
            new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title(R.string.dialog_bizdirid)
                    .content(R.string.dialog_bizdirid_not_available)
                    .positiveText(R.string.button_ok)
                    .show();
        }

    }

    private void showInterest() {
        List<Integer> selectedInterestList = new ArrayList<>();
        if (interestList.size() > 0) {
            //List<Interest> interestList = new ArrayList<>(memberBizdir.getInterest());
            int index = 0;
            for (Interest obj : interestList) {
                if (obj.getStatus() == 1) {
                    selectedInterestList.add(index);
                }
                index++;
            }
        }
        Integer[] selectedInterest = Helpers.toIntArray(selectedInterestList);

        InterestHelper interestHelper = new InterestHelper();
        CharSequence[] interestListDialog = interestHelper.getCharSequence();
        new MaterialDialog.Builder(this)
                .title(R.string.user_interest)
                .items(interestListDialog)
                .itemsCallbackMultiChoice(selectedInterest, new MaterialDialog.ListCallbackMultiChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, Integer[] which, CharSequence[] text) {
                        //StringBuilder str = new StringBuilder();
                        for (int i = 0; i < interestList.size(); i++) {
                            interestList.get(i).setStatus(0);
                        }
                        for (int i = 0; i < which.length; i++) {
                            //if (i > 0) str.append('\n');
                            //str.append(which[i]);
                            //str.append(": ");
                            //str.append(text[i]);
                            interestList.get(which[i]).setStatus(1);
                        }
                        //showToast(str.toString());
                        setInterest();
                        return true; // allow selection
                    }
                })
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNeutral(MaterialDialog dialog) {
                        dialog.clearSelectedIndices();
                    }
                })
                .alwaysCallMultiChoiceCallback()
                .positiveText(R.string.button_choose)
                .neutralText(R.string.clear_selection)
                .show();
    }

    private void submitData() {
        final EditText editFullName = (EditText) findViewById(R.id.editFullName);
        final EditText editNoKta = (EditText) findViewById(R.id.editNoKta);
        String fullName = editFullName.getText().toString();
        String noKta = editNoKta.getText().toString();
        boolean isPass = true;
        if (TextUtils.isEmpty(fullName)) {
            isPass = false;
            new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title(R.string.dialog_registration_failed)
                    .content(R.string.register_fullname_cannot_empty)
                    .positiveText(R.string.button_ok)
                    .show();
        }
        if (!TextUtils.isEmpty(noKta)) {
            if (!Helpers.isValidKtaNo(noKta)) {
                isPass = false;
                new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                        .title(R.string.dialog_registration_failed)
                        .content(R.string.register_no_kta_not_valid)
                        .positiveText(R.string.button_ok)
                        .show();
            }
        }
        if (isPass) {
            updateMemberBizdir();
        }
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int nowYear = calendar.get(Calendar.YEAR);

        DatePickerDialog.Builder dateBuilder = new DatePickerDialog.Builder() {
            @Override
            public void onPositiveActionClicked(DialogFragment fragment) {
                DatePickerDialog dialog = (DatePickerDialog) fragment.getDialog();
                //String date = dialog.getFormattedDate(SimpleDateFormat.getDateInstance());
                DateFormat format = new SimpleDateFormat(Const.DATE_FORMAT, Locale.ENGLISH);
                String dateStr = dialog.getFormattedDate(format);
                Date date = Helpers.StringToDateOnly(dateStr);
                //Date date = dialog.getCalendar().getTime();
                //String strDate = Helpers.DateToStringDate(date);
                //Toast.makeText(ProfileActivity.this, dateStr, Toast.LENGTH_SHORT).show();
                setBirthdayObject(date);
                super.onPositiveActionClicked(fragment);
            }

            @Override
            public void onNegativeActionClicked(DialogFragment fragment) {
                // Toast.makeText(ProfileActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                super.onNegativeActionClicked(fragment);
            }
        }.dateRange(1, 1, 1940, 31, 12, nowYear);

        Date selectedDate = memberBizdir.getBirthday();
        if (selectedDate != null) {
            Calendar selectedCalendar = new GregorianCalendar();
            selectedCalendar.setTime(selectedDate);
            int year = selectedCalendar.get(Calendar.YEAR);
            int month = selectedCalendar.get(Calendar.MONTH);
            int day = selectedCalendar.get(Calendar.DAY_OF_MONTH);
            dateBuilder.date(day, month, year);
        }

        Dialog.Builder builder = dateBuilder;
        builder.positiveAction("OK")
                .negativeAction("CANCEL");

        DialogFragment fragment = DialogFragment.newInstance(builder);
        fragment.show(getSupportFragmentManager(), null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_home:
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
