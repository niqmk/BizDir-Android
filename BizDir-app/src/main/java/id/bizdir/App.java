package id.bizdir;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseCrashReporting;
import com.parse.ParseInstallation;
import com.rey.material.app.ThemeManager;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import id.bizdir.model.City;
import id.bizdir.model.Province;
import id.bizdir.model.Weather;
import id.bizdir.util.Const;
import id.bizdir.util.DatabaseHelper;
import id.bizdir.util.IntTypeAdapter;
//import net.danlew.android.joda.JodaTimeAndroid;
//import net.danlew.android.joda.ResourceZoneInfoProvider;

/**
 * Created by Hendry on 20/04/2015.
 */
public class App extends Application {

    private static Context context;
    private static Gson gson;
    private static DatabaseHelper databaseHelper;
    private static boolean isDataMemberIsChanged;
    private static City city;
    private static Province province;
    private static List<Weather> weatherForecastList;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //gson = new GsonBuilder()
        //        .setDateFormat(Const.DATETIME_FORMAT).create();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            DateFormat format = new SimpleDateFormat(Const.DATETIME_FORMAT, Locale.ENGLISH);

            @Override
            public Date deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
                    throws JsonParseException {
                try {
                    return format.parse(json.getAsString());
                } catch (ParseException e) {
                    return null;
                }
            }
        })
                .registerTypeAdapter(int.class, new IntTypeAdapter())
                .registerTypeAdapter(Integer.class, new IntTypeAdapter());
        gson = gsonBuilder.setDateFormat(Const.DATETIME_FORMAT).create();

        isDataMemberIsChanged = false;
        city = null;
        province = null;
        weatherForecastList = null;
        ThemeManager.init(this, 1, 0, null);
        //databaseHelper = OpenHelperManager.getHelper(
        //        context, DatabaseHelper.class);
        //JodaTimeAndroid.init(this);
        setParse();
    }

    public static void setAnalytic(final Intent intent) {
        ParseAnalytics.trackAppOpenedInBackground(intent);
    }

    private void setParse() {
        ParseCrashReporting.enable(getContext());
        Parse.initialize(this, "lLiUhV2nXpxla3ybe1KumieMEd65wjc9gZdyNBId", "8XZYN6Kw7zWBwemQTJzd5wHmuNRCAUOjMQucztL7");
        //Parse.initialize(this, "SAnBjlNgEVzBTHmuyoIcUWHtG0yopOERMdgh6jmb", "JBFBCRAmXOtWBGvZJUjLgNW9N04ZyYsK5j1Y7Ccx");
        ParseInstallation.getCurrentInstallation().saveInBackground();
    }

    public static void setDatabaseHelper(DatabaseHelper databaseHelper) {
        App.databaseHelper = databaseHelper;
    }

    public static DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }

    public static Context getContext() {
        return context;
    }

    public static Gson getGson() {
        return gson;
    }

    public static void setDataMemberIsChanged(boolean isChanged) {
        App.isDataMemberIsChanged = isChanged;
    }

    public static boolean getDataMemberIsChanged() {
        return App.isDataMemberIsChanged;
    }

    public static void setCity(City city) {
        App.city = city;
    }

    public static City getCity() {
        return city;
    }

    public static void setProvince(Province province) {
        App.province = province;
    }

    public static Province getProvince() {
        return province;
    }

    public static void setWeatherForecast(List<Weather> weatherForecastList) {
        App.weatherForecastList = weatherForecastList;
    }

    public static List<Weather> getWeatherForecast() {
        return weatherForecastList;
    }

}