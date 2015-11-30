package id.bizdir.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rey.material.widget.Button;

import java.io.IOException;
import java.io.Reader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.model.AllSyncModel;
import id.bizdir.model.NewsBusiness;
import id.bizdir.model.NewsKadin;
import id.bizdir.model.Newsticker;
import id.bizdir.model.NewstickerResult;
import id.bizdir.model.Promotion;
import id.bizdir.modelhelper.NewsStockHelper;
import id.bizdir.modelhelper.NewstickerHelper;
import id.bizdir.modelhelper.OpportunityHelper;
import id.bizdir.modelhelper.PromotionHelper;
import id.bizdir.model.City;
import id.bizdir.model.Province;
import id.bizdir.model.ResultObject;
import id.bizdir.model.ResultObjectHelper;
import id.bizdir.model.Weather;
import id.bizdir.service.AllSync;
import id.bizdir.service.NewsStockService;
import id.bizdir.service.NewstickerService;
import id.bizdir.ui.activity.ChooseLocationActivity;
import id.bizdir.ui.activity.ChooseMemberActivity;
import id.bizdir.ui.activity.MainActivity;
import id.bizdir.ui.activity.NewsBusinessDetailActivity;
import id.bizdir.ui.activity.NewsKadinDetailActivity;
import id.bizdir.ui.activity.PromotionDetailActivity;
import id.bizdir.ui.activity.WeatherDetailActivity;
import id.bizdir.ui.widget.CircleButton;
import id.bizdir.ui.widget.MarqueeView;
import id.bizdir.ui.widget.ScrollingTextView;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 02/05/2015.
 */
public class SearchFragment extends Fragment {

    private ImageView iconWeather;
    private ImageView imageRegion;
    private TextView textCelsius;
    private TextView textRegion;
    private ProgressBar progressBar;
    private CircleButton btnWeatherDetail;
    private City city;
    private TextView countBusines;
    private TextView countPromo;
    private TextView title;
    private TextView title2;
    private MaterialDialog progressDialog;
    private GetAllSyncTask task;
    private List<Newsticker> newstickerList = new ArrayList<>();
    private MarqueeView marqueeNewsTicker;
    private TextView textNewsTicker;

