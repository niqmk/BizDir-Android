package id.bizdir.util;

import java.io.IOException;
import java.io.Reader;

import id.bizdir.App;
import id.bizdir.list.AnggotaCategoryList;
import id.bizdir.list.AnggotaGalleryList;
import id.bizdir.list.AnggotaList;
import id.bizdir.list.AnggotaSubCategoryAssignmentList;
import id.bizdir.list.AnggotaSubCategoryList;
import id.bizdir.list.CityList;
import id.bizdir.list.CommonList;
import id.bizdir.list.DownloadRootList;
import id.bizdir.list.DownloadSubList;
import id.bizdir.list.EventCategoryList;
import id.bizdir.list.EventList;
import id.bizdir.list.ForumCategoryList;
import id.bizdir.list.ForumPostList;
import id.bizdir.list.ForumThreadList;
import id.bizdir.list.NewsBusinessCategoryList;
import id.bizdir.list.NewsBusinessList;
import id.bizdir.list.NewsKadinList;
import id.bizdir.list.NewsStockList;
import id.bizdir.list.OpportunityCategoryList;
import id.bizdir.list.OpportunityList;
import id.bizdir.list.PromotionList;
import id.bizdir.list.ProvinceList;
import id.bizdir.list.TableSynchList;
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
import id.bizdir.modelhelper.NewsStockHelper;
import id.bizdir.modelhelper.OpportunityCategoryHelper;
import id.bizdir.modelhelper.OpportunityHelper;
import id.bizdir.modelhelper.PromotionHelper;
import id.bizdir.modelhelper.ProvinceHelper;
import id.bizdir.modelhelper.TableSynchHelper;

/**
 * Created by Hendry on 29/07/2015.
 */
public class DatabaseInitializers {

    public void AddAllData() {
        insertDataAnggota();
        insertDataAnggotaCategory();
        insertDataAnggotaGallery();
        insertDataAnggotaSubCategory();
        insertDataAnggotaSubCategoryAssigment();
        insertCity();
        insertCommon();
        insertDownloadRoot();
        insertDownloadSub();
        insertEvent();
        insertEventCategory();
        insertForumCategory();
        insertForumPost();
        insertForumThread();
        insertNewsBusiness();
        insertNewsBusinessCategory();
        insertNewsKadin();
        insertNewsStock();
        insertOpportunity();
        insertOpportunityCategory();
        insertPromotion();
        insertProvince();
        insertTableSync();
        //insertWeather();
    }

