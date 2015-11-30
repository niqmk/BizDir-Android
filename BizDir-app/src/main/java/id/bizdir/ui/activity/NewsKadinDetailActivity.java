package id.bizdir.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.modelhelper.NewsKadinHelper;
import id.bizdir.model.NewsKadin;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;
import id.bizdir.util.ShareSocial;


public class NewsKadinDetailActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    private View mView;
    private ObservableScrollView mScrollView;
    private NewsKadin newsKadin;
    private TextView textContent;
    private ImageView imageNews;
    private TextView textNewsTitle;
    private TextView textNewsDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_kadin_detail);
        Helpers.setLockOrientation(NewsKadinDetailActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mView = findViewById(R.id.mView);
        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
        textNewsTitle = (TextView) findViewById(R.id.textNewsTitle);
        textContent = (TextView) findViewById(R.id.textContent);
        textNewsDate = (TextView) findViewById(R.id.textNewsDate);
        imageNews = (ImageView) findViewById(R.id.imageNews);
        ImageView imageButtonAds = (ImageView) findViewById(R.id.imageButtonAds);
        ImageView imageAds = (ImageView) findViewById(R.id.image);
        getDataFromPreviousPage();

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Helpers.setMainActionBar(actionBar);
        }
        Helpers.getLocalAds(NewsKadinDetailActivity.this, imageAds, imageButtonAds, Const.ADS_ZONE_ID_NEWS_DETAIL);
        Helpers.getRemoteAds(NewsKadinDetailActivity.this, imageAds, imageButtonAds, Const.ADS_ZONE_ID_NEWS_DETAIL);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        ViewHelper.setTranslationY(mView, scrollY / 2);
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
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    private void getDataFromPreviousPage() {
        Intent intent = this.getIntent();
        String jsonString = intent.getStringExtra(Const.OBJECT_JSON);
        int objectIndex = intent.getIntExtra(Const.OBJECT_INDEX, 0);
        NewsKadinHelper newsKadinHelper = new NewsKadinHelper();
        if (!TextUtils.isEmpty(jsonString)) {
            try {
                newsKadin = App.getGson().fromJson(jsonString, NewsKadin.class);
            } catch (Exception ignore) {
                newsKadin = newsKadinHelper.get(objectIndex);
            }
        } else {
            newsKadin = newsKadinHelper.get(objectIndex);
        }

        if (newsKadin != null) {
            textNewsTitle.setText(newsKadin.getTitle());
            //textContent.setText(Html.fromHtml(newsKadin.getDescription()));
            textContent.setText(Helpers.cleanString(newsKadin.getDescription()));
            textNewsDate.setText(Helpers.DateToStringPublishDate(newsKadin.getCreateDate()));
            if (!newsKadin.getPicture().isEmpty()) {
                Glide.with(this)
                        .load(newsKadin.getPicture())
                        .centerCrop()
                        .crossFade()
                        .into(imageNews);
            } else {
                imageNews.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (menu.size() > 0) {
            menu.clear();
        }
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_share:
                shareThis();
                return true;
            case R.id.action_home:
                Intent intent = new Intent(NewsKadinDetailActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void shareThis() {
        String subject = newsKadin.getTitle();
        String message = Helpers.DateToStringPublishDate(newsKadin.getCreateDate()) + ". " +
                Helpers.cleanString(newsKadin.getDescription());
        ShareSocial.postText(NewsKadinDetailActivity.this, subject, message);
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
