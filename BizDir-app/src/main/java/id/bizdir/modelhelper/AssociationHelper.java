package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.model.Association;

/**
 * Created by Hendry S on 25/09/2015.
 */

public class AssociationHelper {
    private Dao<Association, String> controller = null;

    private Dao<Association, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(Association.class);
        }
        return controller;
    }

    public List<Association> getAll() {
        List<Association> result = new ArrayList<>();
        try {
            Dao<Association, String> dao = this.getController();
            QueryBuilder<Association, String> ordersQB = dao.queryBuilder();
            ordersQB.orderByRaw("name ASC");
            PreparedQuery<Association> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Association get(int id) {
        Association result = new Association();
        try {
            Dao<Association, String> dao = this.getController();
            QueryBuilder<Association, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<Association> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<Association> listOfData) {
        App.getDatabaseHelper().clearTable(Association.class);
        if (listOfData.size() > 0) {
            try {
                final Dao<Association, String> dao = this.getController();
                dao.callBatchTasks(new Callable<Void>() {
                    public Void call() throws SQLException {
                        for (Association obj : listOfData) {
                            dao.create(obj);
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
