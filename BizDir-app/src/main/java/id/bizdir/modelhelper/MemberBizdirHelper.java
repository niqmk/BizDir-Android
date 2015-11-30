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
import id.bizdir.model.MemberBizdir;

/**
 * Created by Hendry on 20/04/2015.
 */

public class MemberBizdirHelper {

    private Dao<MemberBizdir, String> controller = null;

    private Dao<MemberBizdir, String> getController()
            throws SQLException {
        if (controller == null) {
            controller = App.getDatabaseHelper().getDao(MemberBizdir.class);
        }
        return controller;
    }

    public List<MemberBizdir> getAll() {
        List<MemberBizdir> result = new ArrayList<>();
        try {
            Dao<MemberBizdir, String> dao = this.getController();
            result = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public MemberBizdir get() {
        MemberBizdir result = null;
        List<MemberBizdir> memberBizdirList = getAll();
        if (memberBizdirList != null) {
            if (memberBizdirList.size() > 0) {
                result = memberBizdirList.get(0);
            }
        }
        return result;
    }

    public MemberBizdir get(int id) {
        MemberBizdir result = new MemberBizdir();
        try {
            Dao<MemberBizdir, String> dao = this.getController();
            QueryBuilder<MemberBizdir, String> ordersQB = dao.queryBuilder();
            ordersQB.where().eq("id", id);
            PreparedQuery<MemberBizdir> pq = ordersQB.prepare();
            result = dao.queryForFirst(pq);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void add(final MemberBizdir obj) {
        if (obj != null) {
            clearAll();
            final InterestHelper interestHelper = new InterestHelper();
            interestHelper.clearAll();
            final List<Interest> interestList = new ArrayList<>();
            try {
                final Dao<MemberBizdir, String> dao = this.getController();
                dao.callBatchTasks(new Callable<Void>() {
                    public Void call() throws SQLException {
                        dao.create(obj);
                        if (obj.getInterest().size() > 0) {
                            for (Interest interest : obj.getInterest()) {
                                interest.setMemberBizdir(obj);
                                interestList.add(interest);
                            }
                            interestHelper.addAll(interestList);
                        }
                        return null;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addAll(final List<MemberBizdir> listOfData) {
        if (listOfData.size() > 0) {
            try {
                final Dao<MemberBizdir, String> dao = this.getController();
                dao.callBatchTasks(new Callable<Void>() {
                    public Void call() throws SQLException {
                        for (MemberBizdir obj : listOfData) {
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
        App.getDatabaseHelper().clearTable(MemberBizdir.class);
        InterestHelper interestHelper = new InterestHelper();
        interestHelper.clearAll();
    }
}
