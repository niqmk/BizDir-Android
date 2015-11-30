package id.bizdir.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;
import java.util.List;

import id.bizdir.R;
import id.bizdir.modelhelper.ForumThreadHelper;
import id.bizdir.model.ForumThread;
import id.bizdir.model.ResultObject;
import id.bizdir.model.ResultObjectHelper;
import id.bizdir.service.AllSync;
import id.bizdir.ui.activity.ForumPostActivity;
import id.bizdir.ui.adapter.ForumThreadTrendingAdapter;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 02/05/2015.
 */
public class ForumTrendingTopicsFragment extends Fragment {

    private List<ForumThread> list;
    private ListView listView;
    private TextView textNoData;
    private ForumThreadTrendingAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public ForumTrendingTopicsFragment newInstance() {
        ForumTrendingTopicsFragment fragment = new ForumTrendingTopicsFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {

        }
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.MaterialDrawerTheme_BizDir);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.fragment_location, container, false);
        View headerLayout = localInflater.inflate(R.layout.advertising_view, null);
        ImageView imageButtonAds = (ImageView) headerLayout.findViewById(R.id.imageButtonAds);
        ImageView imageAds = (ImageView) headerLayout.findViewById(R.id.image);
        listView = (ListView) view.findViewById(R.id.list);
        listView.setOnItemClickListener(setOnItemClickListener);
        listView.addHeaderView(headerLayout);
        textNoData = (TextView) view.findViewById(R.id.textNoData);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);
        mSwipeRefreshLayout.setProgressViewEndTarget(false, getResources().getInteger(R.integer.progress_bar_position));
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
        getData();
        Helpers.getLocalAds(getActivity(), imageAds, imageButtonAds, Const.ADS_ZONE_ID_FORUM);
        Helpers.getRemoteAds(getActivity(), imageAds, imageButtonAds, Const.ADS_ZONE_ID_FORUM);
        return view;
    }

    AdapterView.OnItemClickListener setOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                ForumThread obj = list.get(position - 1);
                if (obj != null) {
                    Intent intent = new Intent(getActivity(), ForumPostActivity.class);
                    intent.putExtra(Const.OBJECT_INDEX, obj.getId());
                    intent.putExtra(Const.TOOLBAR_TITLE, obj.getTitle());
                    startActivity(intent);
                }
            }
        }
    };

    private void getData() {
        ForumThreadHelper helper = new ForumThreadHelper();
        list = helper.getPopularThread();
        adapter = new ForumThreadTrendingAdapter(getActivity(),
                R.layout.item_forum_thread_trending, list);
        listView.setAdapter(adapter);
        if (list.size() > 0) {
            textNoData.setVisibility(View.GONE);
        } else {
            textNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //outState.putInt("forumCategoryId", forumCategoryId);
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
                    response = AllSync.syncForum();
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
                            AllSync.insertForumSync(jsonString);
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