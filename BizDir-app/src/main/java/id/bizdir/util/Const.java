package id.bizdir.util;

/**
 * Created by Hendry on 25/04/2015.
 */
public class Const {
    //public static final String API_URL = "http://bizdir.allega.co.id/v3/";
    public static final String API_URL = "http://bizdir.id/v3/";
    public static final String BASIC_AUTH_KEY = "Yml6ZGlyOjEyMzQ1";
    public static final String JSON_CONTENT_TYPE = "application/json; charset=utf-8";

    public static final String API_URL_SYNC = API_URL + "api/synch";
    public static final String API_URL_MEMBER = API_URL + "member/";
    public static final String API_URL_NEWS_STOCK = API_URL + "news/stock";
    public static final String API_URL_REGISTER = API_URL_MEMBER + "register";
    public static final String API_URL_LOGIN = API_URL_MEMBER + "login";
    public static final String API_URL_FORGOT_PASSWORD = API_URL_MEMBER + "forgot";
    public static final String API_URL_UPDATE_PROFILE = API_URL_MEMBER + "update";
    public static final String API_URL_FORUM = API_URL + "forum/";
    public static final String API_URL_FORUM_TOPIC_REPLY = API_URL_FORUM + "reply";
    public static final String API_URL_FORUM_TOPIC_VIEW_COUNT = API_URL_FORUM + "view";
    public static final String API_URL_EVENT = API_URL + "event/";
    public static final String API_URL_EVENT_CHECK_RSVP = API_URL_EVENT + "rsvpcheck";
    public static final String API_URL_EVENT_CREATE_RSVP = API_URL_EVENT + "rsvp";
    public static final String API_URL_ADS = API_URL + "ads";
    public static final String API_URL_WEATHER = API_URL + "weather/city";
    public static final String API_URL_ASSOCIATION = API_URL + "asosiasi/";
    public static final String API_URL_ASSOCIATION_CITY = API_URL_ASSOCIATION + "city/";
    public static final String API_URL_ASSOCIATION_MEMBER = API_URL_ASSOCIATION + "anggota/";
    public static final String API_URL_OPPORTUNITIES = API_URL + "opportunity/";
    public static final String API_URL_OPPORTUNITIES_VIEW = API_URL_OPPORTUNITIES + "view/";
    public static final String API_URL_OPPORTUNITIES_EMAIL = API_URL_OPPORTUNITIES + "mail/";
    public static final String API_URL_NEWSTICKER = API_URL + "newsticker/";
    public static final String API_URL_NEWSTICKER_NEWS = API_URL_NEWSTICKER + "news/";
    public static final String API_URL_NEWSTICKER_PROMOTION = API_URL_NEWSTICKER + "promotions/";
    public static final String API_URL_ANGGOTA_DETAIL = API_URL + "anggota/detail";

    public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    public static final String TOOLBAR_TITLE = "TOOLBAR_TITLE";
    public static final String OBJECT_INDEX = "OBJECT_INDEX";
    public static final String URL_ADDRESS = "URL_ADDRESS";
    public static final String OBJECT_JSON = "OBJECT_JSON";
    //public static final String PDF_NAME_ASSET = "PDF_NAME_ASSET";

    public static final String DATABASE_NAME = "bizdirV1.sqlite";
    public static final int DATABASE_VERSION = 1;

    //public static final String DETAIL_ARROW_ICON = "faw_angle_right";
    //public static final String WEBISTE_ICON = "faw_globe";
    //public static final String EMAIL_ICON = "faw_envelope_o";
    //public static final String PHONE_ICON = "faw_phone";
    //public static final String MAP_ICON = "faw_map_marker";
    public static final String DATETIME_DAY_PATTERN = "dd";
    public static final String DATETIME_MONTHYEAR_PATTERN = "MMM yyyy";
    public static final String DATETIME_PUBLISH_DATE_PATTERN = "d MMM yyyy HH:mm";
    public static final String DATETIME_PUBLISH_MIN_DATE_PATTERN = "d MMM yyyy";
    // public static final String MIME_TYPE = "text/html; charset=utf-8";
    //public static final String ENCODING_TYPE = "UTF-8";
    //public static final String HTML_CONTENT_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" +
    //        "<html><head>" +
    //        "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />" +
    //        "<head><body>";
    //public static final String HTML_CONTENT_FOOTER = "</body></html>";
    public static final String KADIN_ADDRESS_PDF = "https://drive.google.com/viewerng/viewer?embedded=true&url=http://bizdir.id/data/kadin_address.pdf";
    //public static final String ADS_SEARCH = "http://www.bizdir.id/ads/click/id/1";
    //public static final String ADS_GROUP = "http://www.bizdir.id/ads/click/id/2";
    //public static final String ADS_SUB_GROUP = "http://www.bizdir.id/ads/click/id/3";
    //public static final String ADS_LIST_MEMBER = "http://www.bizdir.id/ads/click/id/4";
    //public static final String ADS_NEWS_KADIN = "http://www.bizdir.id/ads/click/id/5";
    //public static final String ADS_NEWS_DETAIL = "http://www.bizdir.id/ads/click/id/6";
    //public static final String ADS_FORUM = "http://www.bizdir.id/ads/click/id/7";
    //public static final String ADS_WEATHER = "http://www.bizdir.id/ads/click/id/8";

