package id.bizdir.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.modelhelper.ForumThreadHelper;
import id.bizdir.model.ForumThread;
import id.bizdir.model.ResultObject;
import id.bizdir.model.ResultObjectHelper;
import id.bizdir.service.AllSync;
import id.bizdir.ui.adapter.ForumThreadAdapter;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;


public class ForumThreadActivity extends AppCompatActivity {

    private TextView textNoData;
    private ListView list;
    private List<ForumThread> listObject;
    private ForumThreadAdapter adapter;
    private ForumThreadHelper forumThreadHelper;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int progressInterval = 700;
    private int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_member);
        Helpers.setLockOrientation(ForumThreadActivity.this);
        getDataFromPreviousPage();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        textNoData = (TextView) findViewById(R.id.textNoData);
        list = (ListView) findViewById(R.id.list);
        list.setOnItemClickListener(setOnItemClickListener);
        forumThreadHelper = new ForumThreadHelper();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        //RefreshData();
                        refreshDataSync();
                    }
                });
            }
        });
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Helpers.setMainActionBarNoShadow(actionBar);
        }

        showProgress();
        RefreshData();
    }

    AdapterView.OnItemClickListener setOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ForumThread obj = listObject.get(position);
            if (obj != null) {
                Intent intent = new Intent(ForumThreadActivity.this, ForumPostActivity.class);
                intent.putExtra(Const.OBJECT_INDEX, obj.getId());
                intent.putExtra(Const.TOOLBAR_TITLE, obj.getTitle());
                startActivity(intent);
            }
        }
    };

    private void RefreshData() {
        new GetDataAndBindList().execute();
    }

    private void showProgress() {
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });
    }

    private void hideProgress() {
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, progressInterval);
    }

    private class GetDataAndBindList extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            getData();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            bindData();
            hideProgress();
        }
    }

    private void refreshDataSync() {
        class GetAllSyncTask extends AsyncTask<String, Void, String> {
            String response = "";

            @Override
            protected String doInBackground(String... param) {
                response = "";
                try {
                    response = AllSync.syncForum();
                } catch (IOException e) {
                    response = e.getMessage();
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(String resultJson) {
                ResultObject resultObject = ResultObjectHelper.getResult(resultJson);
                int status = resultObject.getStatus();
                if (status == 1) {
                    String jsonString = resultObject.getResult();
                    AllSync.insertForumSync(jsonString);
                }
                RefreshData();
                //hideProgress();
            }
        }
        GetAllSyncTask task = new GetAllSyncTask();
        task.execute();
    }

    @Override
    public void onResume() {
        boolean isDataChange = App.getDataMemberIsChanged();
        if (isDataChange) {
            showProgress();
            RefreshData();
        }
        super.onResume();
    }

    private void getData() {
        listObject = forumThreadHelper.getAll(categoryId);
    }

    private void bindData() {
        adapter = new ForumThreadAdapter(this,
                R.layout.item_forum_thread, listObject);
        list.setAdapter(adapter);
        if (listObject.size() > 0) {
            textNoData.setVisibility(View.GONE);
        } else {
            textNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void getDataFromPreviousPage() {
        Intent intent = this.getIntent();
        int objectIndex = intent.getIntExtra(Const.OBJECT_INDEX, 0);
        String toolbarTitle = intent.getStringExtra(Const.TOOLBAR_TITLE);
        if (objectIndex != 0) {
            categoryId = objectIndex;
        }
        if (!TextUtils.isEmpty(toolbarTitle)) {
            setTitle(toolbarTitle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu.size() > 0) {
            menu.clear();
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.fragment_favorite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_favorite_refresh:
                showProgress();
                refreshDataSync();
                return true;
            case R.id.action_home:
                Intent intent = new Intent(ForumThreadActivity.this, MainActivity.class);
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
