package id.bizdir.ui.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import id.bizdir.R;
import id.bizdir.ui.adapter.ChooseLocationStatePagerAdapter;
import id.bizdir.ui.widget.SlidingTabLayout;
import id.bizdir.util.Helpers;


public class ChooseLocationActivity extends AppCompatActivity {

    //private City city;
    //private Province province;
    private ViewPager mPager;
    private ChooseLocationStatePagerAdapter chooseLocationStatePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);
        Helpers.setLockOrientation(ChooseLocationActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mPager = (ViewPager) findViewById(R.id.pager);
        chooseLocationStatePagerAdapter = new ChooseLocationStatePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(chooseLocationStatePagerAdapter);
        setSlidingTabLayout();
        setSupportActionBar(toolbar);
        //getDataFromPreviousPage();
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Helpers.setMainActionBarNoShadow(actionBar);
        }

    }

    private void setSlidingTabLayout() {
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.material_drawer_accent));
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setViewPager(mPager);
        slidingTabLayout.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Helpers.hideKeyboard(ChooseLocationActivity.this);
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /*
    private void getDataFromPreviousPage() {
        Intent intent = this.getIntent();
        int objectIndex = intent.getIntExtra(Const.OBJECT_INDEX, 0);
        if (objectIndex != 0) {
            CityHelper cityHelper = new CityHelper();
            city = cityHelper.getCity(objectIndex);
            ProvinceHelper provinceHelper = new ProvinceHelper();
            province = provinceHelper.getProvince(city.getProvinceId());
        }
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
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