    public static final int ADS_ZONE_ID_SEARCH = 1;
    public static final int ADS_ZONE_ID_GROUP = 2;
    public static final int ADS_ZONE_ID_SUB_GROUP = 3;
    public static final int ADS_ZONE_ID_LIST_MEMBER = 4;
    public static final int ADS_ZONE_ID_NEWS_KADIN = 5;
    public static final int ADS_ZONE_ID_NEWS_DETAIL = 6;
    public static final int ADS_ZONE_ID_FORUM = 7;
    public static final int ADS_ZONE_ID_WEATHER = 8;
    public static final int ADS_ZONE_ID_NEWS_ANTARA = 9;
    public static final int ADS_ZONE_ID_NEWS_STOCK = 10;

    //public static final int USER_TYPE_ADMIN = 2;
    //public static final int USER_TYPE_GUEST = 0;

    public static final int MAX_CHAR_POST_MESSAGE = 500;
    public static final int PAGE_HOME_SEARCH = 1;
    public static final int MAX_CHAR_EMAIL_ADDRESS = 128;

    public static final String TABLE_ANGGOTA = "anggota";
    public static final String TABLE_ANGGOTA_CATEGORY = "anggota_category";
    public static final String TABLE_ANGGOTA_GALLERY = "anggota_gallery";
    public static final String TABLE_ANGGOTA_SUB_CATEGORY = "anggota_sub_category";
    public static final String TABLE_ANGGOTA_SUB_CATEGORY_ASSIGNMENT = "anggota_sub_category_assignment";
    public static final String TABLE_CITY = "city";
    public static final String TABLE_COMMON = "common";
    public static final String TABLE_DOWNLOAD_ROOT = "download_root";
    public static final String TABLE_DOWNLOAD_SUB = "download_sub";
    public static final String TABLE_EVENT = "event";
    public static final String TABLE_EVENT_CATEGORY = "event_category";
    public static final String TABLE_FORUM_CATEGORY = "forum_category";
    public static final String TABLE_FORUM_POST = "forum_post";
    public static final String TABLE_FORUM_THREAD = "forum_thread";
    public static final String TABLE_NEWS_BUSINESS = "news_business";
    public static final String TABLE_NEWS_BUSINESS_CATEGORY = "news_business_category";
    public static final String TABLE_NEWS_KADIN = "news_kadin";
    public static final String TABLE_NEWS_STOCK = "news_stock";
    public static final String TABLE_OPPORTUNITY = "opportunity";
    public static final String TABLE_OPPORTUNITY_CATEGORY = "opportunity_category";
    public static final String TABLE_PROMOTION = "promotion";
    public static final String TABLE_PROVINCE = "province";
    public static final String TABLE_SYNCH = "tableSynch";
    public static final String TABLE_WEATHER = "weather";
    public static final String TABLE_WALKTHROUGH = "walkthrough";

    private static final String JSON_EXT = ".json";

    public static final String JSON_FILE_ANGGOTA = TABLE_ANGGOTA + JSON_EXT;
    public static final String JSON_FILE_ANGGOTA_CATEGORY = TABLE_ANGGOTA_CATEGORY + JSON_EXT;
    public static final String JSON_FILE_ANGGOTA_GALLERY = TABLE_ANGGOTA_GALLERY + JSON_EXT;
    public static final String JSON_FILE_ANGGOTA_SUB_CATEGORY = TABLE_ANGGOTA_SUB_CATEGORY + JSON_EXT;
    public static final String JSON_FILE_ANGGOTA_SUB_CATEGORY_ASSIGNMENT = TABLE_ANGGOTA_SUB_CATEGORY_ASSIGNMENT + JSON_EXT;
    public static final String JSON_FILE_CITY = TABLE_CITY + JSON_EXT;
    public static final String JSON_FILE_COMMON = TABLE_COMMON + JSON_EXT;
    public static final String JSON_FILE_DOWNLOAD_ROOT = TABLE_DOWNLOAD_ROOT + JSON_EXT;
    public static final String JSON_FILE_DOWNLOAD_SUB = TABLE_DOWNLOAD_SUB + JSON_EXT;
    public static final String JSON_FILE_EVENT = TABLE_EVENT + JSON_EXT;
    public static final String JSON_FILE_EVENT_CATEGORY = TABLE_EVENT_CATEGORY + JSON_EXT;
    public static final String JSON_FILE_FORUM_CATEGORY = TABLE_FORUM_CATEGORY + JSON_EXT;
    public static final String JSON_FILE_FORUM_POST = TABLE_FORUM_POST + JSON_EXT;
    public static final String JSON_FILE_FORUM_THREAD = TABLE_FORUM_THREAD + JSON_EXT;
    public static final String JSON_FILE_NEWS_BUSINESS = TABLE_NEWS_BUSINESS + JSON_EXT;
    public static final String JSON_FILE_NEWS_BUSINESS_CATEGORY = TABLE_NEWS_BUSINESS_CATEGORY + JSON_EXT;
    public static final String JSON_FILE_NEWS_KADIN = TABLE_NEWS_KADIN + JSON_EXT;
    public static final String JSON_FILE_NEWS_STOCK = TABLE_NEWS_STOCK + JSON_EXT;
    public static final String JSON_FILE_OPPORTUNITY = TABLE_OPPORTUNITY + JSON_EXT;
    public static final String JSON_FILE_OPPORTUNITY_CATEGORY = TABLE_OPPORTUNITY_CATEGORY + JSON_EXT;
    public static final String JSON_FILE_PROMOTION = TABLE_PROMOTION + JSON_EXT;
    public static final String JSON_FILE_PROVINCE = TABLE_PROVINCE + JSON_EXT;
    public static final String JSON_FILE_TABLE_SYNCH = TABLE_SYNCH + JSON_EXT;
    public static final String JSON_FILE_WEATHER = TABLE_WEATHER + JSON_EXT;

