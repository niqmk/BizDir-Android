package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.PromotionList;
import id.bizdir.model.Promotion;

/**
 * Created by Hendry on 20/04/2015.
 */

public class PromotionHelper {

    private Dao<Promotion, String> controller = null;

    private Dao<Promotion, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(Promotion.class);
        }
        return controller;
    }

    public List<Promotion> getAll() {
        List<Promotion> result = new ArrayList<>();
        try {
            Dao<Promotion, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<Promotion, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            ordersQB.orderByRaw("createDate DESC");
            PreparedQuery<Promotion> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Promotion> getPromotionTicker() {
        List<Promotion> result = new ArrayList<>();
        try {
            Dao<Promotion, String> dao = this.getController();
            QueryBuilder<Promotion, String> builder = dao.queryBuilder();
            builder.limit(10);
            builder.orderBy("createDate", false);
            result = dao.query(builder.prepare());  // returns list of ten items
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public long getCount() {
        long result = 0;
        try {
            Dao<Promotion, String> dao = this.getController();
            QueryBuilder<Promotion, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            result = ordersQB.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Promotion get(int id) {
        Promotion result = new Promotion();
        try {
            Dao<Promotion, String> dao = this.getController();
            QueryBuilder<Promotion, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<Promotion> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<Promotion> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<Promotion, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (Promotion obj : listOfData) {
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
        PromotionList list = App.getGson().fromJson(jsonString, PromotionList.class);
        addAll(list.promotion);
    }
}
