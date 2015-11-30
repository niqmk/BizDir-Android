package id.bizdir.ui.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ksoichiro.android.observablescrollview.Scrollable;
import com.github.ksoichiro.android.observablescrollview.TouchInterceptionFrameLayout;
import com.nineoldandroids.view.ViewHelper;

import java.io.IOException;
import java.sql.SQLException;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.model.ResultObject;
import id.bizdir.model.ResultObjectHelper;
import id.bizdir.modelhelper.AnggotaHelper;
import id.bizdir.model.Anggota;
import id.bizdir.service.AnggotaDetailService;
import id.bizdir.ui.adapter.ButtonItemAdapter;
import id.bizdir.ui.adapter.MemberDetailStatePagerAdapter;
import id.bizdir.ui.widget.SlidingTabLayout;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 02/05/2015.
 */
public class MemberKadinDetailActivity extends AppCompatActivity implements ObservableScrollViewCallbacks {
    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    private View mImageView;
    private View mOverlayView;
    private TextView mTitleView;
    //private ImageView imageVerified;
    private ImageView logoMember;
    private ImageView logoMemberBackground;
    private ImageView imageMemberBackground;
    private TouchInterceptionFrameLayout mInterceptionLayout;
    private ViewPager mPager;
    private MemberDetailStatePagerAdapter mPagerAdapter;
    private int mSlop;
    private int mFlexibleSpaceHeight;
    private int mTabHeight;
    private boolean mScrolled;
    private Anggota memberKadin;
    private Menu menu;
    private int favorite;
    private AnggotaHelper memberKadinHelper;
    private int memberId;
    protected SlidingTabLayout slidingTabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_member_kadin_detail);
        Helpers.setLockOrientation(MemberKadinDetailActivity.this);
        //setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            Helpers.setMainActionBar(actionBar);
        }

        mTitleView = (TextView) findViewById(R.id.title);
        logoMember = (ImageView) findViewById(R.id.logoMember);
        logoMemberBackground = (ImageView) findViewById(R.id.logoMemberBackground);
        imageMemberBackground = (ImageView) findViewById(R.id.image);
        // imageVerified = (ImageView) findViewById(R.id.imageVerified);
        //imageVerified.setVisibility(View.GONE);

        ViewCompat.setElevation(findViewById(R.id.header), getResources().getDimension(R.dimen.toolbar_elevation));
        mPager = (ViewPager) findViewById(R.id.pager);

        mImageView = findViewById(R.id.image);
        mOverlayView = findViewById(R.id.overlay);
        // Padding for ViewPager must be set outside the ViewPager itself
        // because with padding, EdgeEffect of ViewPager become strange.
        mFlexibleSpaceHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mTabHeight = getResources().getDimensionPixelSize(R.dimen.tab_height);
        findViewById(R.id.pager_wrapper).setPadding(0, mFlexibleSpaceHeight, 0, 0);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);
        slidingTabLayout.setCustomTabView(R.layout.tab_indicator, android.R.id.text1);
        slidingTabLayout.setSelectedIndicatorColors(getResources().getColor(R.color.material_drawer_accent));
        slidingTabLayout.setDistributeEvenly(true);

        getDataFromPreviousPage();
        getAnggotaDetail();
        //mTitleView.setText(getTitle());
        setTitle(null);


        //slidingTabLayout.setViewPager(mPager);
        //((FrameLayout.LayoutParams) slidingTabLayout.getLayoutParams()).topMargin = mFlexibleSpaceHeight - mTabHeight;

        ViewConfiguration vc = ViewConfiguration.get(this);
        mSlop = vc.getScaledTouchSlop();
        mInterceptionLayout = (TouchInterceptionFrameLayout) findViewById(R.id.container);
        mInterceptionLayout.setScrollInterceptionListener(mInterceptionListener);
        ScrollUtils.addOnGlobalLayoutListener(mInterceptionLayout, new Runnable() {
            @Override
            public void run() {
                updateFlexibleSpace();
            }
        });
        if (savedInstanceState != null) {
            favorite = savedInstanceState.getInt("favorite");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("favorite", favorite);
    }

    private void getAnggotaDetail() {
        class AnggotaDetailAsyncTask extends AsyncTask<Void, Void, String> {
            String response = "";
            final MaterialDialog progressDialog = new MaterialDialog.Builder(
                    new ContextThemeWrapper(MemberKadinDetailActivity.this,
                            R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title("Loading")
                    .content("Please wait...")
                    .progress(true, 0)
                    .cancelable(false)
                    .show();

            @Override
            protected String doInBackground(Void... param) {
                response = "";
                try {
                    AnggotaDetailService service = new AnggotaDetailService();
                    response = service.getAnggotaDetail(memberId);
                } catch (IOException e) {
                    response = e.getMessage();
                    e.printStackTrace();
                }
                return response;
            }

            @Override
            protected void onPostExecute(String resultJson) {
                progressDialog.dismiss();
                try {
                    ResultObject resultObject = ResultObjectHelper.getResult(resultJson);
                    onSuccessWs(resultObject);
                } catch (Exception ignore) {

                }
            }
        }
        AnggotaDetailAsyncTask task = new AnggotaDetailAsyncTask();
        task.execute();
    }

    private void onSuccessWs(ResultObject resultObject) {
        if (resultObject != null) {
            int status = resultObject.getStatus();
            if (status == 1) {
                String jsonString = resultObject.getResult();
                if (!TextUtils.isEmpty(jsonString)) {
                    try {
                        memberKadin = App.getGson().fromJson(jsonString,
                                Anggota.class);
                        //memberKadin = anggota;
                        if (memberKadin != null) {
                            memberKadin.setFavorite(favorite);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //mPagerAdapter = new MemberDetailStatePagerAdapter(getSupportFragmentManager(), memberKadin);
                                    //mPager.setAdapter(mPagerAdapter);

                                    refreshDataDetail();
                                }
                            });
                        }
                    } catch (Exception ignore) {

                    }
                }

            }
        }
    }

    private void getDataFromPreviousPage() {
        Intent intent = this.getIntent();
        int objectIndex = intent.getIntExtra(Const.OBJECT_INDEX, 0);
        memberId = objectIndex;
        if (objectIndex != 0) {
            memberKadinHelper = new AnggotaHelper();
            memberKadin = memberKadinHelper.get(objectIndex);
            refreshDataDetail();
        }
        //MemberKadinHelper memberKadinHelper = new MemberKadinHelper();
        //listMemberKadin = memberKadinHelper.getAll(objectIndex);
        //ArrayAdapter<MemberKadin> adapter = new MemberKadinAdapter(this,
        //        R.layout.item_menu_member,
        //        listMemberKadin);
        //mListView.setAdapter(adapter);
    }

    private void refreshDataDetail() {
        if (memberKadin != null) {
            memberId = memberKadin.getId();
            favorite = memberKadin.getFavorite();
            mPagerAdapter = new MemberDetailStatePagerAdapter(getSupportFragmentManager(), memberKadin);
            mPager.setAdapter(mPagerAdapter);
            mTitleView.setText(memberKadin.getName());
            slidingTabLayout.setViewPager(mPager);
            ((FrameLayout.LayoutParams) slidingTabLayout.getLayoutParams()).topMargin = mFlexibleSpaceHeight - mTabHeight;
                /*
                imageVerified.setVisibility(View.VISIBLE);
                if (memberKadin.getVerification() > 0) {
                    imageVerified.setImageResource(R.drawable.btn_verified);
                } else {
                    imageVerified.setImageResource(R.drawable.btn_unverified);
                }*/

            if (memberKadin.getThumb() != null) {
                String logoUrl = memberKadin.getThumb();
                if (!logoUrl.isEmpty()) {
                    Glide.with(this)
                            .load(logoUrl)
                            .centerCrop()
                            .crossFade()
                            .into(new GlideDrawableImageViewTarget(logoMember) {
                                @Override
                                public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                                    super.onResourceReady(drawable, anim);
                                    logoMemberBackground.setVisibility(View.VISIBLE);
                                    logoMember.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                    logoMember.setVisibility(View.GONE);
                                    logoMemberBackground.setVisibility(View.GONE);
                                }
                            });
                } else {
                    logoMember.setVisibility(View.GONE);
                    logoMemberBackground.setVisibility(View.GONE);
                }
            }

            if (memberKadin.getPicture() != null) {
                String logoBackgroundUrl = memberKadin.getPicture();
                if (!logoBackgroundUrl.isEmpty()) {
                    Glide.with(this)
                            .load(logoBackgroundUrl)
                            .centerCrop()
                            .crossFade()
                            .into(imageMemberBackground);
                } else {
                    imageMemberBackground.setImageResource(R.drawable.bg_company_detail_default);
                }
            }
        }
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    private void checkIconFavorite(final Menu beforeMenu) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (beforeMenu != null) {
                    MenuItem item = beforeMenu.findItem(R.id.action_favorite_member);
                    if (item != null) {
                        if (favorite > 0) {
                            item.setIcon(getResources().getDrawable(R.drawable.ic_star_white_24dp));
                        } else {
                            item.setIcon(getResources().getDrawable(R.drawable.ic_star_border_white_24dp));
                        }
                    }
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_member_kadin_detail, menu);
        checkIconFavorite(menu);
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_home:
                Intent intent = new Intent(MemberKadinDetailActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                this.finish();
                return true;
            case R.id.action_favorite_member:
                onFavoriteClick();
                return true;
            case R.id.action_verification:
                cekVerification();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void cekVerification() {
        if (memberKadin.getVerification() > 0) {
            new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title(R.string.dialog_verification_ok_title)
                    .content(R.string.dialog_verification_ok_message)
                    .positiveText(R.string.button_ok)
                    .show();
        } else {
            new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title(R.string.dialog_verification_ok_title)
                    .content(R.string.dialog_verification_message)
                    .positiveText(R.string.button_verification)
                    .negativeText(R.string.button_close)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            showCustomList();
                        }
                    })
                    .show();
        }
    }

    private void showCustomList() {
        int arrayList;
        int arrayInfoList;
        final boolean isEmptyEmail = memberKadin.getEmail().isEmpty();
        if (!isEmptyEmail) {
            arrayList = R.array.verification_full_list;
            arrayInfoList = R.array.verification_full_info;
        } else {
            arrayList = R.array.verification_list;
            arrayInfoList = R.array.verification_info;
        }
        new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                .title(R.string.button_verification)
                .adapter(new ButtonItemAdapter(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar),
                                arrayList, arrayInfoList),
                        new MaterialDialog.ListCallback() {
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int index, CharSequence text) {
                                if (!isEmptyEmail) {
                                    switch (index) {
                                        case 0:
                                            sendEmailVerification();
                                            dialog.dismiss();
                                            break;
                                        case 1:
                                            openFormCannotAccessMyEmail();
                                            dialog.dismiss();
                                            break;
                                        case 2:
                                            openFormNotYetKadinMember();
                                            dialog.dismiss();
                                            break;
                                    }
                                } else {
                                    switch (index) {
                                        case 0:
                                            openFormCannotAccessMyEmail();
                                            dialog.dismiss();
                                            break;
                                        case 1:
                                            openFormNotYetKadinMember();
                                            dialog.dismiss();
                                            break;
                                    }
                                }
                            }
                        })
                .negativeText(R.string.button_close)
                .show();
    }

    private void sendEmailVerification() {
        new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                .title(R.string.dialog_verification_ok_title)
                .content("Please check this email :\n" + memberKadin.getEmail() + ",\nthen follow the intructions to verify.")
                .positiveText(R.string.button_ok)
                .show();
    }

    private void openFormCannotAccessMyEmail() {
        Intent intent = new Intent(MemberKadinDetailActivity.this, VerificationFormNewEmailActivity.class);
        intent.putExtra(Const.OBJECT_INDEX, memberKadin.getId());
        startActivity(intent);
        /*
        new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                .title(R.string.dialog_verification_ok_title)
                .content("Sorry, still under construction. Please check back later!")
                .positiveText(R.string.button_ok)
                .show();
                */
    }

    private void openFormNotYetKadinMember() {
        Intent intent = new Intent(MemberKadinDetailActivity.this, VerificationFormNotMemberActivity.class);
        intent.putExtra(Const.OBJECT_INDEX, memberKadin.getId());
        startActivity(intent);
        /*
        new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                .title(R.string.dialog_verification_ok_title)
                .content("Sorry, still under construction. Please check back later!")
                .positiveText(R.string.button_ok)
                .show();
                */
    }

    public void onFavoriteClick() {
        MaterialDialog.Builder materialDialog;
        if (favorite > 0) {
            materialDialog = new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title(R.string.member_remove_favorite_title)
                    .content(R.string.member_remove_favorite_message)
                    .positiveText(R.string.button_ok)
                    .negativeText(R.string.button_cancel)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            try {
                                memberKadinHelper.updateFavorite(memberKadin.getId(), false);
                                favorite = 0;
                                setIconFavorite(false);
                                App.setDataMemberIsChanged(true);
                            } catch (SQLException e) {
                                Toast.makeText(getBaseContext(), getString(R.string.error_remove_favorite),
                                        Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
        } else {
            materialDialog = new MaterialDialog.Builder(new ContextThemeWrapper(this, R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title(R.string.member_add_favorite_title)
                    .content(R.string.member_add_favorite_message)
                    .positiveText(R.string.button_ok)
                    .negativeText(R.string.button_cancel)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            try {
                                memberKadin.setFavorite(1);
                                memberKadinHelper.add(memberKadin);
                                //memberKadinHelper.updateFavorite(memberKadin.getId(), true);
                                favorite = 1;
                                setIconFavorite(true);
                                App.setDataMemberIsChanged(true);
                            } catch (Exception e) {
                                Toast.makeText(getBaseContext(), getString(R.string.error_add_favorite),
                                        Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    });
        }
        materialDialog.show();
    }

    private void setIconFavorite(final boolean isFavorite) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (menu != null) {
                    MenuItem item = menu.findItem(R.id.action_favorite_member);
                    if (item != null) {
                        if (isFavorite) {
                            item.setIcon(getResources().getDrawable(R.drawable.ic_star_white_24dp));
                        } else {
                            item.setIcon(getResources().getDrawable(R.drawable.ic_star_border_white_24dp));
                        }
                    }
                }
            }
        });
    }

    private TouchInterceptionFrameLayout.TouchInterceptionListener mInterceptionListener = new TouchInterceptionFrameLayout.TouchInterceptionListener() {
        @Override
        public boolean shouldInterceptTouchEvent(MotionEvent ev, boolean moving, float diffX, float diffY) {
            if (!mScrolled && mSlop < Math.abs(diffX) && Math.abs(diffY) < Math.abs(diffX)) {
                // Horizontal scroll is maybe handled by ViewPager
                return false;
            }

            Scrollable scrollable = getCurrentScrollable();
            if (scrollable == null) {
                mScrolled = false;
                return false;
            }

            // If interceptionLayout can move, it should intercept.
            // And once it begins to move, horizontal scroll shouldn't work any longer.
            int flexibleSpace = mFlexibleSpaceHeight - mTabHeight;
            int translationY = (int) ViewHelper.getTranslationY(mInterceptionLayout);
            boolean scrollingUp = 0 < diffY;
            boolean scrollingDown = diffY < 0;
            if (scrollingUp) {
                if (translationY < 0) {
                    mScrolled = true;
                    return true;
                }
            } else if (scrollingDown) {
                if (-flexibleSpace < translationY) {
                    mScrolled = true;
                    return true;
                }
            }
            mScrolled = false;
            return false;
        }

        @Override
        public void onDownMotionEvent(MotionEvent ev) {
        }

        @Override
        public void onMoveMotionEvent(MotionEvent ev, float diffX, float diffY) {
            int flexibleSpace = mFlexibleSpaceHeight - mTabHeight;
            float translationY = ScrollUtils.getFloat(ViewHelper.getTranslationY(mInterceptionLayout) + diffY, -flexibleSpace, 0);
            updateFlexibleSpace(translationY);
            if (translationY < 0) {
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInterceptionLayout.getLayoutParams();
                lp.height = (int) (-translationY + getScreenHeight());
                mInterceptionLayout.requestLayout();
            }
        }

        @Override
        public void onUpOrCancelMotionEvent(MotionEvent ev) {
            mScrolled = false;
        }
    };

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    private Scrollable getCurrentScrollable() {
        Fragment fragment = getCurrentFragment();
        if (fragment == null) {
            return null;
        }
        View view = fragment.getView();
        if (view == null) {
            return null;
        }
        return (Scrollable) view.findViewById(R.id.scroll);
    }

    private void updateFlexibleSpace() {
        updateFlexibleSpace(ViewHelper.getTranslationY(mInterceptionLayout));
    }

    private void updateFlexibleSpace(float translationY) {
        ViewHelper.setTranslationY(mInterceptionLayout, translationY);
        int minOverlayTransitionY = getActionBarSize() - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-translationY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        float flexibleRange = mFlexibleSpaceHeight - getActionBarSize();
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat(-translationY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange + translationY - mTabHeight) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
        setPivotXToTitle();
        ViewHelper.setPivotY(mTitleView, 0);
        ViewHelper.setScaleX(mTitleView, scale);
        ViewHelper.setScaleY(mTitleView, scale);
    }

    private Fragment getCurrentFragment() {
        return mPagerAdapter.getItemAt(mPager.getCurrentItem());
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private void setPivotXToTitle() {
        Configuration config = getResources().getConfiguration();
        if (Build.VERSION_CODES.JELLY_BEAN_MR1 <= Build.VERSION.SDK_INT
                && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            ViewHelper.setPivotX(mTitleView, findViewById(android.R.id.content).getWidth());
        } else {
            ViewHelper.setPivotX(mTitleView, 0);
        }
    }

    /**
     * This adapter provides two types of fragments as an example.
     * {@linkplain #createItem(int)} should be modified if you use this example for your app.
     */

}