    //public static final String GALLERY_TYPE_IMAGE = "image";
    public static final String GALLERY_TYPE_VIDEO = "video";

    public static final String WEATHER_TYPE_AM_SHOWERS = "AM Showers"; //weather_showers
    public static final String WEATHER_TYPE_AM_THUNDERSTORMS = "AM Thunderstorms"; //weather_storm
    public static final String WEATHER_TYPE_CLOUDY = "Cloudy"; //weather_cloudy
    public static final String WEATHER_TYPE_HAZE = "Haze"; //weather_haze
    public static final String WEATHER_TYPE_ISOLATED_THUNDERSTORMS = "Isolated Thunderstorms"; //weather_storm
    public static final String WEATHER_TYPE_MOSTLY_CLOUDY = "Mostly Cloudy"; //weather_mostly_cloudy
    public static final String WEATHER_TYPE_MOSTLY_SUNNY = "Mostly Sunny"; //weather_sunny
    public static final String WEATHER_TYPE_PM_SHOWERS = "PM Showers"; //weather_pm_showers
    public static final String WEATHER_TYPE_PM_THUNDERSTORMS = "PM Thunderstorms"; //weather_pm_thunderstorms
    public static final String WEATHER_TYPE_PARTLY_CLOUDY = "Partly Cloudy"; //weather_sunny
    public static final String WEATHER_TYPE_PARTLY_CLOUDY_WINDY = "Partly Cloudy/Windy"; //weather_sunny
    public static final String WEATHER_TYPE_SCATTERED_THUNDERSTORMS = "Scattered Thunderstorms"; //weather_scattered_thunderstorms
    public static final String WEATHER_TYPE_SHOWERS = "Showers"; //weather_showers
    public static final String WEATHER_TYPE_SUNNY = "Sunny"; //weather_clear
    public static final String WEATHER_TYPE_THUNDERSHOWERS = "Thundershowers"; //weather_storm
    public static final String WEATHER_TYPE_THUNDERSTORM = "Thunderstorm"; //weather_storm
    public static final String WEATHER_TYPE_THUNDERSTORMS = "Thunderstorms"; //weather_storm
    public static final String WEATHER_TYPE_THUNDER_VICINITY = "Thunder in the Vicinity"; //weather_storm
    public static final String WEATHER_TYPE_RAIN = "Light Rain";
    public static final String WEATHER_TYPE_LIGHT_RAIN = "Rain";

    public static final String NEWS_STOCK_TYPE_SUMMARY = "summary";
    public static final String NEWS_STOCK_TYPE_GAINER = "gainer"; //weather_showers
    public static final String NEWS_STOCK_TYPE_LOOSER = "looser"; //weather_clear
    public static final String NEWS_STOCK_TYPE_VALUE = "value"; //weather_storm
    public static final String NEWS_STOCK_TYPE_VOLUME = "volume"; //weather_storm
    public static final String NEWS_STOCK_TYPE_ACTIVE = "active"; //weather_storm

    public static final int RSPV_ATTENDING = 2;
    public static final int RSPV_NOT_ATTENDING = 3;

    public static final int TIMEOUT_SECOND_LONG = 240;
    public static final int TIMEOUT_SECOND_MEDIUM = 30;
    public static final int TIMEOUT_SECOND_SHORT = 15;

    public static final int INDEX_MENU_SEARCH = 1;
    public static final int INDEX_MENU_FAVORITE = 2;
    public static final int INDEX_MENU_CATEGORY = 3;
    public static final int INDEX_MENU_ASSOCIATION = 4;
    public static final int INDEX_MENU_OPPORTUNITUES = 5;
    public static final int INDEX_MENU_NEWS = 6;
    public static final int INDEX_MENU_FORUM = 7;
    public static final int INDEX_MENU_PROMOTION = 8;
    public static final int INDEX_MENU_EVENT_CALENDAR = 9;
    public static final int INDEX_MENU_DOWNLOAD = 10;
    public static final int INDEX_MENU_ABOUT = 11;
    public static final int INDEX_MENU_FAQ = 12;
}
