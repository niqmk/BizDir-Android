package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.NewsBusinessList;
import id.bizdir.model.NewsBusiness;

/**
 * Created by Hendry on 20/04/2015.
 */

public class NewsBusinessHelper {

    private Dao<NewsBusiness, String> controller = null;

    private Dao<NewsBusiness, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(NewsBusiness.class);
        }
        return controller;
    }

    public List<NewsBusiness> getAll() {
        List<NewsBusiness> result = new ArrayList<>();
        try {
            Dao<NewsBusiness, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<NewsBusiness, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            ordersQB.orderByRaw("createDate DESC");
            //ordersQB.orderByRaw("name ASC");
            PreparedQuery<NewsBusiness> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<NewsBusiness> getAll(int categoryId) {
        List<NewsBusiness> result = new ArrayList<>();
        try {
            Dao<NewsBusiness, String> dao = this.getController();
            QueryBuilder<NewsBusiness, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("categoryId", categoryId);
            ordersQB.orderByRaw("createDate DESC");
            PreparedQuery<NewsBusiness> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public NewsBusiness get(int id) {
        NewsBusiness result = new NewsBusiness();
        try {
            Dao<NewsBusiness, String> dao = this.getController();
            QueryBuilder<NewsBusiness, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<NewsBusiness> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<NewsBusiness> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<NewsBusiness, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (NewsBusiness obj : listOfData) {
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
        NewsBusinessList list = App.getGson().fromJson(jsonString, NewsBusinessList.class);
        addAll(list.news_business);
    }
}
