package id.bizdir.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rey.material.widget.Button;

import java.io.IOException;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.modelhelper.InterestHelper;
import id.bizdir.modelhelper.MemberBizdirHelper;
import id.bizdir.model.MemberBizdir;
import id.bizdir.model.ResultObject;
import id.bizdir.model.ResultObjectHelper;
import id.bizdir.service.LoginService;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Helpers.setLockOrientation(LoginActivity.this);
        setBindButton();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    private void setBindButton() {
        final Button buttonRegister = (Button) findViewById(R.id.buttonRegister);
        final Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        final Button buttonCantAccess = (Button) findViewById(R.id.buttonCantAccess);
        final Button buttonSkip = (Button) findViewById(R.id.buttonSkip);
        final EditText editPassword = (EditText) findViewById(R.id.editPassword);
        editPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login();
                }
                return false;
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegister();
            }
        });
        buttonCantAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showResetPasswordDialog();
            }
        });
        buttonSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMainMenu();
            }
        });
    }

    private void login() {
        final EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        final EditText editPassword = (EditText) findViewById(R.id.editPassword);

        String email = editTextEmail.getText().toString();
        String password = editPassword.getText().toString();

        String errorEmail = "";
        String errorPassword = "";

        boolean isPass = true;

        if (TextUtils.isEmpty(email)) {
            errorEmail = getResources().getString(R.string.register_email_cannot_empty) + "\n";
            isPass = false;
        } else {
            if (!Helpers.isValidEmail(email)) {
                errorEmail = getResources().getString(R.string.register_email_format_invalid) + "\n";
                isPass = false;
            }
        }

        if (TextUtils.isEmpty(password)) {
            errorPassword = getResources().getString(R.string.register_password_cannot_empty) + "\n";
            isPass = false;
        }

        if (isPass) {
            boolean isavailable = Helpers.isInternetConnected(LoginActivity.this);
            if (isavailable) {
                hitLoginWs(email, password);
            } else {
                new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                        .title(R.string.dialog_login_failed)
                        .content(R.string.no_internet_connection)
                        .positiveText(R.string.button_ok)
                        .show();
            }
        } else {
            String errorMessage = errorEmail + errorPassword;
            errorMessage = errorMessage.substring(0, errorMessage.length() - 2);
            new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title(R.string.dialog_login_failed)
                    .content(errorMessage)
                    .positiveText(R.string.button_ok)
                    .show();
        }
    }

    private void hitLoginWs(String emailAddress, String password) {

        class LoginAsyncTask extends AsyncTask<String, Void, String> {
            String response = "";
            final MaterialDialog progressDialog = new MaterialDialog.Builder(
                    new ContextThemeWrapper(LoginActivity.this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title("Loading")
                    .content("Please wait...")
                    .progress(true, 0)
                    .cancelable(false)
                    .show();

            @Override
            protected String doInBackground(String... param) {
                String emailAddress, password;
                emailAddress = param[0];
                password = param[1];
                response = "";
                try {
                    LoginService loginService = new LoginService();
                    response = loginService.getLogin(emailAddress, password);
                } catch (IOException e) {
                    response = e.getMessage();
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(String resultJson) {
                progressDialog.dismiss();
                if (!TextUtils.isEmpty(resultJson)) {
                    try {
                        ResultObject resultObject = ResultObjectHelper.getResult(resultJson);
                        onSuccessHitWs(resultObject);
                    } catch (Exception ignore) {
                    }
                }
            }
        }
        String[] myTaskParams = {emailAddress, password};
        LoginAsyncTask task = new LoginAsyncTask();
        task.execute(myTaskParams);
    }

    private void resetPasswordWs(String emailAddress) {

        class MyTask extends AsyncTask<String, Void, String> {
            String response = "";
            final MaterialDialog progressDialog = new MaterialDialog.Builder(
                    new ContextThemeWrapper(LoginActivity.this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title("Loading")
                    .content("Please wait...")
                    .progress(true, 0)
                    .cancelable(false)
                    .show();

            @Override
            protected String doInBackground(String... param) {
                String emailAddress = param[0];
                response = "";
                try {
                    LoginService loginService = new LoginService();
                    response = loginService.setResetPassword(emailAddress);
                } catch (IOException e) {
                    response = e.getMessage();
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(String resultJson) {
                progressDialog.dismiss();
                if (!TextUtils.isEmpty(resultJson)) {
                    try {
                        ResultObject resultObject = ResultObjectHelper.getResult(resultJson);
                        onSuccessResetWs(resultObject);
                    } catch (Exception ignore) {

                    }
                }

            }
        }
        String[] myTaskParams = {emailAddress};
        MyTask task = new MyTask();
        task.execute(myTaskParams);
    }

    private void onSuccessResetWs(ResultObject resultObject) {
        if (resultObject != null) {
            int status = resultObject.getStatus();
            if (status == 1) {
                String title = getResources().getString(R.string.dialog_reset_password);
                String content = getResources().getString(R.string.dialog_reset_password_message);
                Helpers.showDialog(this, title, content);
            } else {
                if (!TextUtils.isEmpty(resultObject.getMessage())) {
                    String title = getResources().getString(R.string.dialog_reset_password_failed);
                    Helpers.showDialog(this, title, resultObject.getMessage());
                }
            }
        }
    }

    private void onSuccessHitWs(ResultObject resultObject) {
        if (resultObject != null) {
            int status = resultObject.getStatus();
            if (status == 1) {
                //Log.i("Status ==>", status.toString() + "<==");
                //Log.i("Message ==>", resultObject.getMessage() + "<==");
                //Log.i("Result ==>", resultObject.getResult() + "<==");
                String jsonString = resultObject.getResult();
                try {
                    MemberBizdir memberBizdir = App.getGson().fromJson(jsonString,
                            MemberBizdir.class);
                    if (memberBizdir != null) {
                        MemberBizdirHelper helper = new MemberBizdirHelper();
                        InterestHelper interestHelper = new InterestHelper();
                        interestHelper.clearAll();
                        helper.clearAll();
                        helper.add(memberBizdir);
                        gotoMainMenu();
                    }
                } catch (Exception ex) {
                    String title = getResources().getString(R.string.dialog_login_failed);
                    Helpers.showDialog(this, title, ex.getMessage());
                }
            } else {
                if (!TextUtils.isEmpty(resultObject.getMessage())) {
                    String title = getResources().getString(R.string.dialog_login_failed);
                    Helpers.showDialog(this, title, resultObject.getMessage());
                }

            }
        }
    }

    private void gotoRegister() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void gotoMainMenu() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void showResetPasswordDialog() {
        new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                .title(R.string.action_reset_password)
                .content(R.string.action_reset_password_message)
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                        //InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        // InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputMaxLength(Const.MAX_CHAR_EMAIL_ADDRESS)
                .positiveText(R.string.action_submit)
                .input(R.string.member_detail_contact_email_address, 0,
                        false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                //showToast("Hello, " + input.toString() + "!");
                                submitResetPassword(input.toString());
                            }
                        }).show();
    }

    private void submitResetPassword(String emailAddress) {
        if (!TextUtils.isEmpty(emailAddress)) {
            if (Helpers.isValidEmail(emailAddress)) {
                //hit ws reset password
                boolean isavailable = Helpers.isInternetConnected(LoginActivity.this);
                if (isavailable) {
                    resetPasswordWs(emailAddress);
                } else {
                    new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                            .title(R.string.dialog_reset_password_failed)
                            .content(R.string.no_internet_connection)
                            .positiveText(R.string.button_ok)
                            .show();
                }
            } else {
                String title = getResources().getString(R.string.dialog_reset_password_failed);
                String content = getResources().getString(R.string.register_email_format_invalid);
                Helpers.showDialog(this, title, content);
            }
        }
    }


    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
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
