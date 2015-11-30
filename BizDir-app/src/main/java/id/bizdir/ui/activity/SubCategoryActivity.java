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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableListView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;

import java.util.List;

import id.bizdir.R;
import id.bizdir.modelhelper.AnggotaSubCategoryHelper;
import id.bizdir.model.AnggotaSubCategory;
import id.bizdir.ui.adapter.CategorySubAdapter;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;


public class SubCategoryActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {

    private CharSequence mTitle;
    private View mImageView;
    private View mListBackgroundView;
    private int mParallaxImageHeight;
    private ObservableListView mListView;
    private List<AnggotaSubCategory> listCategory;
    private TextView textNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_sub);
        Helpers.setLockOrientation(SubCategoryActivity.this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mImageView = findViewById(R.id.adsView);
        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.banner_image_height);
        mListView = (ObservableListView) findViewById(R.id.list);
        mListView.setScrollViewCallbacks(this);
        mListView.setOnItemClickListener(setOnItemClickListener);
        textNoData = (TextView) findViewById(R.id.textNoData);
        ImageView imageButtonAds = (ImageView) findViewById(R.id.imageButtonAds);
        ImageView imageAds = (ImageView) findViewById(R.id.image);

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
        Helpers.getLocalAds(SubCategoryActivity.this, imageAds, imageButtonAds, Const.ADS_ZONE_ID_SUB_GROUP);
        Helpers.getRemoteAds(SubCategoryActivity.this, imageAds, imageButtonAds, Const.ADS_ZONE_ID_SUB_GROUP);
    }

    AdapterView.OnItemClickListener setOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            AnggotaSubCategory categorySub = listCategory.get(position - 1);
            if (categorySub != null) {
                //Intent intent = new Intent(SubCategoryActivity.this, MemberKadinDetailActivity.class);
                Intent intent = new Intent(SubCategoryActivity.this, MemberKadinListActivity.class);
                intent.putExtra(Const.TOOLBAR_TITLE, categorySub.getTitle());
                intent.putExtra(Const.OBJECT_INDEX, categorySub.getId());
                startActivity(intent);
            }
        }
    };

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
        int objectIndex = intent.getIntExtra(Const.OBJECT_INDEX, 0);
        mTitle = toolbarTitle;
        AnggotaSubCategoryHelper categorySubHelper = new AnggotaSubCategoryHelper();
        listCategory = categorySubHelper.getAll(objectIndex);
        ArrayAdapter<AnggotaSubCategory> adapter = new CategorySubAdapter(this,
                R.layout.item_menu_category_sub,
                listCategory);
        mListView.setAdapter(adapter);
        if (listCategory.size() > 0) {
            textNoData.setVisibility(View.GONE);
        } else {
            textNoData.setVisibility(View.VISIBLE);
        }
    }

    /*
    public static void launch(Activity activity, View transitionView, Category category) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, transitionView, EXTRA_IMAGE);
        Intent intent = new Intent(activity, SubCategoryActivity.class);
        intent.putExtra(Const.TOOLBAR_TITLE, category.getName());
        intent.putExtra(Const.OBJECT_INDEX, category.getId());
        ActivityCompat.startActivity(activity, intent, options.toBundle());
    }
    */

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
                Intent intent = new Intent(SubCategoryActivity.this, MainActivity.class);
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
