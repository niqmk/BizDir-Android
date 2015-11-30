package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.OpportunityList;
import id.bizdir.model.Opportunity;

/**
 * Created by Hendry on 20/04/2015.
 */

public class OpportunityHelper {

    private Dao<Opportunity, String> controller = null;

    private Dao<Opportunity, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(Opportunity.class);
        }
        return controller;
    }

    public List<Opportunity> getAll() {
        List<Opportunity> result = new ArrayList<>();
        try {
            Dao<Opportunity, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<Opportunity, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            //ordersQB.orderByRaw("name ASC");
            PreparedQuery<Opportunity> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Opportunity> getAll(int categoryId) {
        List<Opportunity> result = new ArrayList<>();
        try {
            Dao<Opportunity, String> dao = this.getController();
            QueryBuilder<Opportunity, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("categoryId", categoryId);
            ordersQB.orderByRaw("startDate DESC");
            PreparedQuery<Opportunity> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public long getCount() {
        long result = 0;
        try {
            Dao<Opportunity, String> dao = this.getController();
            QueryBuilder<Opportunity, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            result = ordersQB.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Opportunity get(int id) {
        Opportunity result = new Opportunity();
        try {
            Dao<Opportunity, String> dao = this.getController();
            QueryBuilder<Opportunity, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<Opportunity> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<Opportunity> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<Opportunity, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (Opportunity obj : listOfData) {
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

    public void updateCount(int id, int totalCount) {
        try {
            final Dao<Opportunity, String> dao = this.getController();
            UpdateBuilder<Opportunity, String> updateBuilder = dao.updateBuilder();
            updateBuilder.where().eq("id", id);
            updateBuilder.updateColumnValue("viewCount", totalCount);
            updateBuilder.update();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addAll(String jsonString) {
        OpportunityList list = App.getGson().fromJson(jsonString, OpportunityList.class);
        addAll(list.opportunity);
    }
}
