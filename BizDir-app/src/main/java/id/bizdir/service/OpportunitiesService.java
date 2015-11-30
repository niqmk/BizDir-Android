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
public class OpportunitiesService {

    public Reader updateViewCount(Integer opportunityId) throws IOException {

        final OkHttpClient client = new OkHttpClient();
        String json = "{ \"id\":" + opportunityId.toString() + ", \"viewCount\":1 }";

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_OPPORTUNITIES_VIEW)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().charStream();
    }

    public Reader updateEmailCount(Integer opportunityId) throws IOException {

        final OkHttpClient client = new OkHttpClient();
        String json = "{ \"id\":" + opportunityId.toString() + ", \"emailCount\":1 }";

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_OPPORTUNITIES_EMAIL)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().charStream();
    }
}
