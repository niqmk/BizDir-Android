package id.bizdir.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import id.bizdir.R;
import id.bizdir.modelhelper.AnggotaHelper;
import id.bizdir.model.Anggota;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;


public class VerificationFormNotMemberActivity extends AppCompatActivity {

    //private CharSequence mTitle;
    private Anggota memberKadin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_form_not_member);
        Helpers.setLockOrientation(VerificationFormNotMemberActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getDataFromPreviousPage();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Helpers.setMainActionBar(actionBar);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void getDataFromPreviousPage() {
        Intent intent = this.getIntent();
        int objectIndex = intent.getIntExtra(Const.OBJECT_INDEX, 0);
        if (objectIndex != 0) {
            AnggotaHelper memberKadinHelper = new AnggotaHelper();
            memberKadin = memberKadinHelper.get(objectIndex);
            if (memberKadin != null) {
                //mTitle = memberKadin.getName();
            }
        }
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
