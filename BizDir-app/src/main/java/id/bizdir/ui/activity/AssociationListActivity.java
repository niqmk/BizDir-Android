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
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.list.AssociationMemberList;
import id.bizdir.model.Anggota;
import id.bizdir.service.AssociationService;
import id.bizdir.ui.adapter.MemberKadinAdapter;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;


public class AssociationListActivity extends AppCompatActivity {

    private TextView textNoData;
    private ListView list;
    private List<Anggota> listObject;
    private MemberKadinAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private Integer associationId;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum_post);
        Helpers.setLockOrientation(AssociationListActivity.this);
        getDataFromPreviousPage();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        textNoData = (TextView) findViewById(R.id.textNoData);
        list = (ListView) findViewById(R.id.list);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        refreshData();
                    }
                });
            }
        });
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Helpers.setMainActionBarNoShadow(actionBar);
        }
        setTitle(title);
        refreshData();
    }

    private void refreshData() {
        class MyTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgress();
            }

            @Override
            protected Void doInBackground(Void... params) {
                Reader reader = null;
                try {
                    AssociationService associationService = new AssociationService();
                    reader = associationService.getAssociationMemberList(associationId.toString());
                    AssociationMemberList result = null;
                    try {
                        result = App.getGson().fromJson(reader, AssociationMemberList.class);
                    } catch (Exception ex) {
                        Log.e("Error:", ex.getMessage());
                    }
                    if (result != null) {
                        int status = result.getStatus();
                        if (status == 1) {
                            if (result.getResult() != null) {
                                listObject = result.getResult();
                            } else {
                                listObject = new ArrayList<>();
                            }
                        } else {
                            listObject = new ArrayList<>();
                        }
                    } else {
                        listObject = new ArrayList<>();
                    }
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

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                bindData();
                hideProgress();
            }
        }
        boolean isavailable = Helpers.isInternetConnected(AssociationListActivity.this);
        if (isavailable) {
            MyTask task = new MyTask();
            task.execute();
        } else {
            hideProgress();
            new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title(R.string.dialog_association_failed)
                    .content(R.string.no_internet_connection)
                    .positiveText(R.string.button_ok)
                    .show();
        }
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
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void bindData() {
        adapter = new MemberKadinAdapter(this,
                R.layout.item_menu_member, listObject);
        list.setAdapter(adapter);
        if (listObject.size() > 0) {
            textNoData.setVisibility(View.GONE);
        } else {
            textNoData.setVisibility(View.VISIBLE);
        }
    }

    private void getDataFromPreviousPage() {
        Intent intent = this.getIntent();
        int objectIndex = intent.getIntExtra(Const.OBJECT_INDEX, 0);
        String toolbarTitle = intent.getStringExtra(Const.TOOLBAR_TITLE);
        if (objectIndex != 0) {
            associationId = objectIndex;
        }
        if (!TextUtils.isEmpty(toolbarTitle)) {
            title = toolbarTitle;
        }
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
                refreshData();
                return true;
            case R.id.action_home:
                Intent intent = new Intent(AssociationListActivity.this, MainActivity.class);
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
