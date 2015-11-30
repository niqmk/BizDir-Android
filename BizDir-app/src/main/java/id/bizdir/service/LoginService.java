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
public class LoginService {

    public String getLogin(String emailAddress, String password) throws IOException {
        final OkHttpClient client = new OkHttpClient();
        String json = "{ \"email\": \"" + emailAddress + "\"," +
                " \"password\": \"" + password + "\" } ";

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_LOGIN)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

    public String setResetPassword(String emailAddress) throws IOException {
        final OkHttpClient client = new OkHttpClient();
        String json = "{\"email\":\"" + emailAddress + "\"}";

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_FORGOT_PASSWORD)
                .post(body).build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }
}
