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
import id.bizdir.list.AnggotaGalleryList;
import id.bizdir.model.AnggotaGallery;

/**
 * Created by Hendry on 20/04/2015.
 */

public class AnggotaGalleryHelper {

    private Dao<AnggotaGallery, String> controller = null;

    private Dao<AnggotaGallery, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(AnggotaGallery.class);
        }
        return controller;
    }

    public List<AnggotaGallery> getAll() {
        List<AnggotaGallery> result = new ArrayList<>();
        try {
            Dao<AnggotaGallery, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<AnggotaGallery, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            PreparedQuery<AnggotaGallery> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<AnggotaGallery> getAll(int anggotaId) {
        List<AnggotaGallery> result = new ArrayList<>();
        try {
            Dao<AnggotaGallery, String> dao = this.getController();
            QueryBuilder<AnggotaGallery, String> queryBuilder = dao
                    .queryBuilder();
            Where<AnggotaGallery, String> where = queryBuilder.where();
            where.and(where.eq("status", 1),
                    where.eq("anggotaId", anggotaId));
            //queryBuilder.orderByRaw("type ASC, PlayingTime ASC");
            queryBuilder.orderByRaw("type DESC");
            PreparedQuery<AnggotaGallery> pq = queryBuilder.prepare();
            result = dao.query(pq);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public AnggotaGallery get(int id) {
        AnggotaGallery result = new AnggotaGallery();
        try {
            Dao<AnggotaGallery, String> dao = this.getController();
            QueryBuilder<AnggotaGallery, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<AnggotaGallery> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<AnggotaGallery> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<AnggotaGallery, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (AnggotaGallery obj : listOfData) {
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
        AnggotaGalleryList list = App.getGson().fromJson(jsonString, AnggotaGalleryList.class);
        addAll(list.anggota_gallery);
    }

}
