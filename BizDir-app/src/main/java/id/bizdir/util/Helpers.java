package id.bizdir.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.internal.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.amulyakhare.textdrawable.TextDrawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialize.util.UIUtils;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.RequestBody;
//import com.mikepenz.octicons_typeface_library.Octicons;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import id.bizdir.App;
import id.bizdir.BuildConfig;
import id.bizdir.R;
import id.bizdir.list.AdsList;
import id.bizdir.model.AdsObject;
import id.bizdir.model.AllSyncModel;
import id.bizdir.model.ResultObject;
import id.bizdir.model.ResultObjectHelper;
import id.bizdir.modelhelper.AdsHelper;
import id.bizdir.modelhelper.AnggotaCategoryHelper;
import id.bizdir.modelhelper.AnggotaGalleryHelper;
import id.bizdir.modelhelper.AnggotaHelper;
import id.bizdir.modelhelper.AnggotaSubCategoryAssignmentHelper;
import id.bizdir.modelhelper.AnggotaSubCategoryHelper;
import id.bizdir.modelhelper.CityHelper;
import id.bizdir.modelhelper.CommonHelper;
import id.bizdir.modelhelper.DownloadRootHelper;
import id.bizdir.modelhelper.DownloadSubHelper;
import id.bizdir.modelhelper.EventCategoryHelper;
import id.bizdir.modelhelper.EventHelper;
import id.bizdir.modelhelper.ForumCategoryHelper;
import id.bizdir.modelhelper.ForumPostHelper;
import id.bizdir.modelhelper.ForumThreadHelper;
import id.bizdir.modelhelper.NewsBusinessCategoryHelper;
import id.bizdir.modelhelper.NewsBusinessHelper;
import id.bizdir.modelhelper.NewsKadinHelper;
import id.bizdir.modelhelper.OpportunityCategoryHelper;
import id.bizdir.modelhelper.OpportunityHelper;
import id.bizdir.modelhelper.PromotionHelper;
import id.bizdir.modelhelper.ProvinceHelper;
import id.bizdir.modelhelper.TableSynchHelper;
import id.bizdir.modelhelper.WalkthroughHelper;
import id.bizdir.service.AdsService;
import id.bizdir.ui.activity.WebViewActivity;
import okio.Buffer;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;

//import com.mikepenz.octicons_typeface_library.Octicons;

/**
 * Created by Hendry on 20/04/2015.
 */
public class Helpers {

    public static String getString(int stringId) {
        return App.getContext().getResources().getString(stringId);
    }

    public static float getDimens(int dimenId) {
        return App.getContext().getResources().getDimension(dimenId);
    }

    public static int getColor(int colorId) {
        return App.getContext().getResources().getColor(colorId);
    }

    public static String DateToString(Date date) {
        if (date == null)
            return "";
        else
            return new SimpleDateFormat(Const.DATE_FORMAT,
                    Locale.ENGLISH).format(date);
    }

    public static String DateToStringFull(Date date) {
        if (date == null)
            return "";
        else
            return new SimpleDateFormat(Const.DATETIME_FORMAT,
                    Locale.ENGLISH).format(date);
    }

    public static String DateToStringDay(Date date) {
        if (date == null)
            return "";
        else
            return new SimpleDateFormat(Const.DATETIME_DAY_PATTERN,
                    Locale.ENGLISH).format(date);
    }

    public static String DateToStringMonthYear(Date date) {
        if (date == null)
            return "";
        else
            return new SimpleDateFormat(Const.DATETIME_MONTHYEAR_PATTERN,
                    Locale.ENGLISH).format(date);
    }

    public static String DateToStringPublishDate(Date date) {
        if (date == null)
            return "";
        else
            return new SimpleDateFormat(Const.DATETIME_PUBLISH_DATE_PATTERN,
                    Locale.ENGLISH).format(date);
    }

    public static String DateToStringDate(Date date) {
        if (date == null)
            return "";
        else
            return new SimpleDateFormat(Const.DATETIME_PUBLISH_MIN_DATE_PATTERN,
                    Locale.ENGLISH).format(date);
    }

