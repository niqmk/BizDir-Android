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
public class EventService {

    public String checkRspv(String eventId, String userId) throws IOException {

        // Check status rspv.
        // 0 = Status Jika Terjadi kesalahan atau tidak terdapat rspv
        // 1 = Status Rspv available (munculkan tombol rspv attend/not attend)
        // 2 = Status user menghadiri rspv (munculkan tombol download jika fileUrl tidak kosong)
        // 3 = Status User tidak menghadiri Rspv

        final OkHttpClient client = new OkHttpClient();
        String json = "{" +
                "\"eventid\":" + eventId + ", " +
                "\"userid\":" + userId +
                "}";

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_EVENT_CHECK_RSVP)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

    public String createRspv(String eventId, String userId, String attendingAction) throws IOException {

        //attendingAction: 2 (attending rspv), 3 (not attending rspv)

        final OkHttpClient client = new OkHttpClient();
        String json = "{" +
                "\"eventid\":" + eventId + ", " +
                "\"userid\":" + userId + ", " +
                "\"attending\":" + attendingAction +
                "}";

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_EVENT_CREATE_RSVP)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }
}
