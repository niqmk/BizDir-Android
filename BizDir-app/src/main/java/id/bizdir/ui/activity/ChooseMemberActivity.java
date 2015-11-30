package id.bizdir.ui.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.modelhelper.AnggotaHelper;
import id.bizdir.model.Anggota;
import id.bizdir.model.City;
import id.bizdir.model.Province;
import id.bizdir.ui.adapter.ChooseMemberKadinAdapter;
import id.bizdir.util.Helpers;


public class ChooseMemberActivity extends AppCompatActivity {

    private TextView textNoData;
    private ListView list;
    private List<Anggota> listObject;
    private ChooseMemberKadinAdapter adapter;
    private AnggotaHelper memberKadinHelper;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    //private int progressInterval = 200;
    private String currentQuery;
    private SearchView searchView;
    private SearchTimer searchTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_member);
        Helpers.setLockOrientation(ChooseMemberActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        textNoData = (TextView) findViewById(R.id.textNoData);
        list = (ListView) findViewById(R.id.list);

        memberKadinHelper = new AnggotaHelper();
        currentQuery = "";
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        RefreshData();

                    }
                });
            }
        });
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Helpers.setMainActionBarNoShadow(actionBar);
        }
        RefreshData();
        searchTimer = new SearchTimer(1000, 1000);
        searchTimer.cancel();
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
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private class GetDataAndBindList extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress();
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
            RefreshData();
        }
        super.onResume();
    }

    private void getData() {
        if (currentQuery.equals("")) {
            listObject = new ArrayList<>();
        } else {
            City city = App.getCity();
            Province province = App.getProvince();
            if (city == null && province == null) {
                listObject = memberKadinHelper.getAll(currentQuery);
            } else {
                if (city.getId() > 0) {
                    listObject = memberKadinHelper.getAll(currentQuery, city.getProvinceId(), city.getId());
                } else {
                    if (province.getId() > 0) {
                        listObject = memberKadinHelper.getAll(currentQuery, province.getId());
                    } else {
                        listObject = memberKadinHelper.getAll(currentQuery);
                    }
                }
            }
        }
        //App.setDataMemberIsChanged(false);
    }

    private void bindData() {
        adapter = new ChooseMemberKadinAdapter(this, R.layout.item_menu_member, listObject, searchView);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu.size() > 0) {
            menu.clear();
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_search_member, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        //searchView.setQuery(currentQuery, true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(final String query) {
                //adapter.getFilter().filter(query);
                //Toast.makeText(getActivity(), "Searching for: " + query, Toast.LENGTH_SHORT).show();
                //adapter.f;
                //setData(query);
                /*
                new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  currentQuery = query;
                                                  RefreshData();
                                              }
                                          },
                        300);
                        return true;
                        */
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                //adapter.getFilter().filter(newText);
                /*
                new Handler().postDelayed(new Runnable() {
                                              @Override
                                              public void run() {
                                                  currentQuery = newText;
                                                  RefreshData();
                                              }
                                          },
                        300);
                */
                currentQuery = newText;
                searchTimer.cancel();
                searchTimer.start();
                return true;
            }
        });
        return true;
    }

    class SearchTimer extends CountDownTimer {
        public SearchTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            RefreshData();
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }

    private void chooseLocation() {
        Intent intent = new Intent(ChooseMemberActivity.this, ChooseLocationActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_search_location:
                chooseLocation();
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
