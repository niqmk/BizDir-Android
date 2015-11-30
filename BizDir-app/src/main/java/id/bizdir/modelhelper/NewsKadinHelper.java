package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.NewsKadinList;
import id.bizdir.model.NewsKadin;

/**
 * Created by Hendry on 20/04/2015.
 */

public class NewsKadinHelper {

    private Dao<NewsKadin, String> controller = null;

    private Dao<NewsKadin, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(NewsKadin.class);
        }
        return controller;
    }

    public List<NewsKadin> getAll() {
        List<NewsKadin> result = new ArrayList<>();
        try {
            Dao<NewsKadin, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<NewsKadin, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            ordersQB.orderByRaw("createDate DESC");
            PreparedQuery<NewsKadin> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public NewsKadin get(int id) {
        NewsKadin result = new NewsKadin();
        try {
            Dao<NewsKadin, String> dao = this.getController();
            QueryBuilder<NewsKadin, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<NewsKadin> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<NewsKadin> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<NewsKadin, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (NewsKadin obj : listOfData) {
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
        NewsKadinList list = App.getGson().fromJson(jsonString, NewsKadinList.class);
        addAll(list.news_kadin);
    }

}
