package id.bizdir.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.rey.material.widget.ProgressView;

import java.io.IOException;
import java.io.Reader;
import java.util.Date;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.model.AllSyncModel;
import id.bizdir.model.MemberBizdir;
import id.bizdir.model.ResultObject;
import id.bizdir.model.ResultObjectHelper;
import id.bizdir.model.TableSynch;
import id.bizdir.modelhelper.MemberBizdirHelper;
import id.bizdir.modelhelper.NewsStockHelper;
import id.bizdir.modelhelper.TableSynchHelper;
import id.bizdir.service.AllSync;
import id.bizdir.service.NewsStockService;
import id.bizdir.util.DatabaseHelper;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 25/04/2015.
 */
public class SplashScreen extends AppCompatActivity {

    private ProgressView progressBar;
    private Date stockNewsLastSync;
    private TextView textPercent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Helpers.setLockOrientation(SplashScreen.this);
        progressBar = (ProgressView) findViewById(R.id.progressBar);
        textPercent = (TextView) findViewById(R.id.textPercent);
        progressBar.setProgress(0);
        progressBar.start();
        stockNewsLastSync = new Date();
        StartTask startTask = new StartTask();
        startTask.execute();
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void setProgressBar(final Integer progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(progress);
                textPercent.setText(progress.toString() + "%");
            }
        });
    }

    private void gotoNextActivity() {
        new Runnable() {
            @Override
            public void run() {
                progressBar.setProgress(100);
                progressBar.stop();
                MemberBizdirHelper memberBizdirHelper = new MemberBizdirHelper();
                MemberBizdir memberBizdir = memberBizdirHelper.get();
                Intent intent;
                if (memberBizdir != null) {
                    intent = new Intent(SplashScreen.this, MainActivity.class);
                } else {
                    //intent = new Intent(SplashScreen.this, LoginActivity.class);
                    intent = new Intent(SplashScreen.this, WalkthroughActivity.class);
                }
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }.run();
    }

    private class StartTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setProgressBar(10);
        }

        @Override
        protected Void doInBackground(Void... params) {
            DatabaseHelper databaseHelper = OpenHelperManager.getHelper(
                    SplashScreen.this, DatabaseHelper.class);
            setProgressBar(20);
            App.setDatabaseHelper(databaseHelper);
            setProgressBar(30);
            TableSynchHelper tableSynchHelper = new TableSynchHelper();
            TableSynch tableSynch = tableSynchHelper.get("news_stock");
            if (tableSynch != null) {
                setProgressBar(50);
                stockNewsLastSync = tableSynch.getLastSynch();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            setProgressBar(60);
            boolean isavailable = Helpers.isInternetConnected(SplashScreen.this);
            if (isavailable) {
                GetAllSyncTask task = new GetAllSyncTask();
                task.execute();
            } else {
                gotoNextActivity();
            }

        }
    }

    private void getStockNewsFromWs(final int indexCategoryNewsStock) {
        class NewsStockAsyncTask extends AsyncTask<Integer, Void, String> {
            String response = "";

            @Override
            protected String doInBackground(Integer... param) {
                response = "";
                try {
                    NewsStockService service = new NewsStockService();
                    response = service.getNewStock(indexCategoryNewsStock);
                } catch (IOException e) {
                    response = e.getMessage();
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(String resultJson) {
                setProgressBar(80);
                ResultObject resultObject = ResultObjectHelper.getResult(resultJson);
                if (resultObject != null) {
                    int status = resultObject.getStatus();
                    if (status == 1) {
                        String jsonString = resultObject.getResult();
                        NewsStockHelper helper = new NewsStockHelper();
                        helper.addAll(jsonString);
                    }
                }
                gotoNextActivity();
            }
        }
        NewsStockAsyncTask task = new NewsStockAsyncTask();
        task.execute();
    }

    private class GetAllSyncTask extends AsyncTask<AllSyncModel, Void, AllSyncModel> {

        @Override
        protected AllSyncModel doInBackground(AllSyncModel... params) {
            AllSyncModel result = new AllSyncModel();
            Reader reader = null;
            try {
                reader = AllSync.syncAllReader(true);
                result = App.getGson().fromJson(reader, AllSyncModel.class);
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
            return result;
        }

        @Override
        protected void onPostExecute(AllSyncModel result) {
            setProgressBar(70);
            if (result != null) {
                InsertAllSyncTask insertAllSyncTask = new InsertAllSyncTask();
                insertAllSyncTask.execute(result);
            } else {
                gotoNextActivity();
            }
        }
    }

    private class InsertAllSyncTask extends AsyncTask<AllSyncModel, Void, Void> {

        @Override
        protected Void doInBackground(AllSyncModel... params) {
            Helpers.insertOrUpdateAllSyncMaster(params[0]);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            setProgressBar(75);
            if (stockNewsLastSync!=null)  {
                if (DateUtils.isToday(stockNewsLastSync.getTime())) {
                    gotoNextActivity();
                } else {
                    getStockNewsFromWs(0);
                }
            } else {
                gotoNextActivity();
            }
        }
    }
}
