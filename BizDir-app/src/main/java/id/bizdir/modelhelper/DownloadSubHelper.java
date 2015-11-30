package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.DownloadSubList;
import id.bizdir.model.DownloadSub;

/**
 * Created by Hendry on 20/04/2015.
 */

public class DownloadSubHelper {

    private Dao<DownloadSub, String> controller = null;

    private Dao<DownloadSub, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(DownloadSub.class);
        }
        return controller;
    }

    public List<DownloadSub> getAll() {
        List<DownloadSub> result = new ArrayList<>();
        try {
            Dao<DownloadSub, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<DownloadSub, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            //ordersQB.orderByRaw("name ASC");
            PreparedQuery<DownloadSub> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<DownloadSub> getAll(int rootId) {
        List<DownloadSub> result = new ArrayList<>();
        try {
            Dao<DownloadSub, String> dao = this.getController();
            QueryBuilder<DownloadSub, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("rootId", rootId);
            ordersQB.orderByRaw("name ASC");
            PreparedQuery<DownloadSub> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public DownloadSub get(int id) {
        DownloadSub result = new DownloadSub();
        try {
            Dao<DownloadSub, String> dao = this.getController();
            QueryBuilder<DownloadSub, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<DownloadSub> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<DownloadSub> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<DownloadSub, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (DownloadSub obj : listOfData) {
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
        DownloadSubList list = App.getGson().fromJson(jsonString, DownloadSubList.class);
        addAll(list.download_sub);
    }
}
