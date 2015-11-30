package id.bizdir.model;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by Hendry on 25/04/2015.
 */
public class AdsObject {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private int timeToShow;

    @DatabaseField
    private String title;

    @DatabaseField
    private String url;

    @DatabaseField
    private String picture;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTimeToShow() {
        return timeToShow;
    }

    public void setTimeToShow(int timeToShow) {
        this.timeToShow = timeToShow;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
