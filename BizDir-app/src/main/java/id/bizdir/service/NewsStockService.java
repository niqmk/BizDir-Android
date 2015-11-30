package id.bizdir.service;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

import id.bizdir.util.Const;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 09/08/2015.
 */
public class NewsStockService {

    public String getNewStock(int indexCategoryNewsStock) throws IOException {
        final OkHttpClient client = Helpers.defaultHttpClientMedium();

        String wsStockParam;

        switch (indexCategoryNewsStock) {
            case 1:
                wsStockParam = Const.NEWS_STOCK_TYPE_GAINER;
                break;
            case 2:
                wsStockParam = Const.NEWS_STOCK_TYPE_LOOSER;
                break;
            case 3:
                wsStockParam = Const.NEWS_STOCK_TYPE_VALUE;
                break;
            case 4:
                wsStockParam = Const.NEWS_STOCK_TYPE_VOLUME;
                break;
            case 5:
                wsStockParam = Const.NEWS_STOCK_TYPE_ACTIVE;
                break;
            default:
                wsStockParam = Const.NEWS_STOCK_TYPE_SUMMARY;
                break;
        }

        String json = "{\"category\":\"" + wsStockParam + "\"}";

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_NEWS_STOCK)
                .post(body).build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }
}
