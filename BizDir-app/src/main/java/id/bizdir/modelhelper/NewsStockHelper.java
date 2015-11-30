package id.bizdir.modelhelper;

import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.NewsStockList;
import id.bizdir.model.NewsStock;
import id.bizdir.util.Const;

/**
 * Created by Hendry on 20/04/2015.
 */

public class NewsStockHelper {

    private Dao<NewsStock, String> controller = null;

    private Dao<NewsStock, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(NewsStock.class);
        }
        return controller;
    }

    public List<NewsStock> getAll() {
        List<NewsStock> result = new ArrayList<>();
        try {
            Dao<NewsStock, String> dao = this.getController();
            result = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public NewsStock get(int id) {
        NewsStock result = new NewsStock();
        try {
            Dao<NewsStock, String> dao = this.getController();
            QueryBuilder<NewsStock, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<NewsStock> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<NewsStock> listOfData) {
        if (listOfData.size() > 0) {
            try {
                final Dao<NewsStock, String> dao = this.getController();
                dao.callBatchTasks(new Callable<Void>() {
                    public Void call() throws SQLException {
                        for (NewsStock obj : listOfData) {
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

    public void addAll(String jsonString) {
        if (!TextUtils.isEmpty(jsonString)) {
            try {
                NewsStockList list = App.getGson().fromJson(jsonString, NewsStockList.class);
                int listCount = list.data.size();
                if (listCount > 0) {
                    clearAll();
                    addAll(list.data);
                    Date nowDate = new Date();
                    TableSynchHelper tableSynchHelper = new TableSynchHelper();
                    tableSynchHelper.update(Const.TABLE_NEWS_STOCK, nowDate);
                }
            } catch (Exception ignored) {

            }
        }

    }

    public void clearAll() {
        App.getDatabaseHelper().clearTable(NewsStock.class);
    }
}
