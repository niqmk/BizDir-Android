package id.bizdir.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;
import java.util.List;

import id.bizdir.R;
import id.bizdir.modelhelper.DownloadRootHelper;
import id.bizdir.modelhelper.DownloadSubHelper;
import id.bizdir.model.DownloadRoot;
import id.bizdir.model.DownloadSub;
import id.bizdir.model.ResultObject;
import id.bizdir.model.ResultObjectHelper;
import id.bizdir.service.AllSync;
import id.bizdir.ui.activity.MainActivity;
import id.bizdir.ui.adapter.DownloadAdapter;
import id.bizdir.ui.widget.AnimatedExpandableListView;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 02/05/2015.
 */
public class DownloadFragment extends Fragment {

    private AnimatedExpandableListView listView;
    private List<DownloadRoot> downloadRootList;
    private TextView textNoData;
    private DownloadAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public DownloadFragment newInstance() {
        DownloadFragment fragment = new DownloadFragment();
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
        View view = localInflater.inflate(R.layout.fragment_download, container, false);
        listView = (AnimatedExpandableListView) view.findViewById(R.id.listView);
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
        //int width = getResources().getDisplayMetrics().widthPixels;
        //if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
        //    listView.setIndicatorBounds(width - Helpers.getPixelValue(55), width - Helpers.getPixelValue(0));
        //} else {
        //    listView.setIndicatorBoundsRelative(width - Helpers.getPixelValue(55), width - Helpers.getPixelValue(0));
        //}
        getData();
        return view;
    }

    private void getData() {
        DownloadRootHelper downloadRootHelper = new DownloadRootHelper();
        DownloadSubHelper downloadSubHelper = new DownloadSubHelper();
        downloadRootList = downloadRootHelper.getAll();
        for (DownloadRoot downloadRoot : downloadRootList) {
            List<DownloadSub> downloadSubList = downloadSubHelper.getAll(downloadRoot.getId());
            if (downloadSubList.size() > 0) {
                downloadRoot.setDownloadSubList(downloadSubList);
            }
        }
        adapter = new DownloadAdapter(getActivity(), R.layout.item_group_download,
                R.layout.item_child_download, downloadRootList);
        listView.setAdapter(adapter);
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            listView.expandGroup(i);
        }
        if (downloadRootList.size() > 0) {
            textNoData.setVisibility(View.GONE);
        } else {
            textNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu.size() > 0) {
            menu.clear();
        }
        inflater.inflate(R.menu.fragment_favorite, menu);
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
                    response = AllSync.syncDownload();
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
                            AllSync.insertDownloadSync(jsonString);
                        }
                        getData();
                        hideProgress();
                    } catch (Exception ignore) {
                        hideProgress();
                    }
                }
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