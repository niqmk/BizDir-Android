package id.bizdir.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.modelhelper.InterestHelper;
import id.bizdir.modelhelper.MemberBizdirHelper;
import id.bizdir.model.MemberBizdir;
import id.bizdir.ui.fragment.AboutBizdirFragment;
import id.bizdir.ui.fragment.AssociationFragment;
import id.bizdir.ui.fragment.BusinessOpportunitiesMainFragment;
import id.bizdir.ui.fragment.CategoryMainHeaderGridViewFragment;
import id.bizdir.ui.fragment.DownloadFragment;
import id.bizdir.ui.fragment.EventCalendarMainFragment;
import id.bizdir.ui.fragment.FaqFragment;
import id.bizdir.ui.fragment.FavoriteFragment;
import id.bizdir.ui.fragment.ForumMainFragment;
import id.bizdir.ui.fragment.NewsMainFragment;
import id.bizdir.ui.fragment.PromotionFragment;
import id.bizdir.ui.fragment.SearchFragment;
import id.bizdir.ui.widget.DrawShadowFrameLayout;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;


public class MainActivity extends AppCompatActivity {

    private static final int PROFILE_SETTING = 1;
    private static final int PROFILE_LOGOUT = 2;
    private static final int ACTIVE_POSITION = 1;

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private DrawShadowFrameLayout drawShadowFrameLayout;
    private CharSequence mTitle;
    private int activePosition;
    private MemberBizdir memberBizdir;
    private MemberBizdirHelper memberBizdirHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        App.setAnalytic(getIntent());
        Helpers.setLockOrientation(MainActivity.this);
        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getMemberBizdir();
        mTitle = getTitle();
        drawShadowFrameLayout = (DrawShadowFrameLayout) findViewById(R.id.frame_container);
        //CategoryHelper categoryHelper = new CategoryHelper();
        //List<Category> listCategory = categoryHelper.getAll();

        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        //final IProfile profile = new ProfileDrawerItem().withName("Hendry Syamsudin").withEmail("hendry.syamsudin@gmail.com").withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460");
        //final IProfile profile2 = new ProfileDrawerItem().withName("Hendry Syamsudin").withEmail("hendry.syamsudin@gmail.com").withIcon(Uri.parse("https://avatars3.githubusercontent.com/u/887462?v=3&s=460"));
        //final IProfile profile3 = new ProfileDrawerItem().withName("Hendry Syamsudin").withEmail("hendry.syamsudin@gmail.com").withIcon(getResources().getDrawable(R.drawable.profile2));


        //final IProfile profile = new ProfileDrawerItem().withName("Hendry Syamsudin").withEmail("hendry.syamsudin@gmail.com").withIcon(ContextCompat.getDrawable(this, R.drawable.profile)).withIdentifier(PROFILE_SETTING);
        String fullName;
        String email;
        String loginLogout;

