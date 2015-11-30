package id.bizdir.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.util.Log;
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
import java.io.Reader;
import java.util.List;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.list.AssociationList;
import id.bizdir.model.Association;
import id.bizdir.modelhelper.AssociationHelper;
import id.bizdir.service.AssociationService;
import id.bizdir.ui.activity.AssociationListActivity;
import id.bizdir.ui.activity.ChooseLocationActivity;
import id.bizdir.ui.activity.MainActivity;
import id.bizdir.ui.adapter.AssociationAdapter;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 20/04/2015.
 */
public class AssociationFragment extends Fragment {

    private TextView textNoData;
    private AssociationAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView list;
    private boolean isCancel = false;

    public AssociationFragment newInstance() {
        //AssociationFragment fragment = new AssociationFragment();
        return new AssociationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
        }
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.MaterialDrawerTheme_BizDir);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.fragment_location, container, false);
        //View view = inflater.inflate(R.layout.fragment_location, container, false);
        textNoData = (TextView) view.findViewById(R.id.textNoData);
        list = (ListView) view.findViewById(R.id.list);
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
        refreshData();
        return view;
    }

    private void getData() {
        AssociationHelper helper = new AssociationHelper();
        List<Association> listObject = helper.getAll();
        adapter = new AssociationAdapter(getActivity(),
                R.layout.item_association, listObject);
        list.setAdapter(adapter);
        list.setOnItemClickListener(setOnItemClickListener);
        if (listObject.size() > 0) {
            textNoData.setVisibility(View.GONE);
        } else {
            textNoData.setVisibility(View.VISIBLE);
        }
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
        isCancel = false;
        class GetAllSyncTask extends AsyncTask<Void, Void, Void> {

            private void showProgress() {
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(true);
                    }
                });
            }

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
                    Integer cityId = App.getCity().getId();
                    reader = associationService.getAssociationMaster(cityId.toString());
                    AssociationList result = null;
                    try {
                        result = App.getGson().fromJson(reader, AssociationList.class);
                    } catch (Exception ex) {
                        Log.e("Error:", ex.getMessage());
                    }
                    if (result != null) {
                        int status = result.getStatus();
                        if (status == 1) {
                            if (result.getResult() != null) {
                                AssociationHelper associationHelper = new AssociationHelper();
                                associationHelper.addAll(result.getResult());
                            }
                        }
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
                if (!isCancel) {
                    getData();
                    hideProgress();
                }
            }
        }

        boolean isavailable = Helpers.isInternetConnected(getActivity());
        if (isavailable) {
            GetAllSyncTask task = new GetAllSyncTask();
            task.execute();
        } else {
            hideProgress();
            new MaterialDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title(R.string.dialog_association_failed)
                    .content(R.string.no_internet_connection)
                    .positiveText(R.string.button_ok)
                    .show();
        }
    }

    AdapterView.OnItemClickListener setOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            handelListItemClick(adapter.getItem(position));
        }
    };

    private void handelListItemClick(Association obj) {
        if (obj != null) {
            boolean isavailable = Helpers.isInternetConnected(getActivity());
            if (isavailable) {
                Intent intent = new Intent(getActivity(), AssociationListActivity.class);
                intent.putExtra(Const.TOOLBAR_TITLE, obj.getAbbr());
                intent.putExtra(Const.OBJECT_INDEX, obj.getId());
                startActivity(intent);
            } else {
                hideProgress();
                new MaterialDialog.Builder(new ContextThemeWrapper(getActivity(), R.style.MaterialDrawerTheme_Light_DarkToolbar))
                        .title(R.string.dialog_association_failed)
                        .content(R.string.no_internet_connection)
                        .positiveText(R.string.button_ok)
                        .show();
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu.size() > 0) {
            menu.clear();
        }
        inflater.inflate(R.menu.menu_fragment_category, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search_location:
                chooseLocation();
                return true;
            case R.id.action_home:
                ((MainActivity) getActivity()).onSectionAttached(Const.PAGE_HOME_SEARCH);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void chooseLocation() {
        Intent intent = new Intent(getActivity(), ChooseLocationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        isCancel = true;
        super.onPause();
    }

    @Override
    public void onResume() {
        boolean isDataChange = App.getDataMemberIsChanged();
        if (isDataChange) {
            refreshData();
        }
        super.onResume();
    }

}