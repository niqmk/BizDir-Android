package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.model.Interest;

/**
 * Created by Hendry on 20/04/2015.
 */

public class InterestHelper {

    private Dao<Interest, String> controller = null;

    private Dao<Interest, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(Interest.class);
        }
        return controller;
    }

    public List<Interest> getAll() {
        List<Interest> result = new ArrayList<>();
        try {
            Dao<Interest, String> dao = this.getController();
            result = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<CharSequence> getAllArray() {
        List<CharSequence> result = new ArrayList<>();
        List<Interest> interestList = getAll();
        for (Interest obj : interestList) {
            result.add(obj.getName());
        }
        return result;
    }

    public CharSequence[] getCharSequence() {
        List<CharSequence> charSequences = getAllArray();
        return charSequences.toArray(new
                CharSequence[charSequences.size()]);
    }

    public List<Interest> getSelectedAll() {
        List<Interest> result = new ArrayList<>();
        try {
            Dao<Interest, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<Interest, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            //ordersQB.orderByRaw("name ASC");
            PreparedQuery<Interest> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Interest get() {
        Interest result = new Interest();
        try {
            Dao<Interest, String> dao = this.getController();
            QueryBuilder<Interest, String> queryBuilder = dao.queryBuilder();
            PreparedQuery<Interest> pq = queryBuilder.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Interest get(int id) {
        Interest result = new Interest();
        try {
            Dao<Interest, String> dao = this.getController();
            QueryBuilder<Interest, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<Interest> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<Interest> listOfData) {
        if (listOfData.size() > 0) {
            try {
                final Dao<Interest, String> dao = this.getController();
                dao.callBatchTasks(new Callable<Void>() {
                    public Void call() throws SQLException {
                        for (Interest obj : listOfData) {
                            dao.create(obj);
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
        App.getDatabaseHelper().clearTable(Interest.class);
    }
}
