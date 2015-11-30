package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.CommonList;
import id.bizdir.model.Common;

/**
 * Created by Hendry on 20/04/2015.
 */

public class CommonHelper {

    private Dao<Common, String> controller = null;

    private Dao<Common, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(Common.class);
        }
        return controller;
    }

    public List<Common> getAll() {
        List<Common> result = new ArrayList<>();
        try {
            Dao<Common, String> dao = this.getController();
            //result = dao.queryForAll();
            QueryBuilder<Common, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("status", 1);
            PreparedQuery<Common> pq = ordersQB.prepare();
            result = dao.query(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public Common get(int id) {
        Common result = new Common();
        try {
            Dao<Common, String> dao = this.getController();
            QueryBuilder<Common, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<Common> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public Common getAboutBizdir() {
        Common result = getCommon("about");
        return result;
    }

    public Common getFaq() {
        Common result = getCommon("faq");
        return result;
    }

    public Common getBizDirId() {
        Common result = getCommon("bizdirid");
        return result;
    }

    public Common getCommon(String nameId) {
        Common result = new Common();
        try {
            Dao<Common, String> dao = this.getController();
            QueryBuilder<Common, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("name", nameId);
            PreparedQuery<Common> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void addAll(final List<Common> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<Common, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (Common obj : listOfData) {
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
        CommonList list = App.getGson().fromJson(jsonString, CommonList.class);
        addAll(list.common);
    }

}
