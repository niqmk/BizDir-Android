package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.R;
import id.bizdir.list.ProvinceList;
import id.bizdir.model.Province;

/**
 * Created by Hendry on 20/04/2015.
 */

public class ProvinceHelper {

    private Dao<Province, String> controller = null;

    private Dao<Province, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(Province.class);
        }
        return controller;
    }

    public List<Province> getAll() {
        List<Province> result = new ArrayList<>();
        try {
            Dao<Province, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<Province, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            ordersQB.orderByRaw("name ASC");
            PreparedQuery<Province> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        result.add(0, getIndonesia());
        return result;
    }

    public Province get(int id) {
        Province result = new Province();
        try {
            Dao<Province, String> dao = this.getController();
            QueryBuilder<Province, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<Province> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Province getIndonesia() {
        Province result = new Province();
        result.setId(0);
        result.setName(App.getContext().getString(R.string.location_indonesia));
        return result;
    }

    public void addAll(final List<Province> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<Province, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (Province obj : listOfData) {
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
        ProvinceList list = App.getGson().fromJson(jsonString, ProvinceList.class);
        addAll(list.province);
    }

}
