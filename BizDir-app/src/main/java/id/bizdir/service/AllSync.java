package id.bizdir.service;

import android.text.TextUtils;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import id.bizdir.App;
import id.bizdir.modelhelper.AnggotaCategoryHelper;
import id.bizdir.modelhelper.AnggotaGalleryHelper;
import id.bizdir.modelhelper.AnggotaHelper;
import id.bizdir.modelhelper.AnggotaSubCategoryAssignmentHelper;
import id.bizdir.modelhelper.AnggotaSubCategoryHelper;
import id.bizdir.modelhelper.CityHelper;
import id.bizdir.modelhelper.CommonHelper;
import id.bizdir.modelhelper.DownloadRootHelper;
import id.bizdir.modelhelper.DownloadSubHelper;
import id.bizdir.modelhelper.EventCategoryHelper;
import id.bizdir.modelhelper.EventHelper;
import id.bizdir.modelhelper.ForumCategoryHelper;
import id.bizdir.modelhelper.ForumPostHelper;
import id.bizdir.modelhelper.ForumThreadHelper;
import id.bizdir.modelhelper.NewsBusinessCategoryHelper;
import id.bizdir.modelhelper.NewsBusinessHelper;
import id.bizdir.modelhelper.NewsKadinHelper;
import id.bizdir.modelhelper.OpportunityCategoryHelper;
import id.bizdir.modelhelper.OpportunityHelper;
import id.bizdir.modelhelper.PromotionHelper;
import id.bizdir.modelhelper.ProvinceHelper;
import id.bizdir.modelhelper.TableSynchHelper;
import id.bizdir.model.TableSynch;
import id.bizdir.util.Const;
import id.bizdir.util.Helpers;

/**
 * Created by Hendry on 25/04/2015.
 */
public class AllSync {

