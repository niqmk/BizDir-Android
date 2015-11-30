package id.bizdir.service;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import id.bizdir.util.Const;

/**
 * Created by Hendry on 09/08/2015.
 */
public class AdsService {

    public String getAds(String zoneId) throws IOException {

        final OkHttpClient client = new OkHttpClient();
        String json = "{" +
                "\"zoneid\":" + zoneId +
                "}";

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_ADS)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

}
