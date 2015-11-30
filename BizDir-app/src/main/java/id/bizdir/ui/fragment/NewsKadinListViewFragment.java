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
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;

import java.io.IOException;
import java.util.List;

import id.bizdir.R;
import id.bizdir.modelhelper.NewsKadinHelper;
import id.bizdir.model.NewsKadin;
import id.bizdir.model.ResultObject;
import id.bizdir.model.ResultObjectHelper;
import id.bizdir.service.AllSync;
import id.bizdir.ui.activity.NewsKadinDetailActivity;
import id.bizdir.ui.adapter.NewsKadinListViewAdapter;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 20/04/2015.
 */
public class NewsKadinListViewFragment extends Fragment implements ObservableScrollViewCallbacks {

    private View mImageView;
    private View mListBackgroundView;
    private int mParallaxImageHeight;
    private ObservableListView mListView;
    private List<NewsKadin> newsKadinList;
    private TextView textNoData;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int progressInterval = 200;
    private ArrayAdapter<NewsKadin> adapter;

    public NewsKadinListViewFragment newInstance() {
        NewsKadinListViewFragment fragment = new NewsKadinListViewFragment();
        return fragment;
    }

    public NewsKadinListViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.MaterialDrawerTheme_BizDir);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View rootView = localInflater.inflate(R.layout.fragment_news_kadin_listview, container, false);
        textNoData = (TextView) rootView.findViewById(R.id.textNoData);
        mImageView = rootView.findViewById(R.id.adsView);
        ImageView imageButtonAds = (ImageView) rootView.findViewById(R.id.imageButtonAds);
        ImageView imageAds = (ImageView) rootView.findViewById(R.id.image);

        mSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
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
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.banner_image_height);
        mListView = (ObservableListView) rootView.findViewById(R.id.list);
        mListView.setScrollViewCallbacks(this);
        mListView.setOnItemClickListener(setOnItemClickListener);

        View paddingView = new View(getActivity());
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                mParallaxImageHeight);
        paddingView.setLayoutParams(lp);
        paddingView.setClickable(true);

        mListView.addHeaderView(paddingView);
        new GetDataAndBindList().execute();

        mListBackgroundView = rootView.findViewById(R.id.list_background);
        final View contentView = getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        contentView.post(new Runnable() {
            @Override
            public void run() {
                mListBackgroundView.getLayoutParams().height = contentView.getHeight();
            }
        });
        if (savedInstanceState != null) {
            onScrollChanged(mListView.getCurrentScrollY(), false, false);
        }
        Helpers.getLocalAds(getActivity(), imageAds, imageButtonAds, Const.ADS_ZONE_ID_NEWS_KADIN);
        Helpers.getRemoteAds(getActivity(), imageAds, imageButtonAds, Const.ADS_ZONE_ID_NEWS_KADIN);
        return rootView;
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
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        ViewHelper.setTranslationY(mImageView, -scrollY / 2);
        ViewHelper.setTranslationY(mListBackgroundView, Math.max(0, -scrollY + mParallaxImageHeight));
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private void getData() {
        NewsKadinHelper newsKadinHelper = new NewsKadinHelper();
        newsKadinList = newsKadinHelper.getAll();
    }

    private void bindData() {
        adapter = new NewsKadinListViewAdapter(getActivity(),
                R.layout.item_menu_news, newsKadinList);
        mListView.setAdapter(adapter);
        if (newsKadinList.size() > 0) {
            textNoData.setVisibility(View.GONE);
        } else {
            textNoData.setVisibility(View.VISIBLE);
        }
    }

    AdapterView.OnItemClickListener setOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            NewsKadin newsKadin = newsKadinList.get(position - 1);
            if (newsKadin != null) {
                Intent intent = new Intent(getActivity(), NewsKadinDetailActivity.class);
                intent.putExtra(Const.OBJECT_INDEX, newsKadin.getId());
                startActivity(intent);
            }
        }
    };

    private void refreshData() {
        class GetAllSyncTask extends AsyncTask<String, Void, String> {
            String response = "";

            @Override
            protected String doInBackground(String... param) {
                response = "";
                try {
                    response = AllSync.syncNewsKadin();
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
                            AllSync.insertNewsKadinSync(jsonString);
                        }
                        getData();
                        bindData();
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
}
