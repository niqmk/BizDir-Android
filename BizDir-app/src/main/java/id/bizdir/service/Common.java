package id.bizdir.service;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.Request;

import id.bizdir.util.Const;

/**
 * Created by Hendry on 25/04/2015.
 */
public class Common {

    public static final MediaType JSON
            = MediaType.parse(Const.JSON_CONTENT_TYPE);

    public static Request.Builder getBasicRequestBuilder() {
        Request.Builder builder = new Request.Builder();
        builder.addHeader("Authorization", "Basic " + Const.BASIC_AUTH_KEY)
                .addHeader("Content-Type", Const.JSON_CONTENT_TYPE);
        return builder;
    }

}
