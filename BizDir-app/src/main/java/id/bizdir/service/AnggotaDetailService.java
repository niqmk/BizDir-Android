package id.bizdir.service;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import id.bizdir.util.Const;

/**
 * Created by michael on 9/22/15.
 */
public class AnggotaDetailService {
	public String getAnggotaDetail(int id) throws IOException {
		final OkHttpClient client = new OkHttpClient();
		String json = "{\"id\":" + id + "}";

		RequestBody body = RequestBody.create(Common.JSON, json);
		Request.Builder builder = Common.getBasicRequestBuilder();

		Request request = builder.url(Const.API_URL_ANGGOTA_DETAIL)
				.post(body)
				.build();

		Response response = client.newCall(request).execute();
		if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
		return response.body().string();
	}
}
