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
public class ForumPostService {

    public String postForum(String memberId, String topicId, String message) throws IOException {
        final OkHttpClient client = new OkHttpClient();
        String json = "{\"member\":" + memberId +
                ",\"topic\":" + topicId +
                ",\"content\":\"" + message + "\"}";

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_FORUM_TOPIC_REPLY)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

    public String postViewCount(String topicId) throws IOException {
        final OkHttpClient client = new OkHttpClient();
        String json = "{\"id\":" + topicId +",\"viewCount\":1}";

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_FORUM_TOPIC_VIEW_COUNT)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }
}
