package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.ForumThreadList;
import id.bizdir.model.ForumThread;

/**
 * Created by Hendry on 20/04/2015.
 */

public class ForumThreadHelper {

    private Dao<ForumThread, String> controller = null;

    private Dao<ForumThread, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(ForumThread.class);
        }
        return controller;
    }

    public List<ForumThread> getAll() {
        List<ForumThread> result = new ArrayList<>();
        try {
            Dao<ForumThread, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<ForumThread, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            //ordersQB.orderByRaw("name ASC");
            PreparedQuery<ForumThread> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<ForumThread> getAll(int categoryId) {
        List<ForumThread> result = new ArrayList<>();
        try {
            Dao<ForumThread, String> dao = this.getController();
            QueryBuilder<ForumThread, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            ordersQB.where().eq("categoryId", categoryId);
            ordersQB.orderByRaw("createDate DESC");
            PreparedQuery<ForumThread> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<ForumThread> getPopularThread() {
        List<ForumThread> result = new ArrayList<>();
        try {
            Dao<ForumThread, String> dao = this.getController();
            QueryBuilder<ForumThread, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            ordersQB.orderByRaw("viewCount DESC");
            PreparedQuery<ForumThread> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<ForumThread> getPopularCategories() {
        List<ForumThread> result = new ArrayList<>();
        try {
            Dao<ForumThread, String> dao = this.getController();
            QueryBuilder<ForumThread, String> ordersQB = dao.queryBuilder();
            //ordersQB.selectRaw("SUM (viewCount)");
            ordersQB.where().eq("status", 1);
            ordersQB.groupBy("categoryId");
            ordersQB.orderByRaw("viewCount DESC");
            PreparedQuery<ForumThread> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ForumThread get(int id) {
        ForumThread result = new ForumThread();
        try {
            Dao<ForumThread, String> dao = this.getController();
            QueryBuilder<ForumThread, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<ForumThread> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<ForumThread> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<ForumThread, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (ForumThread obj : listOfData) {
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
        ForumThreadList list = App.getGson().fromJson(jsonString, ForumThreadList.class);
        addAll(list.forum_thread);
    }
}
