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
import id.bizdir.list.CityList;
import id.bizdir.model.City;

/**
 * Created by Hendry on 20/04/2015.
 */

public class CityHelper {

    private Dao<City, String> controller = null;

    private Dao<City, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(City.class);
        }
        return controller;
    }

    public List<City> getAll() {
        List<City> result = new ArrayList<>();
        try {
            Dao<City, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<City, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            ordersQB.orderByRaw("name ASC");
            PreparedQuery<City> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        result.add(0, getIndonesia());
        return result;
    }

    public City get(int id) {
        City result = new City();
        try {
            Dao<City, String> dao = this.getController();
            QueryBuilder<City, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<City> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private City getIndonesia() {
        City result = new City();
        result.setId(0);
        result.setName(App.getContext().getString(R.string.location_indonesia));
        result.setProvinceId(0);
        result.setProvinceName(App.getContext().getString(R.string.location_indonesia));
        return result;
    }

    public void addAll(final List<City> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<City, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (City obj : listOfData) {
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
        CityList list = App.getGson().fromJson(jsonString, CityList.class);
        addAll(list.city);
    }
}
