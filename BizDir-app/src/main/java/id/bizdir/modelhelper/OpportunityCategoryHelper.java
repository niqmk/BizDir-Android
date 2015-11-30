package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.OpportunityCategoryList;
import id.bizdir.model.OpportunityCategory;

/**
 * Created by Hendry on 20/04/2015.
 */

public class OpportunityCategoryHelper {

    private Dao<OpportunityCategory, String> controller = null;

    private Dao<OpportunityCategory, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(OpportunityCategory.class);
        }
        return controller;
    }

    public List<OpportunityCategory> getAll() {
        List<OpportunityCategory> result = new ArrayList<>();
        try {
            Dao<OpportunityCategory, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<OpportunityCategory, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            //ordersQB.orderByRaw("name ASC");
            PreparedQuery<OpportunityCategory> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public OpportunityCategory get(int id) {
        OpportunityCategory result = new OpportunityCategory();
        try {
            Dao<OpportunityCategory, String> dao = this.getController();
            QueryBuilder<OpportunityCategory, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<OpportunityCategory> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<OpportunityCategory> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<OpportunityCategory, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (OpportunityCategory obj : listOfData) {
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
        OpportunityCategoryList list = App.getGson().fromJson(jsonString, OpportunityCategoryList.class);
        addAll(list.opportunity_category);
    }
}