        if (memberBizdir != null) {
            fullName = memberBizdir.getName();
            email = memberBizdir.getEmail();
            loginLogout = "Logout";

        } else {
            fullName = "Guest";
            email = "Guest BizDir";
            loginLogout = "Login";
        }
        final IProfile profile = new ProfileDrawerItem().withName(fullName).withEmail(email).withIcon(Helpers.getIconName(fullName)).withIdentifier(PROFILE_SETTING);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                        //.withHeaderBackground(R.drawable.header)
                .withHeaderBackground(R.drawable.header3)
                .addProfiles(
                        profile,
                        new ProfileSettingDrawerItem().withName(loginLogout).withIcon(FontAwesome.Icon.faw_sign_out).withIdentifier(PROFILE_LOGOUT)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_SETTING) {
                            if (memberBizdir != null) {
                                gotoProfileDetail();
                            } else {
                                userLogout(false);
                            }
                        } else if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_LOGOUT) {
                            //Toast.makeText(getBaseContext(), "Logout",
                            //Toast.LENGTH_SHORT).show();
                            userLogout(false);
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withTranslucentStatusBarShadow(true)
                        //.withSliderBackgroundDrawable(getResources().getDrawable(R.drawable.background2))
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_search).withIcon(FontAwesome.Icon.faw_search).withIdentifier(Const.INDEX_MENU_SEARCH),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_favorite).withIcon(FontAwesome.Icon.faw_heart_o).withIdentifier(Const.INDEX_MENU_FAVORITE),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_category).withIcon(FontAwesome.Icon.faw_th).withIdentifier(Const.INDEX_MENU_CATEGORY),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_association).withIcon(FontAwesome.Icon.faw_users).withIdentifier(Const.INDEX_MENU_ASSOCIATION),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_opportunities).withIcon(FontAwesome.Icon.faw_dollar).withIdentifier(Const.INDEX_MENU_OPPORTUNITUES),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_news).withIcon(FontAwesome.Icon.faw_newspaper_o).withIdentifier(Const.INDEX_MENU_NEWS),
                        //new PrimaryDrawerItem().withDescription("ini sample desc").withName(R.string.drawer_item_complex_header_drawer).withIcon(GoogleMaterial.Icon.gmd_adb).withIdentifier(5).withCheckable(false),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_forum).withIcon(FontAwesome.Icon.faw_comments_o).withIdentifier(Const.INDEX_MENU_FORUM),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_promotion).withIcon(FontAwesome.Icon.faw_ticket).withIdentifier(Const.INDEX_MENU_PROMOTION),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_event_calendar).withIcon(FontAwesome.Icon.faw_calendar).withIdentifier(Const.INDEX_MENU_EVENT_CALENDAR),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_download).withIcon(FontAwesome.Icon.faw_download).withIdentifier(Const.INDEX_MENU_DOWNLOAD),
                        //new SectionDrawerItem().withName(R.string.drawer_item_header_other),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_about).withIcon(FontAwesome.Icon.faw_info_circle).withIdentifier(Const.INDEX_MENU_ABOUT),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_faq).withIcon(FontAwesome.Icon.faw_question_circle).withIdentifier(Const.INDEX_MENU_FAQ)
                        //new PrimaryDrawerItem().withName(R.string.drawer_item_logout).withIcon(FontAwesome.Icon.faw_sign_out).withIdentifier(13).withCheckable(false)
                        //new SecondaryDrawerItem().withName(R.string.drawer_item_logout).withIcon(FontAwesome.Icon.faw_power_off).withIdentifier(13).withCheckable(false)
                        //new DividerDrawerItem(),
                        //new SwitchDrawerItem().withName("Switch").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                        //new SwitchDrawerItem().withName("Switch2").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                        // new ToggleDrawerItem().withName("Toggle").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    //public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                    public boolean onItemClick(View view, int i, IDrawerItem iDrawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem
                        //IIcon test = FontAwesome.Icon.faw_info_circle;
                        //Drawable test = new IconicsDrawable(getApplicationContext(),
                        //        FontAwesome.Icon.faw_android).color(Color.RED).sizeDp(24);
                        //new IconicsDrawable(this, "faw-adjust").actionBarSize()
                        //new IconicsDrawable(this, FontAwesome.Icon.faw_adjust).sizeDp(24)
                        if (iDrawerItem != null) {
                            //Intent intent = null;

                            int selectedMenuIndex = iDrawerItem.getIdentifier();
                            switch (selectedMenuIndex) {
                                case Const.INDEX_MENU_FAVORITE:
                                case Const.INDEX_MENU_OPPORTUNITUES:
                                case Const.INDEX_MENU_FORUM:
                                case Const.INDEX_MENU_DOWNLOAD:
                                    if (memberBizdir == null) {
                                        userLogout(true);
                                        return false;
                                    }
                                    break;
                            }
                            onNavigationDrawerItemSelected(selectedMenuIndex);
                            result.setSelection(iDrawerItem.getIdentifier(), false);
                            /*
                            if (drawerItem.getId() == 1) {
                                //intent = new Intent(SimpleHeaderDrawerActivity.this, SimpleCompactHeaderDrawerActivity.class);
                            } else if (drawerItem.getId() == 2) {
                                //intent = new Intent(SimpleHeaderDrawerActivity.this, ActionBarDrawerActivity.class);
                            } else if (drawerItem.getId() == 3) {
                                //intent = new Intent(SimpleHeaderDrawerActivity.this, MultiDrawerActivity.class);
                            } else if (drawerItem.getId() == 4) {
                                //intent = new Intent(SimpleHeaderDrawerActivity.this, SimpleNonTranslucentDrawerActivity.class);
                            } else if (drawerItem.getId() == 5) {
                                //intent = new Intent(SimpleHeaderDrawerActivity.this, ComplexHeaderDrawerActivity.class);
                            } else if (drawerItem.getId() == 6) {
                                //intent = new Intent(SimpleHeaderDrawerActivity.this, SimpleFragmentDrawerActivity.class);
                            } else if (drawerItem.getId() == 7) {
                                //intent = new Intent(SimpleHeaderDrawerActivity.this, EmbeddedDrawerActivity.class);
                            } else if (drawerItem.getId() == 8) {
                                //intent = new Intent(SimpleHeaderDrawerActivity.this, FullscreenDrawerActivity.class);
                            } else if (drawerItem.getId() == 20) {
                                //intent = new Libs.Builder().withFields(R.string.class.getFields()).withActivityTheme(R.style.MaterialDrawerTheme_ActionBar).intent(SimpleHeaderDrawerActivity.this);
                            }
                            //SimpleHeaderDrawerActivity.this.startActivity(intent);
                            */

                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                        //.withShowDrawerOnFirstLaunch(true)
                .build();

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 1
            //activePosition = ACTIVE_POSITION;
            //result.setSelectionByIdentifier(activePosition, false);
            //onNavigationDrawerItemSelected(activePosition);
            gotoPage(ACTIVE_POSITION);
            //set the active profile
            headerResult.setActiveProfile(profile);
        } else {
            activePosition = savedInstanceState.getInt("activePosition");
        }
        setStyleAndTitle();
        //drawShadowFrameLayout.setShadowVisible(false, true);
    }

    /*
    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };*/

    private void getMemberBizdir() {
        memberBizdirHelper = new MemberBizdirHelper();
        memberBizdir = memberBizdirHelper.get();
    }

    private void gotoProfileDetail() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    private void userLogout(boolean needLogin) {
        String title;
        String message;
        if (memberBizdir != null) {
            title = Helpers.getString(R.string.app_logout_title);
            message = Helpers.getString(R.string.app_logout_message);
        } else {
            if (needLogin) {
                title = Helpers.getString(R.string.app_need_login_title);
                message = Helpers.getString(R.string.app_need_login_message);
            } else {
                title = Helpers.getString(R.string.app_login_title);
                message = Helpers.getString(R.string.app_login_message);
            }
        }

        new MaterialDialog.Builder(this)
                .title(title)
                .content(message)
                .positiveText(R.string.button_ok)
                .negativeText(R.string.button_cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        //clean user login db
                        InterestHelper interestHelper = new InterestHelper();
                        interestHelper.clearAll();

                        memberBizdirHelper.clearAll();
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        MainActivity.this.finish();
                    }
                })
                .show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        outState.putInt("activePosition", activePosition);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            new MaterialDialog.Builder(this)
                    .title(R.string.app_exit_title)
                    .content(R.string.app_exit_message)
                    .positiveText(R.string.button_ok)
                    .negativeText(R.string.button_cancel)
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            //super.onBackPressed();
                            finish();
                        }
                    })
                    .show();
        }
    }

    private void onNavigationDrawerItemSelected(int position) {
        activePosition = position;
        setStyleAndTitle();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        switch (position) {
            case Const.INDEX_MENU_SEARCH:
                SearchFragment searchFragment = new SearchFragment();
                fragment = searchFragment.newInstance();
                break;
            case Const.INDEX_MENU_FAVORITE:
                FavoriteFragment favoriteFragment = new FavoriteFragment();
                fragment = favoriteFragment.newInstance();
                break;
            case Const.INDEX_MENU_CATEGORY:
                CategoryMainHeaderGridViewFragment categoryMainHeaderGridViewFragment = new CategoryMainHeaderGridViewFragment();
                fragment = categoryMainHeaderGridViewFragment.newInstance();
                break;
            case Const.INDEX_MENU_ASSOCIATION:
                fragment = new AssociationFragment().newInstance();
                break;
            case Const.INDEX_MENU_OPPORTUNITUES:
                BusinessOpportunitiesMainFragment businessOpportunitiesMainFragment = new BusinessOpportunitiesMainFragment();
                fragment = businessOpportunitiesMainFragment.newInstance();
                break;
            case Const.INDEX_MENU_NEWS:
                NewsMainFragment newsMainFragment = new NewsMainFragment();
                fragment = newsMainFragment.newInstance();
                break;
            case Const.INDEX_MENU_FORUM:
                ForumMainFragment forumMainFragment = new ForumMainFragment();
                fragment = forumMainFragment.newInstance();
                break;
            case Const.INDEX_MENU_PROMOTION:
                PromotionFragment promotionFragment = new PromotionFragment();
                fragment = promotionFragment.newInstance();
                break;
            case Const.INDEX_MENU_EVENT_CALENDAR:
                EventCalendarMainFragment eventCalendarMainFragment = new EventCalendarMainFragment();
                fragment = eventCalendarMainFragment.newInstance();
                break;
            case Const.INDEX_MENU_DOWNLOAD:
                DownloadFragment downloadFragment = new DownloadFragment();
                fragment = downloadFragment.newInstance();
                break;
            case Const.INDEX_MENU_ABOUT:
                AboutBizdirFragment aboutBizdirFragment = new AboutBizdirFragment();
                fragment = aboutBizdirFragment.newInstance();
                break;
            case Const.INDEX_MENU_FAQ:
                FaqFragment faqFragment = new FaqFragment();
                fragment = faqFragment.newInstance();
                break;
            default:
                break;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, fragment)
                .commit();
    }

    private void setStyleAndTitle() {
        switch (activePosition) {
            case Const.INDEX_MENU_SEARCH:
                mTitle = getString(R.string.app_name);
                setTitle(mTitle);
                setShadowVisible(true);
                break;
            case Const.INDEX_MENU_FAVORITE:
                mTitle = getString(R.string.drawer_item_favorite);
                setTitle(mTitle);
                setShadowVisible(true);
                break;
            case Const.INDEX_MENU_ASSOCIATION:
                mTitle = getString(R.string.drawer_item_association);
                setTitle(mTitle);
                setShadowVisible(true);
                break;
            case Const.INDEX_MENU_CATEGORY:
                mTitle = getString(R.string.drawer_item_category);
                setTitle(mTitle);
                setShadowVisible(true);
                break;
            case Const.INDEX_MENU_OPPORTUNITUES:
                mTitle = getString(R.string.drawer_item_opportunities);
                setTitle(mTitle);
                setShadowVisible(false);
                break;
            case Const.INDEX_MENU_NEWS:
                mTitle = getString(R.string.drawer_item_news);
                setTitle(mTitle);
                setShadowVisible(false);
                break;
            case Const.INDEX_MENU_FORUM:
                mTitle = getString(R.string.drawer_item_forum);
                setTitle(mTitle);
                setShadowVisible(false);
                break;
            case Const.INDEX_MENU_PROMOTION:
                mTitle = getString(R.string.drawer_item_promotion);
                setTitle(mTitle);
                setShadowVisible(true);
                break;
            case Const.INDEX_MENU_EVENT_CALENDAR:
                mTitle = getString(R.string.drawer_item_event_calendar);
                setTitle(mTitle);
                setShadowVisible(false);
                break;
            case Const.INDEX_MENU_DOWNLOAD:
                mTitle = getString(R.string.drawer_item_download);
                setTitle(mTitle);
                setShadowVisible(true);
                break;
            case Const.INDEX_MENU_ABOUT:
                mTitle = getAboutBizdir();
                setTitle(mTitle);
                setShadowVisible(true);
                break;
            case Const.INDEX_MENU_FAQ:
                mTitle = getString(R.string.drawer_item_faq);
                setTitle(mTitle);
                setShadowVisible(true);
                break;
            default:
                mTitle = getString(R.string.app_name);
                setTitle(mTitle);
                setShadowVisible(true);
                break;
        }
    }

    private String getAboutBizdir() {
        return "About BizDir " + Helpers.getVersionName();
    }

    private void setShadowVisible(boolean shadowVisible) {
        if (shadowVisible) {
            getSupportActionBar().setElevation(5);
            drawShadowFrameLayout.setShadowVisible(true, false);
        } else {
            getSupportActionBar().setElevation(0);
            drawShadowFrameLayout.setShadowVisible(false, false);
        }
    }

    public void onSectionAttached(int number) {
        gotoPage(number);
    }

    private void gotoPage(int pageIndex) {
        activePosition = pageIndex;
        result.setSelection(pageIndex, false);
        onNavigationDrawerItemSelected(pageIndex);
    }
}
