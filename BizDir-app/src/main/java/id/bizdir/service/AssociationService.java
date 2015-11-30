package id.bizdir.service;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.Reader;

import id.bizdir.util.Const;

/**
 * Created by Hendry on 17/09/2015.
 */
public class AssociationService {

    public Reader getAssociationMaster(String cityId) throws IOException {
        final OkHttpClient client = new OkHttpClient();
        String json = "{\"city_id\":" + cityId + "}";
        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();
        Request request = builder.url(Const.API_URL_ASSOCIATION_CITY)
                .post(body).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().charStream();
    }

    public Reader getAssociationMemberList(String associationId) throws IOException {
        final OkHttpClient client = new OkHttpClient();
        String json = "{\"id_asosiasi\":" + associationId + "}";
        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();
        Request request = builder.url(Const.API_URL_ASSOCIATION_MEMBER)
                .post(body).build();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().charStream();
    }
}
