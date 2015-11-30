package id.bizdir.service;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.Reader;

import id.bizdir.util.Const;

/**
 * Created by Hendry on 09/08/2015.
 */
public class NewstickerService {

    public Reader getNewsTicker(Integer cityId) throws IOException {

        final OkHttpClient client = new OkHttpClient();
        String json = "{ \"city_id\":" + cityId.toString() + ", \"type\":\"all\" }";

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_NEWSTICKER_NEWS)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().charStream();
    }

    public Reader getAllNewsTicker() throws IOException {

        final OkHttpClient client = new OkHttpClient();
        String json = "";

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_NEWSTICKER)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().charStream();
    }

    public Reader getPromotionTicker() throws IOException {

        final OkHttpClient client = new OkHttpClient();
        String json = "";

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_NEWSTICKER_PROMOTION)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().charStream();
    }

}
