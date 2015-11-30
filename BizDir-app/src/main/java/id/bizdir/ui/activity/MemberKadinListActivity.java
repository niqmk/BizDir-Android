package id.bizdir.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;

import java.util.List;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.modelhelper.AnggotaHelper;
import id.bizdir.model.Anggota;
import id.bizdir.model.City;
import id.bizdir.model.Province;
import id.bizdir.ui.adapter.MemberKadinAdapter;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;


public class MemberKadinListActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    private CharSequence mTitle;
    private View mImageView;
    private View mListBackgroundView;
    private int mParallaxImageHeight;
    private ObservableListView mListView;
    private List<Anggota> listMemberKadin;
    private TextView textNoData;
    private City city;
    private Province province;
    private int objectIndex;
    private AnggotaHelper memberKadinHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_sub);
        Helpers.setLockOrientation(MemberKadinListActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mImageView = findViewById(R.id.adsView);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.banner_image_height);
        mListView = (ObservableListView) findViewById(R.id.list);
        mListView.setScrollViewCallbacks(this);
        textNoData = (TextView) findViewById(R.id.textNoData);
        ImageView imageButtonAds = (ImageView) findViewById(R.id.imageButtonAds);
        ImageView imageAds = (ImageView) findViewById(R.id.image);
        //mListView.setOnItemClickListener(setOnItemClickListener);

        View paddingView = new View(this);
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
                mParallaxImageHeight);
        paddingView.setLayoutParams(lp);
        paddingView.setClickable(true);

        mListView.addHeaderView(paddingView);

        getDataFromPreviousPage();
        setTitle(mTitle);

        mListBackgroundView = findViewById(R.id.list_background);
        final View contentView = getWindow().getDecorView().findViewById(android.R.id.content);
        contentView.post(new Runnable() {
            @Override
            public void run() {
                mListBackgroundView.getLayoutParams().height = contentView.getHeight();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Helpers.setMainActionBar(actionBar);
        }
        Helpers.getLocalAds(MemberKadinListActivity.this, imageAds, imageButtonAds, Const.ADS_ZONE_ID_LIST_MEMBER);
        Helpers.getRemoteAds(MemberKadinListActivity.this, imageAds, imageButtonAds, Const.ADS_ZONE_ID_LIST_MEMBER);
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

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mListView.getCurrentScrollY(), false, false);
    }

    private void getDataFromPreviousPage() {
        Intent intent = this.getIntent();
        String toolbarTitle = intent.getStringExtra(Const.TOOLBAR_TITLE);
        objectIndex = intent.getIntExtra(Const.OBJECT_INDEX, 0);
        mTitle = toolbarTitle;
        memberKadinHelper = new AnggotaHelper();
        listMemberKadin = memberKadinHelper.getAll(objectIndex);
        refreshList();
    }

    private void refreshList() {
        MemberKadinAdapter adapter = new MemberKadinAdapter(this,
                R.layout.item_menu_member,
                listMemberKadin);
        mListView.setAdapter(adapter);
        if (listMemberKadin.size() > 0) {
            textNoData.setVisibility(View.GONE);
        } else {
            textNoData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_fragment_category, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_home:
                Intent intent = new Intent(MemberKadinListActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                return true;
            case R.id.action_search_location:
                chooseLocation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void chooseLocation() {
        Intent intent = new Intent(MemberKadinListActivity.this, ChooseLocationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        boolean isDataChange = App.getDataMemberIsChanged();
        if (isDataChange) {
            getData();
        }
        super.onResume();
    }

    private void getData() {
        city = App.getCity();
        province = App.getProvince();
        if (city == null && province == null) {
            city = new City();
            city.setId(0);
            city.setName(getString(R.string.location_indonesia));
            city.setProvinceId(0);
            city.setName(getString(R.string.location_indonesia));
            province = new Province();
            province.setId(0);
            province.setName(getString(R.string.location_indonesia));
            App.setCity(city);
            App.setProvince(province);
            listMemberKadin = memberKadinHelper.getAll(objectIndex);
        } else {
            if (province.getId() > 0) {
                listMemberKadin = memberKadinHelper.getAll(objectIndex, province.getId());
            } else {
                listMemberKadin = memberKadinHelper.getAll(objectIndex);
            }

            if (city.getId() > 0) {
                listMemberKadin = memberKadinHelper.getAll(objectIndex, province.getId(), city.getId());
            } else {
                if (city.getProvinceId() > 0) {
                    listMemberKadin = memberKadinHelper.getAll(objectIndex, province.getId());
                } else {
                    listMemberKadin = memberKadinHelper.getAll(objectIndex);
                }
                listMemberKadin = memberKadinHelper.getAll(objectIndex);
            }
        }
        App.setDataMemberIsChanged(false);
        refreshList();
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
