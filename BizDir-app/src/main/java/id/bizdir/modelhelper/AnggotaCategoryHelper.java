package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.AnggotaCategoryList;
import id.bizdir.model.AnggotaCategory;

/**
 * Created by Hendry on 20/04/2015.
 */

public class AnggotaCategoryHelper {

    private Dao<AnggotaCategory, String> controller = null;

    private Dao<AnggotaCategory, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(AnggotaCategory.class);
        }
        return controller;
    }

    public List<AnggotaCategory> getAll() {
        List<AnggotaCategory> result = new ArrayList<>();
        try {
            Dao<AnggotaCategory, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<AnggotaCategory, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            ordersQB.orderByRaw("title ASC");
            PreparedQuery<AnggotaCategory> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<CharSequence> getAllArray() {
        List<CharSequence> result = new ArrayList<>();
        List<AnggotaCategory> anggotaCategoryList = getAll();
        for (AnggotaCategory obj : anggotaCategoryList) {
            result.add(obj.getTitle());
        }
        return result;
    }

    public CharSequence[] getCharSequence() {
        List<CharSequence> charSequences = getAllArray();
        return charSequences.toArray(new
                CharSequence[charSequences.size()]);
    }

    public AnggotaCategory get(int id) {
        AnggotaCategory result = new AnggotaCategory();
        try {
            Dao<AnggotaCategory, String> dao = this.getController();
            QueryBuilder<AnggotaCategory, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<AnggotaCategory> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<AnggotaCategory> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<AnggotaCategory, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (AnggotaCategory obj : listOfData) {
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
        AnggotaCategoryList list = App.getGson().fromJson(jsonString, AnggotaCategoryList.class);
        addAll(list.anggota_category);
    }

}
