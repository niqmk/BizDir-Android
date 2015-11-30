package id.bizdir.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.modelhelper.CityHelper;
import id.bizdir.model.City;
import id.bizdir.model.Weather;
import id.bizdir.ui.adapter.WeatherDetailAdapter;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;


public class WeatherDetailActivity extends AppCompatActivity {

    private List<Weather> list;
    private City city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_detail);
        Helpers.setLockOrientation(WeatherDetailActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setAdsViewButton();
        getDataFromPreviousPage();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Helpers.setMainActionBar(actionBar);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void setAdsViewButton() {
        ImageView imageButtonAds = (ImageView) findViewById(R.id.imageButtonAds);
        ImageView imageAds = (ImageView) findViewById(R.id.image);
        Helpers.getLocalAds(WeatherDetailActivity.this, imageAds, imageButtonAds, Const.ADS_ZONE_ID_WEATHER);
        Helpers.getRemoteAds(WeatherDetailActivity.this, imageAds, imageButtonAds, Const.ADS_ZONE_ID_WEATHER);
    }

    private void getDataFromPreviousPage() {
        Intent intent = this.getIntent();
        int objectIndex = intent.getIntExtra(Const.OBJECT_INDEX, 0);
        if (objectIndex != 0) {
            CityHelper cityHelper = new CityHelper();
            city = cityHelper.get(objectIndex);
            if (city != null) {
                setDataHeaderBind();
            }
            TextView textNoData = (TextView) findViewById(R.id.textNoData);
            //WeatherHelper helper = new WeatherHelper();
            list = App.getWeatherForecast();
            if (list != null) {
                if (list.size() > 0) {
                    textNoData.setVisibility(View.GONE);
                    setTodayWeatherType();
                    setListWeather();
                } else {
                    textNoData.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void setDataHeaderBind() {
        TextView textCity = (TextView) findViewById(R.id.textCity);
        textCity.setText(city.getName());
    }

    private void setTodayWeatherType() {
        TextView textWeatherType = (TextView) findViewById(R.id.textWeatherType);
        textWeatherType.setText(list.get(0).getWeather());
        TextView textTodayCelsius = (TextView) findViewById(R.id.textTodayCelsius);
        Integer todayCelsius = list.get(0).getWeatherMax();
        textTodayCelsius.setText(todayCelsius + "°C");
    }

    private void setListWeather() {
        ListView listView = (ListView) findViewById(R.id.list);
        ArrayAdapter<Weather> adapter = new WeatherDetailAdapter(this,
                R.layout.item_weather_detail, list);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_home:
                Intent intent = new Intent(WeatherDetailActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
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
