package id.bizdir.model;

import java.util.Date;

/**
 * Created by Hendry on 28/07/2015.
 */

public class Weather {

    private int id;
    private Date date;
    private String day;
    private int provinceId;
    private String provinceName;
    private int cityId;
    private String cityName;
    private String weather;
    private int weatherMin;
    private int weatherMax;
    private String icon;
    private int localResourceIcon;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    public int getWeatherMin() {
        return weatherMin;
    }

    public void setWeatherMin(int weatherMin) {
        this.weatherMin = weatherMin;
    }

    public int getWeatherMax() {
        return weatherMax;
    }

    public void setWeatherMax(int weatherMax) {
        this.weatherMax = weatherMax;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getLocalResourceIcon() {
        return localResourceIcon;
    }

    public void setLocalResourceIcon(int localResourceIcon) {
        this.localResourceIcon = localResourceIcon;
    }
}
