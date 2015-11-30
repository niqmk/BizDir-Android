package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.NewsBusinessCategoryList;
import id.bizdir.model.NewsBusinessCategory;

/**
 * Created by Hendry on 20/04/2015.
 */

public class NewsBusinessCategoryHelper {

    private Dao<NewsBusinessCategory, String> controller = null;

    private Dao<NewsBusinessCategory, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(NewsBusinessCategory.class);
        }
        return controller;
    }

    public List<NewsBusinessCategory> getAll() {
        List<NewsBusinessCategory> result = new ArrayList<>();
        try {
            Dao<NewsBusinessCategory, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<NewsBusinessCategory, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            //ordersQB.orderByRaw("name ASC");
            PreparedQuery<NewsBusinessCategory> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public NewsBusinessCategory get(int id) {
        NewsBusinessCategory result = new NewsBusinessCategory();
        try {
            Dao<NewsBusinessCategory, String> dao = this.getController();
            QueryBuilder<NewsBusinessCategory, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<NewsBusinessCategory> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<NewsBusinessCategory> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<NewsBusinessCategory, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (NewsBusinessCategory obj : listOfData) {
                                dao.createOrUpdate(obj);
                            }
                            return null;
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void addAll(String jsonString) {
        NewsBusinessCategoryList list = App.getGson().fromJson(jsonString, NewsBusinessCategoryList.class);
        addAll(list.news_business_category);
    }
}
