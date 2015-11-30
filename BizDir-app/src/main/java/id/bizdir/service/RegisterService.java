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
public class RegisterService {

    public String registerMember(String fullName, String email, String password, String noKTA) throws IOException {
        final OkHttpClient client = new OkHttpClient();
        String json = "{\"email\":\"" + email +
                "\",\"password\":\"" + password +
                "\",\"name\":\"" + fullName +
                "\",\"nokta\":\"" + noKTA + "\"}";

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_REGISTER)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

}