    public SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        if (savedInstanceState != null) {
            //cityId = savedInstanceState.getInt("cityId");
        }
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.MaterialDrawerTheme_BizDir);
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);
        View view = localInflater.inflate(R.layout.fragment_search, container, false);
        //View view = inflater.inflate(R.layout.fragment_about_bizdir, container, false);
        final Button buttonSearch = (Button) view.findViewById(R.id.buttonSearch);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchPage();
            }
        });
        iconWeather = (ImageView) view.findViewById(R.id.iconWeather);
        imageRegion = (ImageView) view.findViewById(R.id.imageRegion);
        final ImageView frame = (ImageView) view.findViewById(R.id.frame);
        frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showInfomation();
            }
        });
        textCelsius = (TextView) view.findViewById(R.id.textCelsius);
        textRegion = (TextView) view.findViewById(R.id.textRegion);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        countBusines = (TextView) view.findViewById(R.id.countBusiness);
        countPromo = (TextView) view.findViewById(R.id.countPromo);
        title = (TextView) view.findViewById(R.id.title);
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoBusinessOpportunitiesPage();
            }
        });
        title2 = (TextView) view.findViewById(R.id.title2);
        title2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPromoProductPage();
            }
        });
        marqueeNewsTicker = (MarqueeView) view.findViewById(R.id.marqueeNewsTicker);
        marqueeNewsTicker.setPauseBetweenAnimations(100);
        marqueeNewsTicker.setSpeed(7);
        textNewsTicker = (TextView) view.findViewById(R.id.textNewsTicker);
        textNewsTicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (textNewsTicker.getTag() != null) {
                    int selectedIndex = (int) textNewsTicker.getTag();
                    gotoNewsTickerDetail(selectedIndex);
                }

            }
        });

        final CircleButton button = (CircleButton) view.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoBusinessOpportunitiesPage();
            }
        });
        final CircleButton button2 = (CircleButton) view.findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPromoProductPage();
            }
        });
        btnWeatherDetail = (CircleButton) view.findViewById(R.id.btnWeatherDetail);
        btnWeatherDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (city != null) {
                    gotoWeatherDetail();
                }

            }
        });
        ImageView imageButtonAds = (ImageView) view.findViewById(R.id.imageButtonAds);
        ImageView imageAds = (ImageView) view.findViewById(R.id.image);
        getData();
        Helpers.getLocalAds(getActivity(), imageAds, imageButtonAds, Const.ADS_ZONE_ID_SEARCH);
        Helpers.getRemoteAds(getActivity(), imageAds, imageButtonAds, Const.ADS_ZONE_ID_SEARCH);
        getNewsTickerRemote();
        //getNewsTickerLocal();
        /*
        progressDialog = new MaterialDialog.Builder(getActivity())
                .title("Refresh Data")
                .content("Please wait...")
                .progress(true, 0)
                .negativeText(R.string.button_cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        progressDialog.dismiss();
                        if (task != null) {
                            task.cancel(true);
                        }
                    }
                })
                .cancelable(false).build();
                */

        progressDialog = new MaterialDialog.Builder(getActivity())
                .title("Refresh Data")
                .content("Please wait...")
                .progress(false, 100, true)
                .progressNumberFormat("%1d/%2d")
                .progressPercentFormat(NumberFormat.getPercentInstance(Locale.ENGLISH))
                .negativeText(R.string.button_cancel)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        progressDialog.dismiss();
                        if (task != null) {
                            task.cancel(true);
                        }
                    }
                })
                .cancelable(false).build();
        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        return view;
    }

    private void gotoWeatherDetail() {
        Intent intent = new Intent(getActivity(), WeatherDetailActivity.class);
        //intent.putExtra(Const.TOOLBAR_TITLE, category.getTitle());
        intent.putExtra(Const.OBJECT_INDEX, city.getId());
        startActivity(intent);
    }

    private void gotoNewsTickerDetail(int selectedIndex) {
        final int NEWS_KADIN = 1;
        final int NEWS_ANTARA = 2;
        final int NEWS_PROMOTION = 3;
        Newsticker obj = newstickerList.get(selectedIndex);
        if (obj != null) {
            switch (obj.getType()) {
                case NEWS_KADIN:

                    NewsKadin newsKadin = new NewsKadin();
                    newsKadin.setId(obj.getId());
                    newsKadin.setTitle(obj.getTitle());
                    newsKadin.setDescription(obj.getDescription());
                    newsKadin.setPicture(obj.getPicture());
                    newsKadin.setCreateDate(obj.getCreateDate());
                    newsKadin.setThumb(obj.getThumb());

                    Intent intentNewsKadin = new Intent(getActivity(), NewsKadinDetailActivity.class);
                    intentNewsKadin.putExtra(Const.OBJECT_INDEX, obj.getId());
                    intentNewsKadin.putExtra(Const.OBJECT_JSON, App.getGson().toJson(newsKadin));
                    startActivity(intentNewsKadin);

                    break;
                case NEWS_ANTARA:
                    NewsBusiness newsBusiness = new NewsBusiness();
                    newsBusiness.setId(obj.getId());
                    newsBusiness.setTitle(obj.getTitle());
                    newsBusiness.setDescription(obj.getDescription());
                    newsBusiness.setPicture(obj.getPicture());
                    newsBusiness.setCreateDate(obj.getCreateDate());
                    newsBusiness.setThumb(obj.getThumb());

                    Intent intentNewsAntara = new Intent(getActivity(), NewsBusinessDetailActivity.class);
                    intentNewsAntara.putExtra(Const.OBJECT_INDEX, obj.getId());
                    intentNewsAntara.putExtra(Const.OBJECT_JSON, App.getGson().toJson(newsBusiness));
                    startActivity(intentNewsAntara);
                    break;
                case NEWS_PROMOTION:
                    Promotion promotion = new Promotion();
                    promotion.setId(obj.getId());
                    promotion.setTitle(obj.getTitle());
                    promotion.setDescription(obj.getDescription());
                    promotion.setPicture(obj.getPicture());
                    promotion.setCreateDate(obj.getCreateDate());

                    Intent intentPromotion = new Intent(getActivity(), PromotionDetailActivity.class);
                    intentPromotion.putExtra(Const.OBJECT_INDEX, obj.getId());
                    intentPromotion.putExtra(Const.OBJECT_JSON, App.getGson().toJson(promotion));
                    startActivity(intentPromotion);
                    break;
                default:
                    if (!TextUtils.isEmpty(obj.getUrl())) {
                        Helpers.gotoWebAddress(obj.getUrl(),
                                obj.getTitle(),
                                getActivity());
                    }
                    break;
            }
        }
    }

    private void getCount() {

        OpportunityHelper opportunityHelper = new OpportunityHelper();
        Long opportunityCount = opportunityHelper.getCount();
        countBusines.setText(opportunityCount.toString());

        PromotionHelper promotionHelper = new PromotionHelper();
        Long promotionCount = promotionHelper.getCount();
        countPromo.setText(promotionCount.toString());
    }

    private void gotoBusinessOpportunitiesPage() {
        ((MainActivity) getActivity()).onSectionAttached(Const.INDEX_MENU_OPPORTUNITUES);
    }

    private void gotoPromoProductPage() {
        ((MainActivity) getActivity()).onSectionAttached(Const.INDEX_MENU_PROMOTION);
    }

    private void chooseLocation() {
        Intent intent = new Intent(getActivity(), ChooseLocationActivity.class);
        //intent.putExtra(Const.OBJECT_INDEX, App.getCityId());
        startActivity(intent);
    }

    private void getData() {
        city = App.getCity();
        Province province = App.getProvince();
        String urlIconRegion;

        if (city == null && province == null) {
            city = new City();
            city.setId(0);
            city.setName(getString(R.string.location_indonesia));
            city.setProvinceId(0);
            city.setName(getString(R.string.location_indonesia));
            province = new Province();
            province.setId(0);
            province.setName(getString(R.string.location_indonesia));
            textRegion.setText(province.getName());
            showWeather(false);
            urlIconRegion = "";
            App.setCity(city);
            App.setProvince(province);
        } else {
            if (province.getId() > 0) {
                textRegion.setText(province.getName());
                urlIconRegion = province.getPicture();
                showWeather(false);
            } else {
                textRegion.setText(province.getName());
                urlIconRegion = "";
                showWeather(false);
            }

            if (city.getId() > 0) {
                textRegion.setText(city.getName());
                showWeather(true);
            } else {
                if (city.getProvinceId() > 0) {
                    textRegion.setText(city.getProvinceName());
                } else {
                    textRegion.setText(city.getName());
                }
                showWeather(false);
            }
        }
        showIcon(urlIconRegion);
        App.setDataMemberIsChanged(false);
        getCount();
    }

    private void showIcon(String iconUrl) {
        if (iconUrl.isEmpty()) {
            progressBar.setVisibility(View.GONE);
            imageRegion.setImageResource(R.drawable.indonesia);
        } else {
            progressBar.setVisibility(View.VISIBLE);
            Glide.with(getActivity())
                    .load(iconUrl)
                            //.fitCenter()
                    .crossFade()
                    .into(new GlideDrawableImageViewTarget(imageRegion) {
                        @Override
                        public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            progressBar.setVisibility(View.GONE);
                            imageRegion.setImageDrawable(getResources().getDrawable(R.drawable.indonesia));
                        }
                    });
        }
    }

    private void showWeather(boolean isVisible) {
        if (isVisible) {
            List<Weather> listWeather = App.getWeatherForecast();
            if (listWeather != null) {
                if (listWeather.size() > 0) {
                    Integer todayCelsius = listWeather.get(0).getWeatherMax();
                    Drawable icon = Helpers.getWeather(listWeather.get(0).getWeather());
                    iconWeather.setImageDrawable(icon);
                    iconWeather.setVisibility(View.VISIBLE);
                    textCelsius.setText(todayCelsius + "°C");
                    textCelsius.setVisibility(View.VISIBLE);
                    btnWeatherDetail.setVisibility(View.VISIBLE);
                }
            }
        } else {
            iconWeather.setVisibility(View.GONE);
            textCelsius.setVisibility(View.GONE);
            btnWeatherDetail.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        boolean isDataChange = App.getDataMemberIsChanged();
        if (isDataChange) {
            getData();
        }
        if (newstickerList != null) {
            if (newstickerList.size() > 0) {
                if (marqueeNewsTicker != null) {
                    marqueeNewsTicker.startMarquee();
                }
            }
        }
        super.onResume();
    }

    @Override
    public void onPause() {
        if (newstickerList != null) {
            if (newstickerList.size() > 0) {
                if (marqueeNewsTicker != null) {
                    marqueeNewsTicker.reset();
                }
            }
        }
        super.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu.size() > 0) {
            menu.clear();
        }
        inflater.inflate(R.menu.fragment_search, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_favorite_refresh:
                getSyncAllStream();
                return true;
            case R.id.action_search_location:
                chooseLocation();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getSyncAllStream() {
        boolean isavailable = Helpers.isInternetConnected(getActivity());
        if (isavailable) {
            task = new GetAllSyncTask();
            task.execute();
        } else {
            new MaterialDialog.Builder(new ContextThemeWrapper(getActivity(),
                    R.style.MaterialDrawerTheme_Light_DarkToolbar))
                    .title(R.string.no_internet_connection_title)
                    .content(R.string.no_internet_connection)
                    .positiveText(R.string.button_ok)
                    .show();
        }
    }

    private void getNewsTickerRemote() {
        boolean isavailable = Helpers.isInternetConnected(getActivity());
        if (isavailable) {
            class GetNewsTickerTask extends AsyncTask<Void, Void, Void> {

                @Override
                protected Void doInBackground(Void... params) {
                    Reader reader = null;
                    try {
                        NewstickerService newstickerService = new NewstickerService();
                        //reader = newstickerService.getNewsTicker(city.getId());
                        reader = newstickerService.getAllNewsTicker();
                        NewstickerResult result = App.getGson().fromJson(reader, NewstickerResult.class);
                        if (result != null) {
                            if (result.getResult().size() > 0) {
                                //newstickerList = new ArrayList<>(result.getResult());
                                newstickerList = result.getResult();
                                //NewstickerHelper newstickerHelper = new NewstickerHelper();
                                //newstickerHelper.clearAll();
                                //newstickerHelper.addAll(result.getResult());
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
                    getNewsTickerLocal();
                }
            }

            GetNewsTickerTask getNewsTickerTask = new GetNewsTickerTask();
            getNewsTickerTask.execute();
        }
    }

    private void getNewsTickerLocal() {
        /*
        PromotionHelper promotionHelper = new PromotionHelper();
        List<Promotion> promotionList = promotionHelper.getPromotionTicker();
        newstickerList = new ArrayList<>();

        NewstickerHelper newstickerHelper = new NewstickerHelper();
        List<Newsticker> newNewstickerList = newstickerHelper.getAll();
        if (newNewstickerList.size() > 0) {
            for (Newsticker obj : newNewstickerList) {
                newstickerList.add(obj);
            }
        }

        if (promotionList.size() > 0) {
            for (Promotion obj : promotionList) {
                Newsticker newsticker = new Newsticker();
                newsticker.setId(obj.getId());
                newsticker.setDescription(obj.getDescription());
                newsticker.setPicture(obj.getPicture());
                newsticker.setTitle(obj.getTitle());
                newstickerList.add(newsticker);
            }
        }
        */

        if (newstickerList.size() > 0) {
            final Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    marqueeNewsTicker.setDataNewsTicker(newstickerList);
                    textNewsTicker.setText(Helpers.setTitle(newstickerList.get(0).getTitle()));
                    textNewsTicker.setTag(0);
                    marqueeNewsTicker.startMarquee();
                }
            });
        }

        /*
        class NewsTickerLocal extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String newsContent = "     ";
                if (newstickerList.size() > 0) {
                    String newsSpace = "                                                                 ";
                    for (Newsticker obj : newstickerList) {
                        newsContent = newsContent + obj.getTitle().trim() + newsSpace;
                    }
                }
                return newsContent;
            }

            @Override
            protected void onPostExecute(String result) {
                if (newstickerList.size() > 0) {
                    textNewsTicker.setText(result);
                    textNewsTicker.setEnabled(true);
                } else {
                    textNewsTicker.setText("");
                    textNewsTicker.setEnabled(true);
                }
            }
        }

        NewsTickerLocal newsTickerLocal = new NewsTickerLocal();
        newsTickerLocal.execute();
                */
    }

    private class GetAllSyncTask extends AsyncTask<Void, Integer, Void> {
        private boolean cancelled = false;

        @Override
        protected void onPreExecute() {
            if (progressDialog != null) {
                progressDialog.show();
                progressDialog.setProgress(0);
            }
            cancelled = false;
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (!cancelled) {
                Reader reader = null;
                publishProgress(10);
                try {
                    reader = AllSync.syncAllReader(false);
                    publishProgress(50);
                    AllSyncModel result = App.getGson().fromJson(reader, AllSyncModel.class);
                    publishProgress(85);
                    if (result != null) {
                        Helpers.insertOrUpdateAllSyncMaster(result);
                        publishProgress(95);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                            publishProgress(100);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (!cancelled) {
                getStockNewsFromWs(0);
                getData();
                progressDialog.dismiss();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (progressDialog != null) {
                progressDialog.setProgress(values[0]);
            }
        }

        @Override
        protected void onCancelled(Void params) {
            cancelled = true;
        }
    }

    private void getStockNewsFromWs(final int indexCategoryNewsStock) {
        class NewsStockAsyncTask extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... param) {
                try {
                    NewsStockService service = new NewsStockService();
                    String response = service.getNewStock(indexCategoryNewsStock);
                    if (!TextUtils.isEmpty(response)) {
                        ResultObject resultObject = ResultObjectHelper.getResult(response);
                        if (resultObject != null) {
                            try {
                                int status = resultObject.getStatus();
                                if (status == 1) {
                                    String jsonString = resultObject.getResult();
                                    NewsStockHelper helper = new NewsStockHelper();
                                    helper.addAll(jsonString);
                                }
                            } catch (Exception ignore) {

                            }

                        }
                    }
                } catch (IOException e) {
                    //response = e.getMessage();
                    e.printStackTrace();
                }
                return null;
            }
        }
        NewsStockAsyncTask task = new NewsStockAsyncTask();
        task.execute();
    }

    private void showInfomation() {
        //Toast.makeText(getActivity(),
        //       "Sorry, this function is still under construction. Please check back later!",
        //        Toast.LENGTH_SHORT).show();
        new MaterialDialog.Builder(getActivity())
                .title(R.string.app_name)
                .content(R.string.info_kadin_search)
                .positiveText(R.string.button_email_bizdir)
                .negativeText(R.string.button_kadin_address)
                .neutralText(R.string.button_close)
                .btnStackedGravity(GravityEnum.END)
                .forceStacking(true)
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        Helpers.setSendEmail(getString(R.string.email_bizdir), "", "", getActivity());
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        Helpers.gotoWebAddress(Const.KADIN_ADDRESS_PDF,
                                getString(R.string.alamat_kadin_title), getActivity());
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void showSearchPage() {
        //Toast.makeText(getActivity(),
        //        "Sorry, this function is still under construction. Please check back later!",
        //        Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), ChooseMemberActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //outState.putInt("cityId", cityId);
        super.onSaveInstanceState(outState);
    }

}