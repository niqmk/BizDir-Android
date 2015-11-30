package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.model.WalkThrough;

/**
 * Created by Hendry S on 25/09/2015.
 */

public class WalkthroughHelper {
    private Dao<WalkThrough, String> controller = null;

    private Dao<WalkThrough, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(WalkThrough.class);
        }
        return controller;
    }

    public List<WalkThrough> getAll() {
        List<WalkThrough> result = new ArrayList<>();
        try {
            Dao<WalkThrough, String> dao = this.getController();
            QueryBuilder<WalkThrough, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            ordersQB.orderByRaw("'order' ASC");
            PreparedQuery<WalkThrough> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public WalkThrough get(int id) {
        WalkThrough result = new WalkThrough();
        try {
            Dao<WalkThrough, String> dao = this.getController();
            QueryBuilder<WalkThrough, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<WalkThrough> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<WalkThrough> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<WalkThrough, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (WalkThrough obj : listOfData) {
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

}
