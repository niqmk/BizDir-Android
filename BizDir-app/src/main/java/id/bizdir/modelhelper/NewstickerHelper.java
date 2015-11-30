package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.model.Newsticker;

/**
 * Created by Hendry on 20/04/2015.
 */

public class NewstickerHelper {

    private Dao<Newsticker, String> controller = null;

    private Dao<Newsticker, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(Newsticker.class);
        }
        return controller;
    }

    public List<Newsticker> getAll() {
        List<Newsticker> result = new ArrayList<>();
        try {
            Dao<Newsticker, String> dao = this.getController();
            result = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Newsticker get(int id) {
        Newsticker result = new Newsticker();
        try {
            Dao<Newsticker, String> dao = this.getController();
            QueryBuilder<Newsticker, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<Newsticker> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<Newsticker> listOfData) {
        if (listOfData.size() > 0) {
            try {
                final Dao<Newsticker, String> dao = this.getController();
                dao.callBatchTasks(new Callable<Void>() {
                    public Void call() throws SQLException {
                        for (Newsticker obj : listOfData) {
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

    public void clearAll() {
        App.getDatabaseHelper().clearTable(Newsticker.class);
    }
}