    public static Reader syncAllReader(final boolean splash) throws IOException {
        final OkHttpClient client = Helpers.defaultHttpClientLong();
        //client.networkInterceptors().add(new GzipRequestInterceptor());

        TableSynchHelper tableSynchHelper = new TableSynchHelper();
        List<TableSynch> listSyncTable = new ArrayList<>();
        TableSynch lastUpdateObj;
        TableSynch obj;

        if (!splash) {
            lastUpdateObj = tableSynchHelper.get(Const.TABLE_ANGGOTA);
            obj = new TableSynch();
            obj.setTable(Const.TABLE_ANGGOTA);
            obj.setLastSynch(lastUpdateObj.getLastSynch());
            listSyncTable.add(obj);

            lastUpdateObj = tableSynchHelper.get(Const.TABLE_ANGGOTA_GALLERY);
            obj = new TableSynch();
            obj.setTable(Const.TABLE_ANGGOTA_GALLERY);
            obj.setLastSynch(lastUpdateObj.getLastSynch());
            listSyncTable.add(obj);

            lastUpdateObj = tableSynchHelper.get(Const.TABLE_ANGGOTA_SUB_CATEGORY);
            obj = new TableSynch();
            obj.setTable(Const.TABLE_ANGGOTA_SUB_CATEGORY);
            obj.setLastSynch(lastUpdateObj.getLastSynch());
            listSyncTable.add(obj);

            lastUpdateObj = tableSynchHelper.get(Const.TABLE_ANGGOTA_SUB_CATEGORY_ASSIGNMENT);
            obj = new TableSynch();
            obj.setTable(Const.TABLE_ANGGOTA_SUB_CATEGORY_ASSIGNMENT);
            obj.setLastSynch(lastUpdateObj.getLastSynch());
            listSyncTable.add(obj);

        }

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_ANGGOTA_CATEGORY);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_ANGGOTA_CATEGORY);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_CITY);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_CITY);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_COMMON);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_COMMON);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_DOWNLOAD_ROOT);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_DOWNLOAD_ROOT);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_DOWNLOAD_SUB);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_DOWNLOAD_SUB);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_EVENT);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_EVENT);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_FORUM_POST);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_FORUM_POST);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_FORUM_THREAD);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_FORUM_THREAD);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_NEWS_BUSINESS);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_NEWS_BUSINESS);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_NEWS_BUSINESS_CATEGORY);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_NEWS_BUSINESS_CATEGORY);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_NEWS_KADIN);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_NEWS_KADIN);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_OPPORTUNITY);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_OPPORTUNITY);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_PROMOTION);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_PROMOTION);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_PROVINCE);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_PROVINCE);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_WALKTHROUGH);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_WALKTHROUGH);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_EVENT_CATEGORY);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_EVENT_CATEGORY);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_FORUM_CATEGORY);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_FORUM_CATEGORY);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_OPPORTUNITY_CATEGORY);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_OPPORTUNITY_CATEGORY);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        String jsonParameters = App.getGson().toJson(listSyncTable);
        RequestBody body = RequestBody.create(Common.JSON, jsonParameters);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_SYNC)
                .post(body)
                .build();

        /*
        Request compressedRequest = request.newBuilder()
                .header("Content-Encoding", "gzip")
                .method(request.method(), Helpers.forceContentLength(Helpers.gzip(request.body())))
                .build();
        */

        //Response response = client.newCall(request).execute();
        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().charStream();
    }

    public static String syncOpportunity() throws IOException {
        final OkHttpClient client = new OkHttpClient();

        TableSynchHelper tableSynchHelper = new TableSynchHelper();
        List<TableSynch> listSyncTable = new ArrayList<>();

        TableSynch lastUpdateObj = tableSynchHelper.get(Const.TABLE_OPPORTUNITY);
        TableSynch obj = new TableSynch();
        obj.setTable(Const.TABLE_OPPORTUNITY);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_OPPORTUNITY_CATEGORY);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_OPPORTUNITY_CATEGORY);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        String json = App.getGson().toJson(listSyncTable);

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_SYNC)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

    public static void insertOpportunitySync(String jsonString) {
        if (!TextUtils.isEmpty(jsonString)) {

            OpportunityHelper opportunityHelper = new OpportunityHelper();
            opportunityHelper.addAll(jsonString);

            OpportunityCategoryHelper opportunityCategoryHelper = new OpportunityCategoryHelper();
            opportunityCategoryHelper.addAll(jsonString);

            TableSynchHelper tableSynchHelper = new TableSynchHelper();
            tableSynchHelper.addAll(jsonString);
        }
    }

    public static String syncEvent() throws IOException {
        final OkHttpClient client = new OkHttpClient();

        TableSynchHelper tableSynchHelper = new TableSynchHelper();
        List<TableSynch> listSyncTable = new ArrayList<>();

        TableSynch lastUpdateObj = tableSynchHelper.get(Const.TABLE_EVENT);
        TableSynch obj = new TableSynch();
        obj.setTable(Const.TABLE_EVENT);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_EVENT_CATEGORY);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_EVENT_CATEGORY);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        String json = App.getGson().toJson(listSyncTable);

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_SYNC)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

    public static void insertEventSync(String jsonString) {
        if (!TextUtils.isEmpty(jsonString)) {

            EventHelper eventHelper = new EventHelper();
            eventHelper.addAll(jsonString);

            EventCategoryHelper eventCategoryHelper = new EventCategoryHelper();
            eventCategoryHelper.addAll(jsonString);

            TableSynchHelper tableSynchHelper = new TableSynchHelper();
            tableSynchHelper.addAll(jsonString);
        }
    }

    public static String syncPromotion() throws IOException {
        final OkHttpClient client = new OkHttpClient();

        TableSynchHelper tableSynchHelper = new TableSynchHelper();
        List<TableSynch> listSyncTable = new ArrayList<>();

        TableSynch lastUpdateObj = tableSynchHelper.get(Const.TABLE_PROMOTION);
        TableSynch obj = new TableSynch();
        obj.setTable(Const.TABLE_PROMOTION);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        String json = App.getGson().toJson(listSyncTable);

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_SYNC)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

    public static void insertPromotionSync(String jsonString) {
        if (!TextUtils.isEmpty(jsonString)) {

            PromotionHelper promotionHelper = new PromotionHelper();
            promotionHelper.addAll(jsonString);

            TableSynchHelper tableSynchHelper = new TableSynchHelper();
            tableSynchHelper.addAll(jsonString);
        }
    }

    public static String syncNewsKadin() throws IOException {
        final OkHttpClient client = new OkHttpClient();

        TableSynchHelper tableSynchHelper = new TableSynchHelper();
        List<TableSynch> listSyncTable = new ArrayList<>();

        TableSynch lastUpdateObj = tableSynchHelper.get(Const.TABLE_NEWS_KADIN);
        TableSynch obj = new TableSynch();
        obj.setTable(Const.TABLE_NEWS_KADIN);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        String json = App.getGson().toJson(listSyncTable);

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_SYNC)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

    public static void insertNewsKadinSync(String jsonString) {
        if (!TextUtils.isEmpty(jsonString)) {

            NewsKadinHelper newsKadinHelper = new NewsKadinHelper();
            newsKadinHelper.addAll(jsonString);

            TableSynchHelper tableSynchHelper = new TableSynchHelper();
            tableSynchHelper.addAll(jsonString);
        }
    }

    public static String syncNewsAntara() throws IOException {
        final OkHttpClient client = new OkHttpClient();

        TableSynchHelper tableSynchHelper = new TableSynchHelper();
        List<TableSynch> listSyncTable = new ArrayList<>();

        TableSynch lastUpdateObj = tableSynchHelper.get(Const.TABLE_NEWS_BUSINESS_CATEGORY);
        TableSynch obj = new TableSynch();
        obj.setTable(Const.TABLE_NEWS_BUSINESS_CATEGORY);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_NEWS_BUSINESS);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_NEWS_BUSINESS);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        String json = App.getGson().toJson(listSyncTable);

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_SYNC)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

    public static void insertNewsAntaraSync(String jsonString) {
        if (!TextUtils.isEmpty(jsonString)) {

            NewsBusinessCategoryHelper newsBusinessCategoryHelper = new NewsBusinessCategoryHelper();
            newsBusinessCategoryHelper.addAll(jsonString);

            NewsBusinessHelper newsBusinessHelper = new NewsBusinessHelper();
            newsBusinessHelper.addAll(jsonString);

            TableSynchHelper tableSynchHelper = new TableSynchHelper();
            tableSynchHelper.addAll(jsonString);
        }
    }

    public static String syncForum() throws IOException {
        final OkHttpClient client = new OkHttpClient();

        TableSynchHelper tableSynchHelper = new TableSynchHelper();
        List<TableSynch> listSyncTable = new ArrayList<>();

        TableSynch lastUpdateObj = tableSynchHelper.get(Const.TABLE_FORUM_CATEGORY);
        TableSynch obj = new TableSynch();
        obj.setTable(Const.TABLE_FORUM_CATEGORY);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_FORUM_POST);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_FORUM_POST);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_FORUM_THREAD);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_FORUM_THREAD);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        String json = App.getGson().toJson(listSyncTable);

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_SYNC)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        //return response.body().charStream();
        return response.body().string();
    }

    public static void insertForumSync(String jsonString) {
        if (!TextUtils.isEmpty(jsonString)) {

            ForumCategoryHelper forumCategoryHelper = new ForumCategoryHelper();
            forumCategoryHelper.addAll(jsonString);

            ForumPostHelper forumPostHelper = new ForumPostHelper();
            forumPostHelper.addAll(jsonString);

            ForumThreadHelper forumThreadHelper = new ForumThreadHelper();
            forumThreadHelper.addAll(jsonString);

            TableSynchHelper tableSynchHelper = new TableSynchHelper();
            tableSynchHelper.addAll(jsonString);
        }
    }

    public static String syncCity() throws IOException {
        final OkHttpClient client = new OkHttpClient();

        TableSynchHelper tableSynchHelper = new TableSynchHelper();
        List<TableSynch> listSyncTable = new ArrayList<>();

        TableSynch lastUpdateObj = tableSynchHelper.get(Const.TABLE_CITY);
        TableSynch obj = new TableSynch();
        obj.setTable(Const.TABLE_CITY);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        String json = App.getGson().toJson(listSyncTable);

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_SYNC)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

    public static void insertCitySync(String jsonString) {
        if (!TextUtils.isEmpty(jsonString)) {

            CityHelper cityHelper = new CityHelper();
            cityHelper.addAll(jsonString);

            TableSynchHelper tableSynchHelper = new TableSynchHelper();
            tableSynchHelper.addAll(jsonString);
        }
    }

    public static String syncProvince() throws IOException {
        final OkHttpClient client = new OkHttpClient();

        TableSynchHelper tableSynchHelper = new TableSynchHelper();
        List<TableSynch> listSyncTable = new ArrayList<>();

        TableSynch lastUpdateObj = tableSynchHelper.get(Const.TABLE_PROVINCE);
        TableSynch obj = new TableSynch();
        obj.setTable(Const.TABLE_PROVINCE);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        String json = App.getGson().toJson(listSyncTable);

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_SYNC)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

    public static void insertProvinceSync(String jsonString) {
        if (!TextUtils.isEmpty(jsonString)) {

            ProvinceHelper provinceHelper = new ProvinceHelper();
            provinceHelper.addAll(jsonString);

            TableSynchHelper tableSynchHelper = new TableSynchHelper();
            tableSynchHelper.addAll(jsonString);
        }
    }

    public static String syncDownload() throws IOException {
        final OkHttpClient client = new OkHttpClient();

        TableSynchHelper tableSynchHelper = new TableSynchHelper();
        List<TableSynch> listSyncTable = new ArrayList<>();

        TableSynch lastUpdateObj = tableSynchHelper.get(Const.TABLE_DOWNLOAD_ROOT);
        TableSynch obj = new TableSynch();
        obj.setTable(Const.TABLE_DOWNLOAD_ROOT);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        lastUpdateObj = tableSynchHelper.get(Const.TABLE_DOWNLOAD_SUB);
        obj = new TableSynch();
        obj.setTable(Const.TABLE_DOWNLOAD_SUB);
        obj.setLastSynch(lastUpdateObj.getLastSynch());
        listSyncTable.add(obj);

        String json = App.getGson().toJson(listSyncTable);

        RequestBody body = RequestBody.create(Common.JSON, json);
        Request.Builder builder = Common.getBasicRequestBuilder();

        Request request = builder.url(Const.API_URL_SYNC)
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
        return response.body().string();
    }

    public static void insertDownloadSync(String jsonString) {
        if (!TextUtils.isEmpty(jsonString)) {

            DownloadRootHelper downloadRootHelper = new DownloadRootHelper();
            downloadRootHelper.addAll(jsonString);

            DownloadSubHelper downloadSubHelper = new DownloadSubHelper();
            downloadSubHelper.addAll(jsonString);

            TableSynchHelper tableSynchHelper = new TableSynchHelper();
            tableSynchHelper.addAll(jsonString);
        }
    }

}