    private void insertDataAnggota() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_ANGGOTA);
            AnggotaList list = App.getGson().fromJson(reader, AnggotaList.class);
            AnggotaHelper helper = new AnggotaHelper();
            helper.addAll(list.anggota);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertDataAnggotaCategory() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_ANGGOTA_CATEGORY);
            AnggotaCategoryList list = App.getGson().fromJson(reader, AnggotaCategoryList.class);
            AnggotaCategoryHelper helper = new AnggotaCategoryHelper();
            helper.addAll(list.anggota_category);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertDataAnggotaGallery() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_ANGGOTA_GALLERY);
            AnggotaGalleryList list = App.getGson().fromJson(reader, AnggotaGalleryList.class);
            AnggotaGalleryHelper helper = new AnggotaGalleryHelper();
            helper.addAll(list.anggota_gallery);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertDataAnggotaSubCategory() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_ANGGOTA_SUB_CATEGORY);
            AnggotaSubCategoryList list = App.getGson().fromJson(reader, AnggotaSubCategoryList.class);
            AnggotaSubCategoryHelper helper = new AnggotaSubCategoryHelper();
            helper.addAll(list.anggota_sub_category);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertDataAnggotaSubCategoryAssigment() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_ANGGOTA_SUB_CATEGORY_ASSIGNMENT);
            AnggotaSubCategoryAssignmentList list = App.getGson().fromJson(reader,
                    AnggotaSubCategoryAssignmentList.class);
            AnggotaSubCategoryAssignmentHelper helper = new AnggotaSubCategoryAssignmentHelper();
            helper.addAll(list.anggota_sub_category_assignment);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertCity() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_CITY);
            CityList list = App.getGson().fromJson(reader, CityList.class);
            CityHelper helper = new CityHelper();
            helper.addAll(list.city);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertCommon() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_COMMON);
            CommonList list = App.getGson().fromJson(reader, CommonList.class);
            CommonHelper helper = new CommonHelper();
            helper.addAll(list.common);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertDownloadRoot() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_DOWNLOAD_ROOT);
            DownloadRootList list = App.getGson().fromJson(reader, DownloadRootList.class);
            DownloadRootHelper helper = new DownloadRootHelper();
            helper.addAll(list.download_root);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertDownloadSub() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_DOWNLOAD_SUB);
            DownloadSubList list = App.getGson().fromJson(reader, DownloadSubList.class);
            DownloadSubHelper helper = new DownloadSubHelper();
            helper.addAll(list.download_sub);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertEvent() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_EVENT);
            EventList list = App.getGson().fromJson(reader, EventList.class);
            EventHelper helper = new EventHelper();
            helper.addAll(list.event);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertEventCategory() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_EVENT_CATEGORY);
            EventCategoryList list = App.getGson().fromJson(reader, EventCategoryList.class);
            EventCategoryHelper helper = new EventCategoryHelper();
            helper.addAll(list.event_category);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertForumCategory() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_FORUM_CATEGORY);
            ForumCategoryList list = App.getGson().fromJson(reader, ForumCategoryList.class);
            ForumCategoryHelper helper = new ForumCategoryHelper();
            helper.addAll(list.forum_category);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertForumPost() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_FORUM_POST);
            ForumPostList list = App.getGson().fromJson(reader, ForumPostList.class);
            ForumPostHelper helper = new ForumPostHelper();
            helper.addAll(list.forum_post);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertForumThread() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_FORUM_THREAD);
            ForumThreadList list = App.getGson().fromJson(reader, ForumThreadList.class);
            ForumThreadHelper helper = new ForumThreadHelper();
            helper.addAll(list.forum_thread);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertNewsBusiness() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_NEWS_BUSINESS);
            NewsBusinessList list = App.getGson().fromJson(reader, NewsBusinessList.class);
            NewsBusinessHelper helper = new NewsBusinessHelper();
            helper.addAll(list.news_business);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertNewsBusinessCategory() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_NEWS_BUSINESS_CATEGORY);
            NewsBusinessCategoryList list = App.getGson().fromJson(reader,
                    NewsBusinessCategoryList.class);
            NewsBusinessCategoryHelper helper = new NewsBusinessCategoryHelper();
            helper.addAll(list.news_business_category);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertNewsKadin() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_NEWS_KADIN);
            NewsKadinList list = App.getGson().fromJson(reader, NewsKadinList.class);
            NewsKadinHelper helper = new NewsKadinHelper();
            helper.addAll(list.news_kadin);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertNewsStock() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_NEWS_STOCK);
            NewsStockList list = App.getGson().fromJson(reader, NewsStockList.class);
            NewsStockHelper helper = new NewsStockHelper();
            helper.addAll(list.data);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertOpportunity() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_OPPORTUNITY);
            OpportunityList list = App.getGson().fromJson(reader, OpportunityList.class);
            OpportunityHelper helper = new OpportunityHelper();
            helper.addAll(list.opportunity);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertOpportunityCategory() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_OPPORTUNITY_CATEGORY);
            OpportunityCategoryList list = App.getGson().fromJson(reader,
                    OpportunityCategoryList.class);
            OpportunityCategoryHelper helper = new OpportunityCategoryHelper();
            helper.addAll(list.opportunity_category);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertPromotion() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_PROMOTION);
            PromotionList list = App.getGson().fromJson(reader, PromotionList.class);
            PromotionHelper helper = new PromotionHelper();
            helper.addAll(list.promotion);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertProvince() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_PROVINCE);
            ProvinceList list = App.getGson().fromJson(reader, ProvinceList.class);
            ProvinceHelper helper = new ProvinceHelper();
            helper.addAll(list.province);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void insertTableSync() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_TABLE_SYNCH);
            TableSynchList list = App.getGson().fromJson(reader, TableSynchList.class);
            TableSynchHelper helper = new TableSynchHelper();
            helper.addAll(list.tableSynch);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    private void insertWeather() {
        try {
            Reader reader = Helpers.getReaderFormJsonAsset(Const.JSON_FILE_WEATHER);
            WeatherList list = App.getGson().fromJson(reader, WeatherList.class);
            WeatherHelper helper = new WeatherHelper();
            helper.addAll(list.forecast);
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
*/
}