    public static Date StringToDateOnly(String simpleDateOnly) {
        DateFormat format = new SimpleDateFormat(Const.DATE_FORMAT, Locale.ENGLISH);
        Date result = null;
        try {
            result = format.parse(simpleDateOnly);
        } catch (ParseException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        return result;
    }

    public static Date StringToDate(String date) {
        DateFormat format = new SimpleDateFormat(Const.DATETIME_FORMAT, Locale.ENGLISH);
        Date result = null;
        try {
            result = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            e.printStackTrace();
        }
        return result;
    }

    public static Drawable getListViewIcon(String iconIdStr, Boolean isMaterialDark) {
        Drawable result;
        if (!iconIdStr.isEmpty()) {
            try {
                //String iconType = iconIdStr.substring(0, 3);
                result = Helpers.getListViewIcon(FontAwesome.Icon.valueOf(iconIdStr),
                        isMaterialDark);
                /*
                if (iconType.equals("oct")) {
                    //Octicons octicons = new Octicons();
                    //Octicons.Icon icon = (Octicons.Icon) octicons.getIcon(iconIdStr);
                    //result = getListViewIcon(icon, isMaterialDark);
                    result = getListViewIcon(Octicons.Icon.valueOf(iconIdStr),
                            isMaterialDark);
                } else {
                    result = Helpers.getListViewIcon(FontAwesome.Icon.valueOf(iconIdStr),
                            isMaterialDark);
                }
                */
            } catch (Exception ex) {
                result = Helpers.getListViewIcon(FontAwesome.Icon.faw_pagelines,
                        isMaterialDark);
            }
        } else {
            result = Helpers.getListViewIcon(FontAwesome.Icon.faw_pagelines,
                    isMaterialDark);
        }
        return result;
    }

    public static Drawable getFontAwesomeIcon(FontAwesome.Icon iconId, Boolean isMaterialDark, int size) {
        int color = Color.WHITE;
        if (isMaterialDark) {
            color = UIUtils.getThemeColorFromAttrOrRes(App.getContext(),
                    R.attr.material_drawer_primary_icon,
                    R.color.material_drawer_primary_icon);
        }
        //Drawable result = new IconicsDrawable(App.getContext(), iconId).color(color).sizeDp(size);
        //Drawable result = new IconicsDrawable(App.getContext(), iconId).sizeDp(size);

        Drawable result = new IconicsDrawable(App.getContext(), iconId).color(color)
                .sizeDp(size);

        return result;
    }

    public static Drawable getOcticonsIcon(String iconId, Boolean isMaterialDark, int size) {
        int color = Color.WHITE;
        if (isMaterialDark) {
            color = UIUtils.getThemeColorFromAttrOrRes(App.getContext(),
                    R.attr.material_drawer_primary_icon,
                    R.color.material_drawer_primary_icon);
        }
        Drawable result = new IconicsDrawable(App.getContext(), iconId).color(color)
                .sizeDp(size);

        return result;
    }

    public static Drawable getIconName(String name) {
        String firstChar = name.substring(0, 1);
        return TextDrawable.builder().beginConfig()
                .width(120)  // width in px
                .height(120) // height in px
                .endConfig()
                .buildRect(firstChar.toUpperCase(),
                        App.getContext().getResources().getColor(R.color.material_drawer_accent));
        //App.getContext().getResources().getColor(R.color.white_transparent));
    }

    public static Drawable getIconProfileName(String name, int color) {
        String firstChar = name.substring(0, 1);
        return TextDrawable.builder().beginConfig()
                .width(120)  // width in px
                .height(120) // height in px
                .endConfig()
                .buildRound(firstChar.toUpperCase(),
                        color);
        //App.getContext().getResources().getColor(R.color.white_transparent));
    }

    public static Drawable getFontAwesomeListViewIcon(FontAwesome.Icon iconId, Boolean isMaterialDark) {
        int iconSize = Math.round(getDimens(R.dimen.icon_listview_height));
        return getFontAwesomeIcon(iconId, isMaterialDark, iconSize);
    }

    public static Drawable getOcticonsListViewIcon(String iconId, Boolean isMaterialDark) {
        int iconSize = Math.round(getDimens(R.dimen.icon_listview_height));
        return getOcticonsIcon(iconId, isMaterialDark, iconSize);
    }

    public static Drawable getListViewIcon(FontAwesome.Icon fontAwesomeIcon, Boolean isMaterialDark) {
        int iconSize = Math.round(getDimens(R.dimen.icon_listview_height));
        int color = Color.WHITE;
        if (isMaterialDark) {
            color = UIUtils.getThemeColorFromAttrOrRes(App.getContext(),
                    R.color.material_drawer_accent,
                    R.color.material_drawer_accent);
        }
        return getIcon(fontAwesomeIcon, iconSize, color);
    }

    /*
    public static Drawable getListViewIcon(Octicons.Icon octiconsIcon, Boolean isMaterialDark) {
        int iconSize = Math.round(getDimens(R.dimen.icon_listview_height));
        int color = Color.WHITE;
        if (isMaterialDark) {
            color = UIUtils.getThemeColorFromAttrOrRes(App.getContext(),
                    R.color.material_drawer_accent,
                    R.color.material_drawer_accent);
        }
        return getIcon(octiconsIcon, iconSize, color);
    }
    */

    public static Drawable getEditTextIcon(FontAwesome.Icon fontAwesomeIcon, Boolean isMaterialDark) {
        int iconSize = Math.round(getDimens(R.dimen.icon_actionbar_height));
        int color = Color.WHITE;
        if (isMaterialDark) {
            color = UIUtils.getThemeColorFromAttrOrRes(App.getContext(),
                    R.color.material_drawer_accent,
                    R.color.material_drawer_accent);
        }
        return getIcon(fontAwesomeIcon, iconSize, color);
    }

    /*
    public static Drawable getEditTextIcon(Octicons.Icon octiconsIcon, Boolean isMaterialDark) {
        int iconSize = Math.round(getDimens(R.dimen.icon_actionbar_height));
        int color = Color.WHITE;
        if (isMaterialDark) {
            color = UIUtils.getThemeColorFromAttrOrRes(App.getContext(),
                    R.color.material_drawer_accent,
                    R.color.material_drawer_accent);
        }
        return getIcon(octiconsIcon, iconSize, color);
    }*/

    public static Drawable getBottomIcon(FontAwesome.Icon fontAwesomeIcon, Boolean isMaterialDark) {
        int iconSize = Math.round(getDimens(R.dimen.icon_actionbar_height));
        int color = Color.WHITE;
        if (isMaterialDark) {
            color = UIUtils.getThemeColorFromAttrOrRes(App.getContext(),
                    R.attr.material_drawer_primary_icon,
                    R.color.material_drawer_primary_icon);
        }
        return getIcon(fontAwesomeIcon, iconSize, color);
    }

    /*
    public static Drawable getBottomIcon(Octicons.Icon octiconsIcon, Boolean isMaterialDark) {
        int iconSize = Math.round(getDimens(R.dimen.icon_actionbar_height));
        int color = Color.WHITE;
        if (isMaterialDark) {
            color = UIUtils.getThemeColorFromAttrOrRes(App.getContext(),
                    R.attr.material_drawer_primary_icon,
                    R.color.material_drawer_primary_icon);
        }
        return getIcon(octiconsIcon, iconSize, color);
    }*/

    public static Drawable getActionIcon(FontAwesome.Icon fontAwesomeIcon, Boolean isMaterialDark) {
        int color = Color.WHITE;
        if (isMaterialDark) {
            color = UIUtils.getThemeColorFromAttrOrRes(App.getContext(),
                    R.attr.material_drawer_primary_icon,
                    R.color.material_drawer_primary_icon);
        }
        return getIcon(fontAwesomeIcon, 24, color);
    }

    /*
    public static Drawable getActionIcon(Octicons.Icon octiconsIcon, Boolean isMaterialDark) {
        int color = Color.WHITE;
        if (isMaterialDark) {
            color = UIUtils.getThemeColorFromAttrOrRes(App.getContext(),
                    R.attr.material_drawer_primary_icon,
                    R.color.material_drawer_primary_icon);
        }
        return getIcon(octiconsIcon, 24, color);
    }*/

    public static Drawable getIcon(FontAwesome.Icon fontAwesomeIcon, int iconSize, int color) {
        Drawable result = new IconicsDrawable(App.getContext(), fontAwesomeIcon).color(color)
                .sizeDp(iconSize);
        return result;
    }

    /*
    public static Drawable getIcon(Octicons.Icon octiconsIcon, int iconSize, int color) {
        Drawable result = new IconicsDrawable(App.getContext(), octiconsIcon).color(color)
                .sizeDp(iconSize);
        return result;
    }
    */

    public static Drawable getFontAwesomeActionBarIcon(FontAwesome.Icon iconId, Boolean isMaterialDark) {
        int iconSize = Math.round(getDimens(R.dimen.icon_actionbar_height));
        return getFontAwesomeIcon(iconId, isMaterialDark, iconSize);
    }

    public static void setLockOrientation(Activity activity) {
        if (App.getContext().getResources().getBoolean(R.bool.portrait_only)) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    public static String getPackageName() {
        return App.getContext().getPackageName();
    }

    public static String getDatabasePath() {
        String result = "/data/data/" + getPackageName() + "/databases/";
        //String result = App.getContext().getDatabasePath(Const.DATABASE_NAME).toString();
        return result;
    }

    public static String getDatabaseFullPath() {
        String result = getDatabasePath() + Const.DATABASE_NAME;
        return result;
    }

    public static String[] getArrayString(String value) {
        return value.split("\\s*,\\s*");
    }

    public static String getVersionName() {
        return BuildConfig.VERSION_NAME;
    }

    public static int getVersionCode() {
        return BuildConfig.VERSION_CODE;
    }

    public static void setPhoneDial(String phoneNumber, Activity activity) {
        try {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber.replace(" ", "")));
            activity.startActivity(callIntent);
        } catch (Exception e) {
            Toast.makeText(activity,
                    activity.getResources().getString(R.string.intent_error_call),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public static void setSendEmail(String mailTo, String emailSubject,
                                    String emailMesage, Activity activity) {
        String[] recipients = {mailTo};
        Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
        email.setType("message/rfc822");
        email.putExtra(Intent.EXTRA_EMAIL, recipients);
        email.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        email.putExtra(Intent.EXTRA_TEXT, emailMesage);
        try {
            activity.startActivity(Intent.createChooser(email,
                    activity.getResources().getString(R.string.intent_choose_email_client)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity,
                    activity.getResources().getString(R.string.intent_error_email_client),
                    Toast.LENGTH_LONG).show();
        }
    }

    public static void setOpenUrlOnWeb(String httpUrl, Activity activity) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(httpUrl));
        try {
            activity.startActivity(browserIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity,
                    activity.getResources().getString(R.string.intent_error_open_url),
                    Toast.LENGTH_LONG).show();
        }
    }

    public static void hideKeyboard(Activity activity) {
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public static void gotoWebAddress(String url, String title, Activity activity) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(Const.URL_ADDRESS, url);
        intent.putExtra(Const.TOOLBAR_TITLE, title);
        activity.startActivity(intent);
    }

    public static int getPixelValue(int dp) {
        final float scale = App.getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    public static void openFile(Activity activity, File url) throws IOException {
        // Create URI
        File file = url;
        Uri uri = Uri.fromFile(file);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        // Check what kind of file you are trying to open, by comparing the url with extensions.
        // When the if condition is matched, plugin sets the correct intent (mime) type,
        // so Android knew what application to use to open the file
        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
            // Word document
            intent.setDataAndType(uri, "application/msword");
        } else if (url.toString().contains(".pdf")) {
            // PDF file
            intent.setDataAndType(uri, "application/pdf");
        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
            // Powerpoint file
            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
            // Excel file
            intent.setDataAndType(uri, "application/vnd.ms-excel");
        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
            // ZIP Files
            intent.setDataAndType(uri, "application/zip");
        } else if (url.toString().contains(".rtf")) {
            // RTF file
            intent.setDataAndType(uri, "application/rtf");
        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
            // WAV audio file
            intent.setDataAndType(uri, "audio/x-wav");
        } else if (url.toString().contains(".gif")) {
            // GIF file
            intent.setDataAndType(uri, "image/gif");
        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
            // JPG file
            intent.setDataAndType(uri, "image/jpeg");
        } else if (url.toString().contains(".txt")) {
            // Text file
            intent.setDataAndType(uri, "text/plain");
        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
            // Video files
            intent.setDataAndType(uri, "video/*");
        } else {
            //if you want you can also define the intent type for any other file

            //additionally use else clause below, to manage other unknown extensions
            //in this case, Android will show all applications installed on the device
            //so you can choose which application to use
            intent.setDataAndType(uri, "*/*");
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    public static int getIconFileType(String fileExtention) {
        int result = R.drawable.ic_insert_drive_file_white_24dp;
        fileExtention = fileExtention.toLowerCase();
        if (fileExtention.contains("jpg") || fileExtention.contains("jpeg") || fileExtention.contains("png")) {
            result = R.drawable.ic_image_white_24dp;
        }
        return result;
    }

    public static Reader getReaderFormJsonAsset(String fileName) {
        AssetManager assetManager = App.getContext().getAssets();
        InputStream ims = null;
        try {
            ims = assetManager.open(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new InputStreamReader(ims);
    }

    public static float getFloatFromString(String value) {
        float result = 0;
        if (!TextUtils.isEmpty(value)) {
            result = Float.parseFloat(value);
        }
        return result;
    }

    public static String cleanString(String content) {
        content = content.trim();
        content = content.replace("&ldquo;", "“");
        content = content.replace("&quot;", "\"");
        content = content.replace("&rdquo;", "”");
        content = content.replace("&lsquo;", "‘");
        content = content.replace("&rsquo;", "’");
        content = content.replace("&amp;", "&");
        content = content.replace("&ndash;", "–");
        return content;
    }

    public static Drawable getWeather(String weatherType) {
        Drawable result;
        if (weatherType.equals(Const.WEATHER_TYPE_AM_SHOWERS) || weatherType.equals(Const.WEATHER_TYPE_SHOWERS)) {
            result = App.getContext().getResources().getDrawable(R.drawable.weather_showers);
        } else if (weatherType.equals(Const.WEATHER_TYPE_AM_THUNDERSTORMS) ||
                weatherType.equals(Const.WEATHER_TYPE_ISOLATED_THUNDERSTORMS) ||
                weatherType.equals(Const.WEATHER_TYPE_THUNDERSHOWERS) ||
                weatherType.equals(Const.WEATHER_TYPE_THUNDERSTORM) ||
                weatherType.equals(Const.WEATHER_TYPE_THUNDERSTORMS) ||
                weatherType.equals(Const.WEATHER_TYPE_THUNDER_VICINITY)) {
            result = App.getContext().getResources().getDrawable(R.drawable.weather_storm);
        } else if (weatherType.equals(Const.WEATHER_TYPE_CLOUDY)) {
            result = App.getContext().getResources().getDrawable(R.drawable.weather_cloudy);
        } else if (weatherType.equals(Const.WEATHER_TYPE_HAZE)) {
            result = App.getContext().getResources().getDrawable(R.drawable.weather_haze);
        } else if (weatherType.equals(Const.WEATHER_TYPE_MOSTLY_SUNNY) ||
                weatherType.equals(Const.WEATHER_TYPE_PARTLY_CLOUDY) ||
                weatherType.equals(Const.WEATHER_TYPE_PARTLY_CLOUDY_WINDY)) {
            result = App.getContext().getResources().getDrawable(R.drawable.weather_sunny);
        } else if (weatherType.equals(Const.WEATHER_TYPE_MOSTLY_CLOUDY)) {
            result = App.getContext().getResources().getDrawable(R.drawable.weather_mostly_cloudy);
        } else if (weatherType.equals(Const.WEATHER_TYPE_RAIN) ||
                weatherType.equals(Const.WEATHER_TYPE_LIGHT_RAIN)) {
            result = App.getContext().getResources().getDrawable(R.drawable.weather_rain);
        } else if (weatherType.equals(Const.WEATHER_TYPE_PM_SHOWERS)) {
            result = App.getContext().getResources().getDrawable(R.drawable.weather_pm_showers);
        } else if (weatherType.equals(Const.WEATHER_TYPE_PM_THUNDERSTORMS)) {
            result = App.getContext().getResources().getDrawable(R.drawable.weather_pm_thunderstorms);
        } else if (weatherType.equals(Const.WEATHER_TYPE_SCATTERED_THUNDERSTORMS)) {
            result = App.getContext().getResources().getDrawable(R.drawable.weather_scattered_thunderstorms);
        } else if (weatherType.equals(Const.WEATHER_TYPE_SUNNY)) {
            result = App.getContext().getResources().getDrawable(R.drawable.weather_clear);
        } else {
            result = App.getContext().getResources().getDrawable(R.drawable.weather_clear);
        }
        return result;
    }

    public static void setMainActionBar(ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(false);
    }

    public static void setMainActionBarNoShadow(ActionBar actionBar) {
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setElevation(0);
    }

    public static boolean isValidEmail(CharSequence target) {
        boolean result = false;
        if (target != null) {
            result = android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
        return result;
    }

    public static void showDialog(Activity activity, String title, String content) {
        new MaterialDialog.Builder(new ContextThemeWrapper(activity,
                R.style.MaterialDrawerTheme_Light_DarkToolbar))
                .title(title)
                .content(content)
                .positiveText(R.string.button_ok)
                .show();
    }

    public static Integer[] toIntArray(List<Integer> integerList) {
        Integer[] intArray = new Integer[integerList.size()];
        for (int i = 0; i < integerList.size(); i++) {
            intArray[i] = integerList.get(i);
        }
        return intArray;
    }

    public static void setClipboard(String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) App.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) App.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
        }
    }

    public static boolean isValidKtaNo(String ktaNo) {
        return Pattern.matches("\\b\\d{5}[-]\\d{8}", ktaNo);
    }

    public static void showShortToast(String message) {
        Toast.makeText(App.getContext(), message, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(String message) {
        Toast.makeText(App.getContext(), message, Toast.LENGTH_LONG).show();
    }

    private static void getAdsImageFromUrl(final Activity activity, String url, final ImageView imageView) {
        try {
            Glide.with(activity)
                    .load(url)
                    .crossFade()
                    .into(new GlideDrawableImageViewTarget(imageView) {
                        @Override
                        public void onResourceReady(GlideDrawable drawable, GlideAnimation anim) {
                            super.onResourceReady(drawable, anim);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.advertising_image));
                        }
                    });
        } catch (Exception ex) {
            Log.i("Error:", ex.getMessage());
        }

    }

    public static void getRemoteAds(final Activity activity, final ImageView imageView,
                                    final ImageView imageButtonAds, final Integer zoneId) {
        class GetAllSyncTask extends AsyncTask<String, Void, String> {

            String response = "";

            @Override
            protected String doInBackground(String... param) {
                response = "";
                try {
                    AdsService adsService = new AdsService();
                    response = adsService.getAds(zoneId.toString());
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
                        final int status = resultObject.getStatus();
                        if (status == 1) {
                            //String message = resultObject.getMessage();
                            String jsonString = resultObject.getResult();
                            AdsList adsList = App.getGson().fromJson(jsonString, AdsList.class);
                            if (adsList.data.size() > 0) {
                                final AdsObject adsObject = adsList.data.get(0);
                                AdsHelper adsHelper = new AdsHelper();
                                adsHelper.createOrUpdate(adsObject);
                                Helpers.getAdsImageFromUrl(activity, adsObject.getPicture(), imageView);
                                imageButtonAds.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Helpers.gotoWebAddress(adsObject.getUrl(),
                                                activity.getString(R.string.bizdir_ads),
                                                activity);
                                    }
                                });
                            }
                        }
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        GetAllSyncTask task = new GetAllSyncTask();
        task.execute();
    }

    public static void getLocalAds(final Activity activity, final ImageView imageView,
                                   final ImageView imageButtonAds, final Integer zoneId) {
        AdsHelper adsHelper = new AdsHelper();
        final AdsObject adsObject = adsHelper.get(zoneId);
        if (adsObject != null) {
            Helpers.getAdsImageFromUrl(activity, adsObject.getPicture(), imageView);
            imageButtonAds.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Helpers.gotoWebAddress(adsObject.getUrl(),
                            activity.getString(R.string.bizdir_ads),
                            activity);
                }
            });
        } else {
            imageView.setImageDrawable(activity.getResources().getDrawable(R.drawable.advertising_image));
        }
    }

    public static OkHttpClient defaultHttpClientLong() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(Const.TIMEOUT_SECOND_LONG, TimeUnit.SECONDS);
        client.setReadTimeout(Const.TIMEOUT_SECOND_LONG, TimeUnit.SECONDS);
        client.setWriteTimeout(Const.TIMEOUT_SECOND_LONG, TimeUnit.SECONDS);
        return client;
    }

