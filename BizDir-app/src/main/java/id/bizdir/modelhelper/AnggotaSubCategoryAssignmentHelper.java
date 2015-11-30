package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.AnggotaSubCategoryAssignmentList;
import id.bizdir.model.AnggotaSubCategoryAssignment;

/**
 * Created by Hendry on 20/04/2015.
 */

public class AnggotaSubCategoryAssignmentHelper {

    private Dao<AnggotaSubCategoryAssignment, String> controller = null;

    private Dao<AnggotaSubCategoryAssignment, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(AnggotaSubCategoryAssignment.class);
        }
        return controller;
    }

    public List<AnggotaSubCategoryAssignment> getAll() {
        List<AnggotaSubCategoryAssignment> result = new ArrayList<>();
        try {
            Dao<AnggotaSubCategoryAssignment, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<AnggotaSubCategoryAssignment, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            PreparedQuery<AnggotaSubCategoryAssignment> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<AnggotaSubCategoryAssignment> getAll(int subCategoryId) {
        List<AnggotaSubCategoryAssignment> result = new ArrayList<>();
        try {
            Dao<AnggotaSubCategoryAssignment, String> dao = this.getController();
            QueryBuilder<AnggotaSubCategoryAssignment, String> ordersQB = dao.queryBuilder();
            Where<AnggotaSubCategoryAssignment, String> where = ordersQB.where();
            where.and(
                    where.eq("subCategoryId", subCategoryId),
                    where.eq("status", 1));
            PreparedQuery<AnggotaSubCategoryAssignment> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public AnggotaSubCategoryAssignment get(int id) {
        AnggotaSubCategoryAssignment result = new AnggotaSubCategoryAssignment();
        try {
            Dao<AnggotaSubCategoryAssignment, String> dao = this.getController();
            QueryBuilder<AnggotaSubCategoryAssignment, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<AnggotaSubCategoryAssignment> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<AnggotaSubCategoryAssignment> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<AnggotaSubCategoryAssignment, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (AnggotaSubCategoryAssignment obj : listOfData) {
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
        AnggotaSubCategoryAssignmentList list = App.getGson().fromJson(jsonString, AnggotaSubCategoryAssignmentList.class);
        addAll(list.anggota_sub_category_assignment);
    }
}
