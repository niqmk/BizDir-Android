package id.bizdir.modelhelper;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import id.bizdir.App;
import id.bizdir.list.TableSynchList;
import id.bizdir.model.TableSynch;

/**
 * Created by Hendry on 20/04/2015.
 */

public class TableSynchHelper {

    private Dao<TableSynch, String> controller = null;

    private Dao<TableSynch, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(TableSynch.class);
        }
        return controller;
    }

    public List<TableSynch> getAll() {
        List<TableSynch> result = new ArrayList<>();
        try {
            Dao<TableSynch, String> dao = this.getController();
            result = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public TableSynch get(String tableName) {
        TableSynch result = new TableSynch();
        try {
            Dao<TableSynch, String> dao = this.getController();
            QueryBuilder<TableSynch, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("table", tableName);
            PreparedQuery<TableSynch> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    public void update(String tableNameDate, Date lastSync) throws SQLException {
        final Dao<TableSynch, String> dao = this.getController();
        UpdateBuilder<TableSynch, String> updateBuilder = dao.updateBuilder();
        updateBuilder.updateColumnValue("lastSynch", lastSync);
        updateBuilder.where().eq("table", tableNameDate);
        dao.update(updateBuilder.prepare());
    }

    public void addAll(final List<TableSynch> listOfData) {
        if (listOfData != null) {
            if (listOfData.size() > 0) {
                try {
                    final Dao<TableSynch, String> dao = this.getController();
                    dao.callBatchTasks(new Callable<Void>() {
                        public Void call() throws SQLException {
                            for (TableSynch obj : listOfData) {
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
        TableSynchList list = App.getGson().fromJson(jsonString, TableSynchList.class);
        addAll(list.tableSynch);
    }
}
