package id.bizdir.modelhelper;

import com.google.gson.stream.JsonReader;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.AnggotaList;
import id.bizdir.model.Anggota;
import id.bizdir.model.AnggotaSubCategoryAssignment;

/**
 * Created by Hendry on 20/04/2015.
 */

public class AnggotaHelper {

    private Dao<Anggota, String> controller = null;

    private Dao<Anggota, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(Anggota.class);
        }
        return controller;
    }

    public List<Anggota> getAll() {
        List<Anggota> result = new ArrayList<>();
        try {
            Dao<Anggota, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<Anggota, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            PreparedQuery<Anggota> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Anggota> getAll(int categorySubId) {
        List<Anggota> result = new ArrayList<>();
        AnggotaSubCategoryAssignmentHelper categoryAssigmentHelper = new AnggotaSubCategoryAssignmentHelper();
        List<AnggotaSubCategoryAssignment> categoryAssigmentList = categoryAssigmentHelper.getAll(categorySubId);
        if (!categoryAssigmentList.isEmpty()) {
            for (AnggotaSubCategoryAssignment categoryAssigment : categoryAssigmentList) {
                Anggota memberKadin = get(categoryAssigment.getAnggotaId());
                if (memberKadin != null) {
                    //memberKadin.setCityName(categoryAssigment.g);
                    result.add(memberKadin);
                }
            }
        }
        Collections.sort(result, new Comparator<Anggota>() {
            public int compare(Anggota member1, Anggota member2) {
                return member1.getName().compareToIgnoreCase(member2.getName());
            }
        });
        return result;
    }

    public List<Anggota> getAll(int categorySubId, int provinceId) {
        List<Anggota> result = new ArrayList<>();
        AnggotaSubCategoryAssignmentHelper categoryAssigmentHelper = new AnggotaSubCategoryAssignmentHelper();
        List<AnggotaSubCategoryAssignment> categoryAssigmentList = categoryAssigmentHelper.getAll(categorySubId);
        if (!categoryAssigmentList.isEmpty()) {
            for (AnggotaSubCategoryAssignment categoryAssigment : categoryAssigmentList) {
                Anggota memberKadin = get(categoryAssigment.getAnggotaId());
                if (memberKadin != null) {
                    if (memberKadin.getProvinceId() == provinceId) {
                        result.add(memberKadin);
                    }
                }
            }
        }
        Collections.sort(result, new Comparator<Anggota>() {
            public int compare(Anggota member1, Anggota member2) {
                return member1.getName().compareToIgnoreCase(member2.getName());
            }
        });
        return result;
    }

    public List<Anggota> getAll(int categorySubId, int provinceId, int cityId) {
        List<Anggota> result = new ArrayList<>();
        AnggotaSubCategoryAssignmentHelper categoryAssigmentHelper = new AnggotaSubCategoryAssignmentHelper();
        List<AnggotaSubCategoryAssignment> categoryAssigmentList = categoryAssigmentHelper.getAll(categorySubId);
        if (!categoryAssigmentList.isEmpty()) {
            for (AnggotaSubCategoryAssignment categoryAssigment : categoryAssigmentList) {
                Anggota memberKadin = get(categoryAssigment.getAnggotaId());
                if (memberKadin != null) {
                    if (memberKadin.getProvinceId() == provinceId && memberKadin.getCityId() == cityId) {
                        result.add(memberKadin);
                    }
                }
            }
        }
        Collections.sort(result, new Comparator<Anggota>() {
            public int compare(Anggota member1, Anggota member2) {
                return member1.getName().compareToIgnoreCase(member2.getName());
            }
        });
        return result;
    }

    public List<Anggota> getAll(String keyWord) {
        List<Anggota> result = new ArrayList<>();
        try {
            Dao<Anggota, String> dao = this.getController();
            QueryBuilder<Anggota, String> queryBuilder = dao
                    .queryBuilder();
            Where<Anggota, String> where = queryBuilder.where();
            where.or(
                    where.like("name", "%" + keyWord + "%"),
                    where.like("address", "%" + keyWord + "%"),
                    where.like("product", "%" + keyWord + "%"));
            //where.like("name", keyWord + "%");
            queryBuilder.orderByRaw("name ASC");
            PreparedQuery<Anggota> pq = queryBuilder.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Anggota> getAll(String keyWord, int provinceId) {
        List<Anggota> result = new ArrayList<>();
        try {
            Dao<Anggota, String> dao = this.getController();
            QueryBuilder<Anggota, String> queryBuilder = dao
                    .queryBuilder();
            Where<Anggota, String> where = queryBuilder.where();
            where.or(
                    where.and(
                            where.eq("provinceId", provinceId),
                            where.like("name", "%" + keyWord + "%"))
                    , where.and(
                            where.eq("provinceId", provinceId),
                            where.like("address", "%" + keyWord + "%"))
                    , where.and(
                            where.eq("provinceId", provinceId),
                            where.like("product", "%" + keyWord + "%"))
            );
            queryBuilder.orderByRaw("name ASC");
            PreparedQuery<Anggota> pq = queryBuilder.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<Anggota> getAll(String keyWord, int provinceId, int cityId) {
        List<Anggota> result = new ArrayList<>();
        try {
            Dao<Anggota, String> dao = this.getController();
            QueryBuilder<Anggota, String> queryBuilder = dao
                    .queryBuilder();
            Where<Anggota, String> where = queryBuilder.where();
            where.or(
                    where.and(
                            where.eq("provinceId", provinceId),
                            where.eq("cityId", cityId),
                            where.like("name", "%" + keyWord + "%"))
                    , where.and(
                            where.eq("provinceId", provinceId),
                            where.eq("cityId", cityId),
                            where.like("address", "%" + keyWord + "%"))
                    , where.and(
                            where.eq("provinceId", provinceId),
                            where.eq("cityId", cityId),
                            where.like("product", "%" + keyWord + "%"))
            );
            queryBuilder.orderByRaw("name ASC");
            PreparedQuery<Anggota> pq = queryBuilder.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Anggota get(int id) {
        Anggota result = new Anggota();
        try {
            Dao<Anggota, String> dao = this.getController();
            QueryBuilder<Anggota, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<Anggota> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void updateFavorite(int memberId, boolean isFavorite) throws SQLException {
        final Dao<Anggota, String> dao = this.getController();
        UpdateBuilder<Anggota, String> updateBuilder = dao.updateBuilder();
        if (isFavorite) {
            updateBuilder.updateColumnValue("favorite", 1);
        } else {
            updateBuilder.updateColumnValue("favorite", 0);
        }
        updateBuilder.where().eq("id", memberId);
        dao.update(updateBuilder.prepare());
    }

    public List<Anggota> getFavoriteAll() {
        List<Anggota> result = new ArrayList<>();
        try {
            Dao<Anggota, String> dao = this.getController();
            QueryBuilder<Anggota, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("favorite", 1);
            ordersQB.orderByRaw("name ASC");
            PreparedQuery<Anggota> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<Anggota> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<Anggota, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (Anggota obj : listOfData) {
                                dao.createOrUpdate(obj);
                                //dao.create(obj);
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
        AnggotaList list = App.getGson().fromJson(jsonString, AnggotaList.class);
        addAll(list.anggota);
    }

    public void addAll(InputStream jsonInputStream) throws IOException {
        JsonReader r = null;
        try {
            Reader reader = new BufferedReader(new InputStreamReader(jsonInputStream));
            r = new JsonReader(reader);
            AnggotaList list = App.getGson().fromJson(r, AnggotaList.class);
            addAll(list.anggota);
        } finally {
            if (null != r) {
                r.close();
            }
        }
    }

    public void add(final Anggota obj) {
        if (obj != null) {
            try {
                final Dao<Anggota, String> dao = this.getController();
                dao.callBatchTasks(new Callable<Void>() {
                    public Void call() throws SQLException {
                        dao.createOrUpdate(obj);
                        return null;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
