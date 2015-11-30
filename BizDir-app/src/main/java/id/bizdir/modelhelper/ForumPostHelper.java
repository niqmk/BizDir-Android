package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.ForumPostList;
import id.bizdir.model.ForumPost;

/**
 * Created by Hendry on 20/04/2015.
 */

public class ForumPostHelper {

    private Dao<ForumPost, String> controller = null;

    private Dao<ForumPost, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(ForumPost.class);
        }
        return controller;
    }

    public List<ForumPost> getAll() {
        List<ForumPost> result = new ArrayList<>();
        try {
            Dao<ForumPost, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<ForumPost, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            //ordersQB.orderByRaw("name ASC");
            PreparedQuery<ForumPost> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<ForumPost> getAll(int threadId) {
        List<ForumPost> result = new ArrayList<>();
        try {
            Dao<ForumPost, String> dao = this.getController();
            QueryBuilder<ForumPost, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("threadId", threadId);
            ordersQB.orderByRaw("createDate ASC");
            PreparedQuery<ForumPost> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public ForumPost get(int id) {
        ForumPost result = new ForumPost();
        try {
            Dao<ForumPost, String> dao = this.getController();
            QueryBuilder<ForumPost, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<ForumPost> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<ForumPost> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<ForumPost, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (ForumPost obj : listOfData) {
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

    public void add(final ForumPost obj) {
        if (obj != null) {
            try {
                final Dao<ForumPost, String> dao = this.getController();
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

    public void addAll(String jsonString) {
        ForumPostList list = App.getGson().fromJson(jsonString, ForumPostList.class);
        addAll(list.forum_post);
    }
}
