package id.bizdir.ui.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.modelhelper.ProvinceHelper;
import id.bizdir.model.City;
import id.bizdir.model.Province;
import id.bizdir.model.ResultObject;
import id.bizdir.model.ResultObjectHelper;
import id.bizdir.service.AllSync;
import id.bizdir.ui.adapter.LocationProvinceAdapter;

/**
 * Created by Hendry on 02/05/2015.
 */
public class ProvinceFragment extends Fragment {

    private List<Province> listObject;
    private TextView textNoData;
    private LocationProvinceAdapter adapter;
    private String currentQuery = null;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView list;

    public ProvinceFragment newInstance() {
        ProvinceFragment fragment = new ProvinceFragment();
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
        View view = localInflater.inflate(R.layout.fragment_location, container, false);
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
        currentQuery = "";
        getData();
        return view;
    }

    private void getData() {
        ProvinceHelper helper = new ProvinceHelper();
        listObject = helper.getAll();
        adapter = new LocationProvinceAdapter(getActivity(),
                R.layout.item_location, listObject);
        list.setAdapter(adapter);
        list.setOnItemClickListener(setOnItemClickListener);
        if (listObject.size() > 0) {
            textNoData.setVisibility(View.GONE);
        } else {
            textNoData.setVisibility(View.VISIBLE);
        }
    }

    private void refreshData() {
        class GetAllSyncTask extends AsyncTask<String, Void, String> {
            String response = "";

            private void hideProgress() {
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }

            @Override
            protected String doInBackground(String... param) {
                response = "";
                try {
                    response = AllSync.syncProvince();
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
                    AllSync.insertProvinceSync(jsonString);
                }
                getData();
                hideProgress();
            }
        }
        GetAllSyncTask task = new GetAllSyncTask();
        task.execute();
    }

    AdapterView.OnItemClickListener setOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            handelListItemClick(adapter.getItem(position));
        }
    };

    private void handelListItemClick(Province obj) {
        City city = new City();
        if (obj.getId() == 0) {
            city.setId(0);
            city.setName(getString(R.string.location_indonesia));
            city.setProvinceId(0);
            city.setProvinceName(getString(R.string.location_indonesia));
        } else {
            city.setId(0);
            city.setName(getString(R.string.location_indonesia));
            city.setProvinceId(obj.getId());
            city.setProvinceName(obj.getName());
        }
        App.setCity(city);
        App.setProvince(obj);
        App.setDataMemberIsChanged(true);
        getActivity().finish();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu.size() > 0) {
            menu.clear();
        }
        inflater.inflate(R.menu.fragment_location, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
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
                adapter.getFilter().filter(newText);
                currentQuery = newText;
                return true;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}