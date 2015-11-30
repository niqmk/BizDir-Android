package id.bizdir.ui.activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rey.material.widget.Button;

import java.io.IOException;

import id.bizdir.R;
import id.bizdir.model.ResultObject;
import id.bizdir.model.ResultObjectHelper;
import id.bizdir.service.RegisterService;
import id.bizdir.util.Helpers;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Helpers.setLockOrientation(RegisterActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Helpers.setMainActionBar(actionBar);
        }
        setBindButton();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void setBindButton() {
        final Button buttonSubmit = (Button) findViewById(R.id.buttonSubmit);
        final Button buttonAlreadyMember = (Button) findViewById(R.id.buttonAlreadyMember);
        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitData();
            }
        });
        buttonAlreadyMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void submitData() {
        final EditText editFullName = (EditText) findViewById(R.id.editFullName);
        final EditText editEmail = (EditText) findViewById(R.id.editEmail);
        final EditText editPassword = (EditText) findViewById(R.id.editPassword);
        final EditText editPasswordRepeat = (EditText) findViewById(R.id.editPasswordRepeat);

        String errorName = "";
        String errorEmail = "";
        String errorPassword = "";
        String errorPasswordRepeat = "";

        boolean isPass = true;

        String fullName = editFullName.getText().toString();
        String email = editEmail.getText().toString();
        String password = editPassword.getText().toString();
        String passwordRepeat = editPasswordRepeat.getText().toString();

        if (TextUtils.isEmpty(fullName)) {
            errorName = getResources().getString(R.string.register_fullname_cannot_empty) + "\n";
            isPass = false;
        }
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

        if (TextUtils.isEmpty(passwordRepeat)) {
            errorPasswordRepeat = getResources().getString(R.string.register_password_repeat_cannot_empty) + "\n";
            isPass = false;
        } else {
            if (TextUtils.isEmpty(password)) {
                errorPassword = getResources().getString(R.string.register_password_cannot_empty) + "\n";
                isPass = false;
            } else {
                if (!password.equals(passwordRepeat)) {
                    errorPassword = getResources().getString(R.string.register_password_notmatch) + "\n";
                    isPass = false;
                }
            }
        }

        if (isPass) {
            boolean isavailable = Helpers.isInternetConnected(RegisterActivity.this);
            if (isavailable) {
                registerUserWs(fullName, email, password);
            } else {
                new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                        .title(R.string.dialog_registration_failed)
                        .content(R.string.no_internet_connection)
                        .positiveText(R.string.button_ok)
                        .show();
            }
        } else {
            String errorMessage = errorName + errorEmail + errorPassword + errorPasswordRepeat;
            errorMessage = errorMessage.substring(0, errorMessage.length() - 2);
            new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title(R.string.dialog_registration_failed)
                    .content(errorMessage)
                    .positiveText(R.string.button_ok)
                    .show();
        }
    }

    private void registerUserWs(String fullName, String email, String password) {
        String noKTA = "";
        class MyTask extends AsyncTask<String, Void, String> {
            String response = "";
            final MaterialDialog progressDialog = new MaterialDialog.Builder(
                    new ContextThemeWrapper(RegisterActivity.this,
                            R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title("Loading")
                    .content("Please wait...")
                    .progress(true, 0)
                    .cancelable(false)
                    .show();

            @Override
            protected String doInBackground(String... param) {
                String fullName = param[0];
                String email = param[1];
                String password = param[2];
                String noKTA = param[3];
                response = "";
                try {
                    RegisterService service = new RegisterService();
                    response = service.registerMember(fullName, email, password, noKTA);
                } catch (IOException e) {
                    response = e.getMessage();
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(String resultJson) {
                progressDialog.dismiss();
                try {
                    ResultObject resultObject = ResultObjectHelper.getResult(resultJson);
                    if (resultObject != null) {
                        int status = resultObject.getStatus();
                        if (status == 1) {
                            String title = getResources().getString(R.string.dialog_registration);
                            String content = getResources().getString(R.string.dialog_registration_message);
                            //Helpers.showDialog(RegisterActivity.this, title, content);
                            new MaterialDialog.Builder(new ContextThemeWrapper(RegisterActivity.this,
                                    R.style.MaterialDrawerTheme_Light_DarkToolbar))
                                    .title(title)
                                    .content(content)
                                    .positiveText(R.string.button_ok)
                                    .callback(new MaterialDialog.ButtonCallback() {
                                        @Override
                                        public void onPositive(MaterialDialog dialog) {
                                            onBackPressed();
                                        }
                                    })
                                    .show();
                        } else {
                            if (!TextUtils.isEmpty(resultObject.getMessage())) {
                                String title = getResources().getString(R.string.dialog_registration_failed);
                                Helpers.showDialog(RegisterActivity.this, title, resultObject.getMessage());
                            }
                        }
                    }
                } catch (Exception ex) {
                    String title = getResources().getString(R.string.dialog_registration_failed);
                    Helpers.showDialog(RegisterActivity.this, title, ex.getMessage());
                }

            }
        }
        String[] myTaskParams = {fullName, email, password, noKTA};
        MyTask task = new MyTask();
        task.execute(myTaskParams);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
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
