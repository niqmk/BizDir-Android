package id.bizdir.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.cocosw.bottomsheet.BottomSheet;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import java.io.IOException;
import java.io.Reader;
import java.util.Date;

import id.bizdir.R;
import id.bizdir.model.Opportunity;
import id.bizdir.modelhelper.OpportunityHelper;
import id.bizdir.service.OpportunitiesService;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;


public class BusinessOpportunitiesDetailActivity extends AppCompatActivity {

    private Opportunity opportunity;
    private CharSequence mTitle;
    private ServiceViewCountTask serviceViewCountTask;
    private ServiceEmailCountTask serviceEmailCountTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_opportunities_detail);
        Helpers.setLockOrientation(BusinessOpportunitiesDetailActivity.this);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Helpers.setMainActionBar(actionBar);
        }
        getDataFromPreviousPage();
        setTitle(mTitle);
        serviceViewCountTask = new ServiceViewCountTask();
        serviceEmailCountTask = new ServiceEmailCountTask();
        updateViewCount();
    }

    private void getDataFromPreviousPage() {
        Intent intent = this.getIntent();
        int objectIndex = intent.getIntExtra(Const.OBJECT_INDEX, 0);
        String toolbarTitle = intent.getStringExtra(Const.TOOLBAR_TITLE);
        mTitle = toolbarTitle;
        if (objectIndex != 0) {
            OpportunityHelper opportunityHelper = new OpportunityHelper();
            opportunity = opportunityHelper.get(objectIndex);
            if (opportunity != null) {
                TextView date = (TextView) findViewById(R.id.date);
                TextView dateMonthYear = (TextView) findViewById(R.id.dateMonthYear);
                TextView title = (TextView) findViewById(R.id.title);
                TextView textEmailCount = (TextView) findViewById(R.id.textEmailCount);
                TextView textViewCount = (TextView) findViewById(R.id.textViewCount);
                ImageView imageEnvelope = (ImageView) findViewById(R.id.imageEnvelope);
                ImageView imageEye = (ImageView) findViewById(R.id.imageEye);

                Drawable iconEnvelope = Helpers.getIcon(FontAwesome.Icon.faw_envelope,
                        15, getResources().getColor(R.color.white));
                imageEnvelope.setImageDrawable(iconEnvelope);
                Drawable iconEye = Helpers.getIcon(FontAwesome.Icon.faw_eye,
                        15, getResources().getColor(R.color.white));
                imageEye.setImageDrawable(iconEye);

                Date eventDate = opportunity.getStartDate();

                date.setText(Helpers.DateToStringDay(eventDate));
                dateMonthYear.setText(Helpers.DateToStringMonthYear(eventDate));
                title.setText(opportunity.getTitle().trim());

                TextView textViewContent = (TextView) findViewById(R.id.textViewContent);
                //String htmlContent = opportunity.getDescription();
                //textViewContent.setText(Html.fromHtml(htmlContent));
                textViewContent.setText(Helpers.cleanString(opportunity.getDescription()));

                textEmailCount.setText(Integer.toString(opportunity.getEmailCount()));
                textViewCount.setText(Integer.toString(opportunity.getViewCount()));
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        if (!opportunity.getCompanyEmail().isEmpty()) {
            MenuItem item =
                    menu.add(Menu.NONE, R.id.menu_email_us, Menu.NONE, R.string.action_email_us);
            MenuItemCompat.setShowAsAction(item, MenuItemCompat.SHOW_AS_ACTION_IF_ROOM);
            item.setIcon(Helpers.getActionIcon(FontAwesome.Icon.faw_envelope_o, false));
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_email_us:
                if (!opportunity.getCompanyEmail().isEmpty()) {
                    setSendEmail(opportunity.getCompanyEmail());
                }
                return true;
            case R.id.action_home:
                Intent intent = new Intent(BusinessOpportunitiesDetailActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (serviceViewCountTask != null) {
            serviceViewCountTask.cancel(true);
        }
        if (serviceEmailCountTask != null) {
            serviceEmailCountTask.cancel(true);
        }
        setResult(RESULT_OK, new Intent());
        finish();
        super.onBackPressed();
    }

    private void updateViewCount() {
        if (opportunity != null) {
            boolean isavailable = Helpers.isInternetConnected(BusinessOpportunitiesDetailActivity.this);
            if (isavailable) {
                serviceViewCountTask.execute();
            }
        }
    }

    private void updateEmailCount() {
        if (opportunity != null) {
            boolean isavailable = Helpers.isInternetConnected(BusinessOpportunitiesDetailActivity.this);
            if (isavailable) {
                serviceEmailCountTask.execute();
            }
        }
    }

    class ServiceViewCountTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            Reader reader = null;
            try {
                final int opportunitiesId = opportunity.getId();
                OpportunitiesService opportunitiesService = new OpportunitiesService();
                reader = opportunitiesService.updateViewCount(opportunitiesId);
                //App.getGson().fromJson(reader, ResultViewCount.class);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    class ServiceEmailCountTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (isCancelled()) {
                return null;
            }
            Reader reader = null;
            try {
                final int opportunitiesId = opportunity.getId();
                OpportunitiesService opportunitiesService = new OpportunitiesService();
                reader = opportunitiesService.updateEmailCount(opportunitiesId);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    private void setSendEmail(String email) {
        final String[] emailList = Helpers.getArrayString(email);
        int emailCount = emailList.length;
        if (emailCount > 0) {
            BottomSheet.Builder sheetBuilder = new BottomSheet.Builder(this, R.style.BottomSheet_StyleDialog)
                    .title(getResources().getString(R.string.bottom_sheet_sendemail)).
                            listener(new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int index) {
                                    Helpers.setSendEmail(emailList[index - 1], "", "", BusinessOpportunitiesDetailActivity.this);
                                    updateEmailCount();
                                }
                            });
            int countId = 1;
            for (String mail : emailList) {
                sheetBuilder.sheet(countId, Helpers.getBottomIcon(FontAwesome.Icon.faw_envelope_o, true),
                        mail);
                countId++;
            }
            sheetBuilder.build().show();

        }
    }
}
