package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.ForumCategoryList;
import id.bizdir.model.ForumCategory;
import id.bizdir.model.ForumThread;

/**
 * Created by Hendry on 20/04/2015.
 */

public class ForumCategoryHelper {

    private Dao<ForumCategory, String> controller = null;

    private Dao<ForumCategory, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(ForumCategory.class);
        }
        return controller;
    }

    public List<ForumCategory> getAll() {
        List<ForumCategory> result = new ArrayList<>();
        try {
            Dao<ForumCategory, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<ForumCategory, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            ordersQB.orderByRaw("title ASC");
            PreparedQuery<ForumCategory> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<ForumCategory> getPopularCategories() {
        List<ForumCategory> result = new ArrayList<>();
        ForumThreadHelper forumThreadHelper = new ForumThreadHelper();
        List<ForumThread> forumThreadList = forumThreadHelper.getPopularCategories();
        if (forumThreadList.size() > 0) {
            for (ForumThread forumThread : forumThreadList) {
                ForumCategory forumCategory = get(forumThread.getCategoryId());
                if (forumCategory != null) {
                    result.add(forumCategory);
                }
            }
        }
        return result;
    }

    public ForumCategory get(int id) {
        ForumCategory result = new ForumCategory();
        try {
            Dao<ForumCategory, String> dao = this.getController();
            QueryBuilder<ForumCategory, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<ForumCategory> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<ForumCategory> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<ForumCategory, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (ForumCategory obj : listOfData) {
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
        ForumCategoryList list = App.getGson().fromJson(jsonString, ForumCategoryList.class);
        addAll(list.forum_category);
    }

}
