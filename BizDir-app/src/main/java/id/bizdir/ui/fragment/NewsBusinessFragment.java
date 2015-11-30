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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.bizdir.R;
import id.bizdir.modelhelper.NewsBusinessCategoryHelper;
import id.bizdir.modelhelper.NewsBusinessHelper;
import id.bizdir.model.NewsBusiness;
import id.bizdir.model.NewsBusinessCategory;
import id.bizdir.model.ResultObject;
import id.bizdir.model.ResultObjectHelper;
import id.bizdir.service.AllSync;
import id.bizdir.ui.activity.NewsBusinessDetailActivity;
import id.bizdir.ui.adapter.NewsBusinessAdapter;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 02/05/2015.
 */
public class NewsBusinessFragment extends Fragment {

    private List<NewsBusiness> newsBusinessList;
    private ListView listNewsBusiness;
    private int selectedIndex;
    private TextView textNoData;
    private EditText editNewsCategory;
    private List<NewsBusinessCategory> newsBusinessCategoryList;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private NewsBusinessAdapter newsBusinessAdapter;
    private CharSequence[] charSequenceArray;

    public NewsBusinessFragment newInstance() {
        NewsBusinessFragment fragment = new NewsBusinessFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            selectedIndex = savedInstanceState.getInt("selectedIndex");
        } else {
            selectedIndex = 0;
        }

        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.MaterialDrawerTheme_BizDir);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.fragment_news_business, container, false);

        View headerLayout = localInflater.inflate(R.layout.advertising_view, null);
        ImageView imageButtonAds = (ImageView) headerLayout.findViewById(R.id.imageButtonAds);
        ImageView imageAds = (ImageView) headerLayout.findViewById(R.id.image);

        listNewsBusiness = (ListView) view.findViewById(R.id.listNewsBusiness);
        listNewsBusiness.setOnItemClickListener(setOnItemClickListener);
        listNewsBusiness.addHeaderView(headerLayout);

        textNoData = (TextView) view.findViewById(R.id.textNoData);
        editNewsCategory = (EditText) view.findViewById(R.id.editNewsCategory);
        editNewsCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewsCategory();
            }
        });
        editNewsCategory.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    showNewsCategory();
                }
            }
        });
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
        getData();
        Helpers.getLocalAds(getActivity(), imageAds, imageButtonAds, Const.ADS_ZONE_ID_NEWS_ANTARA);
        Helpers.getRemoteAds(getActivity(), imageAds, imageButtonAds, Const.ADS_ZONE_ID_NEWS_ANTARA);
        return view;
    }

    AdapterView.OnItemClickListener setOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                NewsBusiness newsBusiness = newsBusinessList.get(position - 1);
                if (newsBusiness != null) {
                    Intent intent = new Intent(getActivity(), NewsBusinessDetailActivity.class);
                    intent.putExtra(Const.OBJECT_INDEX, newsBusiness.getId());
                    startActivity(intent);
                }
            }
        }
    };

    private void getData() {
        NewsBusinessCategoryHelper newsBusinessCategoryHelper = new NewsBusinessCategoryHelper();
        newsBusinessCategoryList = newsBusinessCategoryHelper.getAll();
        NewsBusinessHelper newsBusinessHelper = new NewsBusinessHelper();
        newsBusinessList = newsBusinessHelper.
                getAll(newsBusinessCategoryList.get(selectedIndex).getId());
        newsBusinessAdapter = new NewsBusinessAdapter(getActivity(),
                R.layout.item_menu_news, newsBusinessList);
        listNewsBusiness.setAdapter(newsBusinessAdapter);
        if (newsBusinessList.size() > 0) {
            textNoData.setVisibility(View.GONE);
        } else {
            textNoData.setVisibility(View.VISIBLE);
        }

        List<CharSequence> categoryNews = new ArrayList<>();
        for (NewsBusinessCategory category : newsBusinessCategoryList) {
            categoryNews.add(category.getTitle());
        }
        charSequenceArray = categoryNews.toArray(new CharSequence[categoryNews.size()]);
        String selectedCategory = charSequenceArray[selectedIndex].toString();
        if (!TextUtils.isEmpty(selectedCategory)) {
            editNewsCategory.setText(selectedCategory);
        }
    }

    private void showNewsCategory() {


        new MaterialDialog.Builder(getActivity())
                .title(R.string.action_change_news)
                .items(charSequenceArray)
                .itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int index, CharSequence text) {
                        selectedIndex = index;
                        getData();
                        return true;
                    }
                })
                .positiveText(R.string.button_choose)
                .show();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedIndex", selectedIndex);
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
                    response = AllSync.syncNewsAntara();
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
                            AllSync.insertNewsAntaraSync(jsonString);
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