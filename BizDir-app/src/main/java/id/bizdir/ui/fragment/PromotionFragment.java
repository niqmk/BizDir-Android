package id.bizdir.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;
import java.util.List;

import id.bizdir.R;
import id.bizdir.modelhelper.PromotionHelper;
import id.bizdir.model.Promotion;
import id.bizdir.model.ResultObject;
import id.bizdir.model.ResultObjectHelper;
import id.bizdir.service.AllSync;
import id.bizdir.ui.activity.MainActivity;
import id.bizdir.ui.activity.PromotionDetailActivity;
import id.bizdir.ui.adapter.PromotionAdapter;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 02/05/2015.
 */
public class PromotionFragment extends Fragment {

    private List<Promotion> listPromotion;
    private TextView textNoData;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView list;
    private PromotionAdapter promotionAdapter;
    private SearchView searchView;
    private String currentQuery = null;

    public PromotionFragment newInstance() {
        PromotionFragment fragment = new PromotionFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
        }

        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.MaterialDrawerTheme_BizDir);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.fragment_promotion, container, false);
        textNoData = (TextView) view.findViewById(R.id.textNoData);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
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
        list = (ListView) view.findViewById(R.id.list);
        list.setOnItemClickListener(setOnItemClickListener);
        currentQuery = "";
        getData();
        return view;
    }

    private void getData() {
        PromotionHelper promotionHelper = new PromotionHelper();
        listPromotion = promotionHelper.getAll();
        promotionAdapter = new PromotionAdapter(getActivity(),
                R.layout.item_cardview_promotion, listPromotion);
        list.setAdapter(promotionAdapter);
        if (listPromotion.size() > 0) {
            textNoData.setVisibility(View.GONE);
        } else {
            textNoData.setVisibility(View.VISIBLE);
        }
    }

    AdapterView.OnItemClickListener setOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Promotion promotion = promotionAdapter.getItem(position);
            if (promotion != null) {
                searchView.clearFocus();
                Intent intent = new Intent(getActivity(), PromotionDetailActivity.class);
                intent.putExtra(Const.OBJECT_INDEX, promotion.getId());
                startActivity(intent);
            }
        }
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu.size() > 0) {
            menu.clear();
        }
        inflater.inflate(R.menu.fragment_search_home_refresh, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        if (!currentQuery.isEmpty()) {
            searchView.setQuery(currentQuery, true);
            searchView.setIconified(false);
            searchView.requestFocusFromTouch();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                promotionAdapter.getFilter().filter(newText);
                currentQuery = newText;
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite_refresh:
                showProgress();
                refreshData();
                return true;
            case R.id.action_home:
                ((MainActivity) getActivity()).onSectionAttached(Const.PAGE_HOME_SEARCH);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
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

    private void refreshData() {
        class GetAllSyncTask extends AsyncTask<String, Void, String> {
            String response = "";

            @Override
            protected String doInBackground(String... param) {
                response = "";
                try {
                    response = AllSync.syncPromotion();
                } catch (IOException e) {
                    response = e.getMessage();
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(String resultJson) {
                if (!TextUtils.isEmpty(resultJson)) {
                    try {
                        ResultObject resultObject = ResultObjectHelper.getResult(resultJson);
                        int status = resultObject.getStatus();
                        if (status == 1) {
                            String jsonString = resultObject.getResult();
                            AllSync.insertPromotionSync(jsonString);
                        }
                        getData();
                    } catch (Exception ignore) {
                        hideProgress();
                    }
                }
                hideProgress();
            }
        }
        boolean isavailable = Helpers.isInternetConnected(getActivity());
        if (isavailable) {
            GetAllSyncTask task = new GetAllSyncTask();
            task.execute();
        } else {
            new MaterialDialog.Builder(new ContextThemeWrapper(getActivity(),
                    R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title(R.string.no_internet_connection_title)
                    .content(R.string.no_internet_connection)
                    .positiveText(R.string.button_ok)
                    .show();
            hideProgress();
        }
    }
}