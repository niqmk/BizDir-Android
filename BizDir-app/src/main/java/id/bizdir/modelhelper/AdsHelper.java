package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.model.AdsObject;

/**
 * Created by Hendry on 15/09/2015.
 */

public class AdsHelper {

    private Dao<AdsObject, String> controller = null;

    private Dao<AdsObject, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(AdsObject.class);
        }
        return controller;
    }

    public List<AdsObject> getAll() {
        List<AdsObject> result = new ArrayList<>();
        try {
            Dao<AdsObject, String> dao = this.getController();
            QueryBuilder<AdsObject, String> ordersQB = dao.queryBuilder();
            ordersQB.orderByRaw("id ASC");
            PreparedQuery<AdsObject> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public AdsObject get(int id) {
        AdsObject result = new AdsObject();
        try {
            Dao<AdsObject, String> dao = this.getController();
            QueryBuilder<AdsObject, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<AdsObject> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void createOrUpdate(final AdsObject obj) {
        if (obj != null) {
            try {
                final Dao<AdsObject, String> dao = this.getController();
                dao.callBatchTasks(new Callable<Void>() {
                    public Void call() throws SQLException {
                        dao.createOrUpdate(obj);
                        return null;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
