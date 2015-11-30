package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.EventList;
import id.bizdir.model.Event;

/**
 * Created by Hendry on 20/04/2015.
 */

public class EventHelper {

    private Dao<Event, String> controller = null;

    private Dao<Event, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(Event.class);
        }
        return controller;
    }

    public List<Event> getAll() {
        List<Event> result = new ArrayList<>();
        try {
            Dao<Event, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<Event, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            //ordersQB.orderByRaw("name ASC");
            PreparedQuery<Event> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Event> getAll(int categoryId) {
        List<Event> result = new ArrayList<>();
        try {
            Dao<Event, String> dao = this.getController();
            QueryBuilder<Event, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("categoryId", categoryId);
            ordersQB.orderByRaw("startDate DESC");
            PreparedQuery<Event> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Event get(int id) {
        Event result = new Event();
        try {
            Dao<Event, String> dao = this.getController();
            QueryBuilder<Event, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<Event> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<Event> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<Event, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (Event obj : listOfData) {
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
        EventList list = App.getGson().fromJson(jsonString, EventList.class);
        addAll(list.event);
    }
}
