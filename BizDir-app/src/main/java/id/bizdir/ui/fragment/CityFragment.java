package id.bizdir.ui.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.list.WeatherList;
import id.bizdir.modelhelper.CityHelper;
import id.bizdir.modelhelper.ProvinceHelper;
import id.bizdir.model.City;
import id.bizdir.model.Province;
import id.bizdir.model.ResultObject;
import id.bizdir.model.ResultObjectHelper;
import id.bizdir.service.AllSync;
import id.bizdir.service.WeatherService;
import id.bizdir.ui.adapter.LocationCityAdapter;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 02/05/2015.
 */
public class CityFragment extends Fragment {

    private List<City> listObject;
    private TextView textNoData;
    private String currentQuery = null;
    private LocationCityAdapter adapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ListView list;

    public CityFragment newInstance() {
        CityFragment fragment = new CityFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
        }

        //final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.MaterialDrawerTheme_BizDir);
        //LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View view = inflater.inflate(R.layout.fragment_location, container, false);
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
        CityHelper helper = new CityHelper();
        listObject = helper.getAll();
        adapter = new LocationCityAdapter(getActivity(),
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
                    response = AllSync.syncCity();
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
                    AllSync.insertCitySync(jsonString);
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

    private void handelListItemClick(City obj) {
        Province province = new Province();
        if (obj.getId() == 0) {
            province.setId(0);
            province.setName(getString(R.string.location_indonesia));
        } else {
            ProvinceHelper provinceHelper = new ProvinceHelper();
            province = provinceHelper.get(obj.getProvinceId());
            if (province == null) {
                province = provinceHelper.getIndonesia();
            }
        }
        App.setCity(obj);
        App.setProvince(province);
        App.setDataMemberIsChanged(true);
        if (obj.getId() == 0) {
            getActivity().finish();
        } else {
            getWeatherFromWs(obj.getId());
        }
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

    private void getWeatherFromWs(final Integer cityId) {
        class NewsStockAsyncTask extends AsyncTask<Integer, Void, String> {
            String response = "";

            final MaterialDialog progressDialog = new MaterialDialog.Builder(getActivity())
                    .title("Loading")
                    .content("Please wait...")
                    .progress(true, 0)
                    .cancelable(false)
                    .show();

            @Override
            protected String doInBackground(Integer... param) {
                response = "";
                try {
                    WeatherService service = new WeatherService();
                    response = service.getWeather(cityId.toString());
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
                                bindDataWeather(jsonString);
                                progressDialog.dismiss();
                                getActivity().finish();
                            } else {
                                progressDialog.dismiss();
                                String title = getResources().getString(R.string.dialog_get_weather_failed);
                                String content = getResources().getString(R.string.dialog_get_weather_failed_message);
                                Helpers.showDialog(getActivity(), title, content);
                            }
                        }
                    } catch (Exception ignore) {
                        progressDialog.dismiss();
                        String title = getResources().getString(R.string.dialog_get_weather_failed);
                        String content = getResources().getString(R.string.dialog_get_weather_failed_message);
                        Helpers.showDialog(getActivity(), title, content);
                    }
                }
            }
        }
        NewsStockAsyncTask task = new NewsStockAsyncTask();
        task.execute();
    }

    private void bindDataWeather(String jsonString) {
        if (!TextUtils.isEmpty(jsonString)) {
            try {
                WeatherList list = App.getGson().fromJson(jsonString, WeatherList.class);
                int listCount = list.forecast.size();
                if (listCount > 0) {
                    App.setWeatherForecast(list.forecast);
                }
            } catch (Exception ignore) {
            }
        }
    }

}