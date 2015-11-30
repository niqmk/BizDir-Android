package id.bizdir.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.modelhelper.ForumPostHelper;
import id.bizdir.modelhelper.MemberBizdirHelper;
import id.bizdir.model.ForumPost;
import id.bizdir.model.MemberBizdir;
import id.bizdir.model.ResultObject;
import id.bizdir.model.ResultObjectHelper;
import id.bizdir.service.AllSync;
import id.bizdir.service.ForumPostService;
import id.bizdir.ui.adapter.ForumPostAdapter;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;


public class ForumPostActivity extends AppCompatActivity {

    private TextView textNoData;
    private TextView title;
    private ListView list;
    private List<ForumPost> listObject;
    private ForumPostAdapter adapter;
    private ForumPostHelper forumPostHelper;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int progressInterval = 700;
    private int threadId;
    private MemberBizdir memberBizdir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_post);
        Helpers.setLockOrientation(ForumPostActivity.this);
        LayoutInflater inflater = LayoutInflater.from(ForumPostActivity.this);
        View headerLayout = inflater.inflate(R.layout.item_header_forum_post, null);
        title = (TextView) headerLayout.findViewById(R.id.title);
        getDataFromPreviousPage();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        textNoData = (TextView) findViewById(R.id.textNoData);
        list = (ListView) findViewById(R.id.list);
        list.addHeaderView(headerLayout);
        forumPostHelper = new ForumPostHelper();
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
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
            }
        }
        GetAllSyncTask task = new GetAllSyncTask();
        task.execute();
    }

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
            //showProgress();
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
        listObject = forumPostHelper.getAll(threadId);
    }

    private void bindData() {
        adapter = new ForumPostAdapter(this,
                R.layout.item_cardview_forum_post, listObject);
        list.setAdapter(adapter);
        if (listObject.size() > 0) {
            textNoData.setVisibility(View.GONE);
        } else {
            textNoData.setVisibility(View.VISIBLE);
        }
    }

    private void showReplyPostDialog() {
        new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                .title(R.string.action_reply_forum)
                .content(R.string.action_post_message_forum)
                .inputType(InputType.TYPE_CLASS_TEXT)
                        //InputType.TYPE_TEXT_VARIATION_PERSON_NAME |
                        // InputType.TYPE_TEXT_FLAG_CAP_WORDS)
                .inputMaxLength(Const.MAX_CHAR_POST_MESSAGE)
                .positiveText(R.string.action_submit)
                .input(R.string.action_message, 0,
                        false, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                //showToast("Hello, " + input.toString() + "!");
                                int userId = 0;
                                if (memberBizdir != null) {
                                    userId = memberBizdir.getId();
                                }
                                boolean isavailable = Helpers.isInternetConnected(ForumPostActivity.this);
                                if (isavailable) {
                                    postForumWs(userId, threadId, input.toString());
                                } else {
                                    new MaterialDialog.Builder(new ContextThemeWrapper(ForumPostActivity.this,
                                            R.style.MaterialDrawerTheme_Light_DarkToolbar))
                                            .title(R.string.dialog_forum_post_failed)
                                            .content(R.string.no_internet_connection)
                                            .positiveText(R.string.button_ok)
                                            .show();
                                }
                            }
                        }).show();
    }

    private void postForumWs(Integer userId, Integer topicId, String message) {

        class PostAsyncTask extends AsyncTask<String, Void, String> {
            String response = "";
            final MaterialDialog progressDialog = new MaterialDialog.Builder(
                    new ContextThemeWrapper(ForumPostActivity.this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title("Loading")
                    .content("Please wait...")
                    .progress(true, 0)
                    .cancelable(false)
                    .show();

            @Override
            protected String doInBackground(String... param) {
                String userId = param[0];
                String topicId = param[1];
                String message = param[2];
                response = "";
                try {
                    ForumPostService service = new ForumPostService();
                    response = service.postForum(userId, topicId, message);
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
                        String resultJsonString = resultObject.getResult();
                        ForumPost forumPost = App.getGson().fromJson(resultJsonString,
                                ForumPost.class);
                        if (forumPost != null) {
                            forumPost.setCreateDate(new Date());
                            ForumPostHelper helper = new ForumPostHelper();
                            helper.add(forumPost);
                            showProgress();
                            RefreshData();
                        }
                    } else {
                        String title = getResources().getString(R.string.dialog_forum_post_failed);
                        String content = getResources().getString(R.string.dialog_forum_post_failed_message);
                        Helpers.showDialog(ForumPostActivity.this, title, content);
                    }
                }
            }
        }
        String[] myTaskParams = {userId.toString(), topicId.toString(), message};
        PostAsyncTask task = new PostAsyncTask();
        task.execute(myTaskParams);
    }

    private void postViewCountWs(Integer topicId) {

        class PostAsyncTask extends AsyncTask<String, Void, String> {
            String response = "";

            @Override
            protected String doInBackground(String... param) {
                String topicId = param[0];
                response = "";
                try {
                    ForumPostService service = new ForumPostService();
                    response = service.postViewCount(topicId);
                } catch (IOException e) {
                    response = e.getMessage();
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(String resultJson) {
                ResultObject resultObject = ResultObjectHelper.getResult(resultJson);
                if (resultObject != null) {
                    int status = resultObject.getStatus();
                    if (status == 1) {
                        String resultJsonString = resultObject.getResult();
                        Log.i("View Count Success:", resultJsonString);
                    } else {
                        Log.i("View Count Failed:", resultObject.getMessage());
                    }
                }
            }
        }
        String[] myTaskParams = {topicId.toString()};
        PostAsyncTask task = new PostAsyncTask();
        task.execute(myTaskParams);
    }

    private void getDataFromPreviousPage() {
        Intent intent = this.getIntent();
        int objectIndex = intent.getIntExtra(Const.OBJECT_INDEX, 0);
        String toolbarTitle = intent.getStringExtra(Const.TOOLBAR_TITLE);
        if (objectIndex != 0) {
            threadId = objectIndex;
        }
        if (!TextUtils.isEmpty(toolbarTitle)) {
            title.setText(toolbarTitle);
        }
        MemberBizdirHelper memberBizdirHelper = new MemberBizdirHelper();
        memberBizdir = memberBizdirHelper.get();
        postViewCountWs(threadId);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu.size() > 0) {
            menu.clear();
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_forum_post, menu);
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
            case R.id.action_post_forum:
                showReplyPostDialog();
                return true;
            case R.id.action_home:
                Intent intent = new Intent(ForumPostActivity.this, MainActivity.class);
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
