package id.bizdir.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.melnykov.fab.FloatingActionButton;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;

import java.io.IOException;
import java.util.List;

import id.bizdir.R;
import id.bizdir.modelhelper.NewsStockHelper;
import id.bizdir.modelhelper.TableSynchHelper;
import id.bizdir.model.NewsStock;
import id.bizdir.model.ResultObject;
import id.bizdir.model.ResultObjectHelper;
import id.bizdir.model.TableSynch;
import id.bizdir.service.NewsStockService;
import id.bizdir.ui.activity.NewsStockDetailActivity;
import id.bizdir.ui.adapter.NewsStockAdapter;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 02/05/2015.
 */
public class NewsStockFragment extends Fragment {

    private List<NewsStock> list;
    private ListView listView;
    private int selectedIndex;
    private TextView textNoData;
    private TextView textLastUpdate;
    private TextView editSearchCode;
    private NewsStockAdapter adapter;
    private NewsStockHelper helper;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public NewsStockFragment newInstance() {
        NewsStockFragment fragment = new NewsStockFragment();
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
        View view = localInflater.inflate(R.layout.fragment_news_stock, container, false);
        View headerLayout = localInflater.inflate(R.layout.advertising_view, null);
        ImageView imageButtonAds = (ImageView) headerLayout.findViewById(R.id.imageButtonAds);
        ImageView imageAds = (ImageView) headerLayout.findViewById(R.id.image);

        helper = new NewsStockHelper();
        listView = (ListView) view.findViewById(R.id.listNewsBusiness);
        listView.setOnItemClickListener(setOnItemClickListener);
        listView.addHeaderView(headerLayout);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.activity_main_swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue, R.color.red);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        getStockNewsFromWs(selectedIndex);
                    }
                });
            }
        });
        textNoData = (TextView) view.findViewById(R.id.textNoData);
        textLastUpdate = (TextView) view.findViewById(R.id.textLastUpdate);
        editSearchCode = (TextView) view.findViewById(R.id.editSearchCode);
        editSearchCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setImageDrawable(Helpers.getActionIcon(FontAwesome.Icon.faw_bar_chart, false));
        fab.setShadow(true);
        fab.attachToListView(listView);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showCategory();
            }
        });
        bindData();
        Helpers.getLocalAds(getActivity(), imageAds, imageButtonAds, Const.ADS_ZONE_ID_NEWS_STOCK);
        Helpers.getRemoteAds(getActivity(), imageAds, imageButtonAds, Const.ADS_ZONE_ID_NEWS_STOCK);
        return view;
    }

    AdapterView.OnItemClickListener setOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (position > 0) {
                NewsStock obj = adapter.getItem(position - 1);
                if (obj != null) {
                    Intent intent = new Intent(getActivity(), NewsStockDetailActivity.class);
                    intent.putExtra(Const.OBJECT_INDEX, obj.getId());
                    startActivity(intent);
                }
            }
        }
    };

    private void bindData() {
        list = helper.getAll();
        adapter = new NewsStockAdapter(getActivity(),
                R.layout.item_news_stock, list);
        listView.setAdapter(adapter);
        if (list.size() > 0) {
            textNoData.setVisibility(View.GONE);
        } else {
            textNoData.setVisibility(View.VISIBLE);
        }
        TableSynchHelper tableSynchHelper = new TableSynchHelper();
        TableSynch tableSynch = tableSynchHelper.get(Const.TABLE_NEWS_STOCK);
        if (tableSynch != null) {
            textLastUpdate.setText("Last update: " + Helpers.DateToStringPublishDate(tableSynch.getLastSynch()));
        }
    }

    private void showCategory() {
        CharSequence[] categoryArray = getResources().getStringArray(R.array.stock_category);

        new MaterialDialog.Builder(getActivity())
                .title(R.string.action_change_stock_category)
                .items(categoryArray)
                .itemsCallbackSingleChoice(selectedIndex, new MaterialDialog.ListCallbackSingleChoice() {
                    @Override
                    public boolean onSelection(MaterialDialog dialog, View view, int index, CharSequence text) {
                        selectedIndex = index;
                        showProgress();
                        getStockNewsFromWs(selectedIndex);
                        return true;
                    }
                })
                .positiveText(R.string.button_choose)
                .show();
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

    private void getStockNewsFromWs(int indexCategoryNewsStock) {
        class NewsStockAsyncTask extends AsyncTask<Integer, Void, String> {
            String response = "";


            @Override
            protected String doInBackground(Integer... param) {
                int indexCategoryNewsStock = param[0];
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
                if (!TextUtils.isEmpty(resultJson)) {
                    try {
                        ResultObject resultObject = ResultObjectHelper.getResult(resultJson);
                        if (resultObject != null) {
                            int status = resultObject.getStatus();
                            if (status == 1) {
                                String jsonString = resultObject.getResult();
                                helper.addAll(jsonString);
                                bindData();
                                hideProgress();
                            } else {
                                hideProgress();
                                String title = getResources().getString(R.string.dialog_get_news);
                                String content = getResources().getString(R.string.dialog_get_news_message);
                                Helpers.showDialog(getActivity(), title, content);
                            }
                        }
                    } catch (Exception ignore) {
                        hideProgress();
                        String title = getResources().getString(R.string.dialog_get_news);
                        String content = getResources().getString(R.string.dialog_get_news_message);
                        Helpers.showDialog(getActivity(), title, content);
                    }
                }

            }
        }

        boolean isavailable = Helpers.isInternetConnected(getActivity());
        if (isavailable) {
            Integer[] myTaskParams = {indexCategoryNewsStock};
            NewsStockAsyncTask task = new NewsStockAsyncTask();
            task.execute(myTaskParams);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("selectedIndex", selectedIndex);
    }


}