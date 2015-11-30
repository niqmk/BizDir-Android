package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.AnggotaSubCategoryList;
import id.bizdir.model.AnggotaSubCategory;

/**
 * Created by Hendry on 20/04/2015.
 */

public class AnggotaSubCategoryHelper {

    private Dao<AnggotaSubCategory, String> controller = null;

    private Dao<AnggotaSubCategory, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(AnggotaSubCategory.class);
        }
        return controller;
    }

    public List<AnggotaSubCategory> getAll() {
        List<AnggotaSubCategory> result = new ArrayList<>();
        try {
            Dao<AnggotaSubCategory, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<AnggotaSubCategory, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            PreparedQuery<AnggotaSubCategory> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<AnggotaSubCategory> getAll(int categoryId) {
        List<AnggotaSubCategory> result = new ArrayList<>();
        try {
            Dao<AnggotaSubCategory, String> dao = this.getController();
            QueryBuilder<AnggotaSubCategory, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("categoryId", categoryId);
            //ordersQB.orderByRaw("OrderNo DESC");
            PreparedQuery<AnggotaSubCategory> pq = ordersQB.prepare();
            result = dao.query(pq);
            // result =
            // dao.queryBuilder().orderByRaw("Name COLLATE NOCASE").query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public AnggotaSubCategory get(int id) {
        AnggotaSubCategory result = new AnggotaSubCategory();
        try {
            Dao<AnggotaSubCategory, String> dao = this.getController();
            QueryBuilder<AnggotaSubCategory, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<AnggotaSubCategory> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<AnggotaSubCategory> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<AnggotaSubCategory, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (AnggotaSubCategory obj : listOfData) {
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
        AnggotaSubCategoryList list = App.getGson().fromJson(jsonString, AnggotaSubCategoryList.class);
        addAll(list.anggota_sub_category);
    }

}
