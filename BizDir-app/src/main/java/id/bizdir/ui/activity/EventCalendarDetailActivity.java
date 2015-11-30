package id.bizdir.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.Date;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.model.MemberBizdir;
import id.bizdir.model.ResultObject;
import id.bizdir.model.ResultObjectHelper;
import id.bizdir.model.RspvObject;
import id.bizdir.modelhelper.EventHelper;
import id.bizdir.model.Event;
import id.bizdir.modelhelper.MemberBizdirHelper;
import id.bizdir.service.EventService;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;


public class EventCalendarDetailActivity extends AppCompatActivity {

    private Event event;
    private MemberBizdir memberBizdir;
    private RadioGroup radioGroupAttend;
    private RadioButton radioAttend;
    private RadioButton radioNotAttend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_calendar_detail);
        Helpers.setLockOrientation(EventCalendarDetailActivity.this);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        radioGroupAttend = (RadioGroup) findViewById(R.id.radioGroupAttend);
        radioAttend = (RadioButton) findViewById(R.id.radioAttend);
        radioNotAttend = (RadioButton) findViewById(R.id.radioNotAttend);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Helpers.setMainActionBar(actionBar);
        }
        radioGroupAttend.setVisibility(View.GONE);
        radioAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRspv(event.getId(), memberBizdir.getId(), Const.RSPV_ATTENDING);
            }
        });
        radioNotAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRspv(event.getId(), memberBizdir.getId(), Const.RSPV_NOT_ATTENDING);
            }
        });
        getDataFromPreviousPage();
        checkRsvp();
    }

    private void getDataFromPreviousPage() {
        Intent intent = this.getIntent();
        int objectIndex = intent.getIntExtra(Const.OBJECT_INDEX, 0);
        if (objectIndex != 0) {
            EventHelper eventHelper = new EventHelper();
            event = eventHelper.get(objectIndex);
            if (event != null) {

                /*
                String htmlContent = Const.HTML_CONTENT_HEADER;
                htmlContent += event.getDescription() + Const.HTML_CONTENT_FOOTER;
                WebView webContent = (WebView) findViewById(R.id.webContent);
                webContent.loadData(htmlContent, Const.MIME_TYPE, Const.ENCODING_TYPE);
                */

                TextView date = (TextView) findViewById(R.id.date);
                TextView dateMonthYear = (TextView) findViewById(R.id.dateMonthYear);
                TextView title = (TextView) findViewById(R.id.title);

                Date eventDate = event.getStartDate();

                date.setText(Helpers.DateToStringDay(eventDate));
                dateMonthYear.setText(Helpers.DateToStringMonthYear(eventDate));
                title.setText(event.getTitle().trim());

                TextView textViewContent = (TextView) findViewById(R.id.textViewContent);
                //String htmlContent = Const.HTML_CONTENT_HEADER;
                // htmlContent += event.getDescription() + Const.HTML_CONTENT_FOOTER;
                //htmlContent = htmlContent.replace("&nbsp;", "<br />");
                //textViewContent.setText(Html.fromHtml(htmlContent));
                textViewContent.setText(Helpers.cleanString(event.getDescription()));
            }
        }
    }

    private void checkRsvp() {
        getMemberBizdir();
        if (memberBizdir != null) {
            checkRspv(event.getId(), memberBizdir.getId());
        }
    }

    private void checkRspv(Integer eventId, Integer userId) {
        class GetAllSyncTask extends AsyncTask<String, Void, String> {
            String response = "";

            @Override
            protected String doInBackground(String... param) {
                String eventId = param[0];
                String userId = param[1];
                response = "";
                try {
                    EventService eventService = new EventService();
                    response = eventService.checkRspv(eventId, userId);
                } catch (IOException e) {
                    response = e.getMessage();
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(String resultJson) {
                final ResultObject resultObject = ResultObjectHelper.getResult(resultJson);
                final int status = resultObject.getStatus();
                if (status == 1) {
                    String message = resultObject.getMessage();
                    String jsonString = resultObject.getResult();
                    RspvObject rspvObject = App.getGson().fromJson(jsonString, RspvObject.class);
                    if (rspvObject != null) {
                        updateUiRspv(rspvObject, message);
                    }
                } else {
                    checkError(status);
                }
                //showUiEvent();
            }
        }

        String[] myTaskParams = {
                eventId.toString(),
                userId.toString()
        };
        GetAllSyncTask task = new GetAllSyncTask();
        task.execute(myTaskParams);
    }

    private void checkError(int status) {
        if (status == 0) {
            if (TextUtils.isEmpty(memberBizdir.getPhone())) {
                Helpers.showShortToast(Helpers.getString(R.string.fill_phone_number));
            }
        }
    }

    private void updateUiRspv(RspvObject rspvObject, String message) {
        int rspvStatus = rspvObject.getStatusRspv();
        switch (rspvStatus) {
            case 1:
                Helpers.showLongToast(message);
                radioGroupAttend.setVisibility(View.VISIBLE);
                break;
            case 2:
            case 3:
                Helpers.showLongToast(message);
                radioGroupAttend.setVisibility(View.GONE);
                break;
            default:
                Helpers.showLongToast(message);
                break;
        }
    }

    private void getMemberBizdir() {
        MemberBizdirHelper memberBizdirHelper = new MemberBizdirHelper();
        memberBizdir = memberBizdirHelper.get();
    }

    private void createRspv(Integer eventId, Integer userId, Integer attendingAction) {
        class GetAllSyncTask extends AsyncTask<String, Void, String> {
            String response = "";

            @Override
            protected String doInBackground(String... param) {
                String eventId = param[0];
                String userId = param[1];
                String attendingAction = param[2]; //attendingAction: 2 (attending rspv), 3 (not attending rspv)
                response = "";
                try {
                    EventService eventService = new EventService();
                    response = eventService.createRspv(eventId, userId, attendingAction);
                } catch (IOException e) {
                    response = e.getMessage();
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(String resultJson) {
                final ResultObject resultObject = ResultObjectHelper.getResult(resultJson);
                final int status = resultObject.getStatus();
                if (status == 1) {
                    String message = resultObject.getMessage();
                    String jsonString = resultObject.getResult();
                    successCreateRspv(message);
                } else {
                    checkErrorCreateRspv(status);
                }
                //showUiEvent();
            }
        }

        String[] myTaskParams = {
                eventId.toString(),
                userId.toString(),
                attendingAction.toString()
        };
        GetAllSyncTask task = new GetAllSyncTask();
        task.execute(myTaskParams);
    }

    private void successCreateRspv(String message) {
        Helpers.showLongToast(Helpers.getString(R.string.success_create_rspv));
        radioGroupAttend.setVisibility(View.GONE);
    }

    private void checkErrorCreateRspv(int status) {
        if (status == 0) {
            Helpers.showLongToast(Helpers.getString(R.string.error_create_rspv));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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
                Intent intent = new Intent(EventCalendarDetailActivity.this, MainActivity.class);
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
        super.onBackPressed();
    }
}
