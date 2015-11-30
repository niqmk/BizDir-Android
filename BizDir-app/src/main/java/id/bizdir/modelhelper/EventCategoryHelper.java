package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.EventCategoryList;
import id.bizdir.model.EventCategory;

/**
 * Created by Hendry on 20/04/2015.
 */

public class EventCategoryHelper {

    private Dao<EventCategory, String> controller = null;

    private Dao<EventCategory, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(EventCategory.class);
        }
        return controller;
    }

    public List<EventCategory> getAll() {
        List<EventCategory> result = new ArrayList<>();
        try {
            Dao<EventCategory, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<EventCategory, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            PreparedQuery<EventCategory> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public EventCategory get(int id) {
        EventCategory result = new EventCategory();
        try {
            Dao<EventCategory, String> dao = this.getController();
            QueryBuilder<EventCategory, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<EventCategory> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<EventCategory> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<EventCategory, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (EventCategory obj : listOfData) {
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
        EventCategoryList list = App.getGson().fromJson(jsonString, EventCategoryList.class);
        addAll(list.event_category);
    }
}
