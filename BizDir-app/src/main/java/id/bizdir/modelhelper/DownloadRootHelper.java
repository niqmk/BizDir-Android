package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.DownloadRootList;
import id.bizdir.model.DownloadRoot;

/**
 * Created by Hendry on 20/04/2015.
 */

public class DownloadRootHelper {

    private Dao<DownloadRoot, String> controller = null;

    private Dao<DownloadRoot, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(DownloadRoot.class);
        }
        return controller;
    }

    public List<DownloadRoot> getAll() {
        List<DownloadRoot> result = new ArrayList<>();
        try {
            Dao<DownloadRoot, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<DownloadRoot, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            //ordersQB.orderByRaw("name ASC");
            PreparedQuery<DownloadRoot> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public DownloadRoot get(int id) {
        DownloadRoot result = new DownloadRoot();
        try {
            Dao<DownloadRoot, String> dao = this.getController();
            QueryBuilder<DownloadRoot, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<DownloadRoot> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<DownloadRoot> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<DownloadRoot, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (DownloadRoot obj : listOfData) {
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
        DownloadRootList list = App.getGson().fromJson(jsonString, DownloadRootList.class);
        addAll(list.download_root);
    }

}