    public static OkHttpClient defaultHttpClientMedium() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(Const.TIMEOUT_SECOND_MEDIUM, TimeUnit.SECONDS);
        client.setReadTimeout(Const.TIMEOUT_SECOND_MEDIUM, TimeUnit.SECONDS);
        client.setWriteTimeout(Const.TIMEOUT_SECOND_MEDIUM, TimeUnit.SECONDS);
        return client;
    }

    public static OkHttpClient defaultHttpClientShort() {
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(Const.TIMEOUT_SECOND_SHORT, TimeUnit.SECONDS);
        client.setReadTimeout(Const.TIMEOUT_SECOND_SHORT, TimeUnit.SECONDS);
        client.setWriteTimeout(Const.TIMEOUT_SECOND_SHORT, TimeUnit.SECONDS);
        return client;
    }

    public static void insertOrUpdateAllSyncMaster(AllSyncModel allSyncModel) {
        if (allSyncModel != null) {
            int status = allSyncModel.getStatus();
            if (status == 1) {
                if (allSyncModel.getResult().getAnggota() != null) {
                    if (allSyncModel.getResult().getAnggota().size() > 0) {
                        AnggotaHelper anggotaHelper = new AnggotaHelper();
                        anggotaHelper.addAll(allSyncModel.getResult().getAnggota());
                    }
                }

                if (allSyncModel.getResult().getAnggota_category() != null) {
                    if (allSyncModel.getResult().getAnggota_category().size() > 0) {
                        AnggotaCategoryHelper anggotaCategoryHelper = new AnggotaCategoryHelper();
                        anggotaCategoryHelper.addAll(allSyncModel.getResult().getAnggota_category());
                    }
                }

                if (allSyncModel.getResult().getAnggota_gallery() != null) {
                    if (allSyncModel.getResult().getAnggota_gallery().size() > 0) {
                        AnggotaGalleryHelper anggotaGalleryHelper = new AnggotaGalleryHelper();
                        anggotaGalleryHelper.addAll(allSyncModel.getResult().getAnggota_gallery());
                    }
                }

                if (allSyncModel.getResult().getAnggota_gallery() != null) {
                    if (allSyncModel.getResult().getAnggota_gallery().size() > 0) {
                        AnggotaSubCategoryAssignmentHelper anggotaSubCategoryAssignmentHelper =
                                new AnggotaSubCategoryAssignmentHelper();
                        anggotaSubCategoryAssignmentHelper.addAll(
                                allSyncModel.getResult().getAnggota_sub_category_assignment());
                    }
                }

                if (allSyncModel.getResult().getAnggota_sub_category() != null) {
                    if (allSyncModel.getResult().getAnggota_sub_category().size() > 0) {
                        AnggotaSubCategoryHelper anggotaSubCategoryHelper = new AnggotaSubCategoryHelper();
                        anggotaSubCategoryHelper.addAll(allSyncModel.getResult().getAnggota_sub_category());
                    }
                }

                if (allSyncModel.getResult().getCity() != null) {
                    if (allSyncModel.getResult().getCity().size() > 0) {
                        CityHelper cityHelper = new CityHelper();
                        cityHelper.addAll(allSyncModel.getResult().getCity());
                    }
                }

                if (allSyncModel.getResult().getCommon() != null) {
                    if (allSyncModel.getResult().getCommon().size() > 0) {
                        CommonHelper commonHelper = new CommonHelper();
                        commonHelper.addAll(allSyncModel.getResult().getCommon());
                    }
                }

                if (allSyncModel.getResult().getDownload_root() != null) {
                    if (allSyncModel.getResult().getDownload_root().size() > 0) {
                        DownloadRootHelper downloadRootHelper = new DownloadRootHelper();
                        downloadRootHelper.addAll(allSyncModel.getResult().getDownload_root());
                    }
                }

                if (allSyncModel.getResult().getDownload_sub() != null) {
                    if (allSyncModel.getResult().getDownload_sub().size() > 0) {
                        DownloadSubHelper downloadSubHelper = new DownloadSubHelper();
                        downloadSubHelper.addAll(allSyncModel.getResult().getDownload_sub());
                    }
                }

                if (allSyncModel.getResult().getEvent_category() != null) {
                    if (allSyncModel.getResult().getEvent_category().size() > 0) {
                        EventCategoryHelper eventCategoryHelper = new EventCategoryHelper();
                        eventCategoryHelper.addAll(allSyncModel.getResult().getEvent_category());
                    }
                }

                if (allSyncModel.getResult().getEvent() != null) {
                    if (allSyncModel.getResult().getEvent().size() > 0) {
                        EventHelper eventHelper = new EventHelper();
                        eventHelper.addAll(allSyncModel.getResult().getEvent());
                    }
                }

                if (allSyncModel.getResult().getForum_category() != null) {
                    if (allSyncModel.getResult().getForum_category().size() > 0) {
                        ForumCategoryHelper forumCategoryHelper = new ForumCategoryHelper();
                        forumCategoryHelper.addAll(allSyncModel.getResult().getForum_category());
                    }
                }

                if (allSyncModel.getResult().getForum_post() != null) {
                    if (allSyncModel.getResult().getForum_post().size() > 0) {
                        ForumPostHelper forumPostHelper = new ForumPostHelper();
                        forumPostHelper.addAll(allSyncModel.getResult().getForum_post());
                    }
                }

                if (allSyncModel.getResult().getForum_thread() != null) {
                    if (allSyncModel.getResult().getForum_thread().size() > 0) {
                        ForumThreadHelper forumThreadHelper = new ForumThreadHelper();
                        forumThreadHelper.addAll(allSyncModel.getResult().getForum_thread());
                    }
                }

                if (allSyncModel.getResult().getNews_business_category() != null) {
                    if (allSyncModel.getResult().getNews_business_category().size() > 0) {
                        NewsBusinessCategoryHelper newsBusinessCategoryHelper = new NewsBusinessCategoryHelper();
                        newsBusinessCategoryHelper.addAll(allSyncModel.getResult().getNews_business_category());
                    }
                }

                if (allSyncModel.getResult().getNews_business() != null) {
                    if (allSyncModel.getResult().getNews_business().size() > 0) {
                        NewsBusinessHelper newsBusinessHelper = new NewsBusinessHelper();
                        newsBusinessHelper.addAll(allSyncModel.getResult().getNews_business());
                    }
                }

                if (allSyncModel.getResult().getNews_kadin() != null) {
                    if (allSyncModel.getResult().getNews_kadin().size() > 0) {
                        NewsKadinHelper newsKadinHelper = new NewsKadinHelper();
                        newsKadinHelper.addAll(allSyncModel.getResult().getNews_kadin());
                    }
                }

                if (allSyncModel.getResult().getOpportunity_category() != null) {
                    if (allSyncModel.getResult().getOpportunity_category().size() > 0) {
                        OpportunityCategoryHelper opportunityCategoryHelper = new OpportunityCategoryHelper();
                        opportunityCategoryHelper.addAll(allSyncModel.getResult().getOpportunity_category());
                    }
                }

                if (allSyncModel.getResult().getOpportunity() != null) {
                    if (allSyncModel.getResult().getOpportunity().size() > 0) {
                        OpportunityHelper opportunityHelper = new OpportunityHelper();
                        opportunityHelper.addAll(allSyncModel.getResult().getOpportunity());
                    }
                }

                if (allSyncModel.getResult().getPromotion() != null) {
                    if (allSyncModel.getResult().getPromotion().size() > 0) {
                        PromotionHelper promotionHelper = new PromotionHelper();
                        promotionHelper.addAll(allSyncModel.getResult().getPromotion());
                    }
                }

                if (allSyncModel.getResult().getProvince() != null) {
                    if (allSyncModel.getResult().getProvince().size() > 0) {
                        ProvinceHelper provinceHelper = new ProvinceHelper();
                        provinceHelper.addAll(allSyncModel.getResult().getProvince());
                    }
                }

                if (allSyncModel.getResult().getWalkthrough() != null) {
                    if (allSyncModel.getResult().getWalkthrough().size() > 0) {
                        WalkthroughHelper walkthroughHelper = new WalkthroughHelper();
                        walkthroughHelper.addAll(allSyncModel.getResult().getWalkthrough());
                    }
                }

                if (allSyncModel.getResult().getTableSynch() != null) {
                    if (allSyncModel.getResult().getTableSynch().size() > 0) {
                        TableSynchHelper tableSynchHelper = new TableSynchHelper();
                        tableSynchHelper.addAll(allSyncModel.getResult().getTableSynch());
                    }
                }
            }
        }
    }

    public static RequestBody forceContentLength(final RequestBody requestBody) throws IOException {
        final Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return requestBody.contentType();
            }

            @Override
            public long contentLength() {
                return buffer.size();
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                sink.write(buffer.snapshot());
            }
        };
    }

    public static RequestBody gzip(final RequestBody body) {
        return new RequestBody() {
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public long contentLength() {
                return -1; // We don't know the compressed length in advance!
            }

            @Override
            public void writeTo(BufferedSink sink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };
    }

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean result;
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public static String setTitle(String title) {
        String result = "                                                                                                   ";
        if (title.length() < 100) {
            result = result + title + result;
        } else {
            result = result + title;
        }
        return result;
    }
}
