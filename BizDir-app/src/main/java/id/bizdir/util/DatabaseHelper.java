package id.bizdir.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;
import java.sql.SQLException;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import id.bizdir.model.AdsObject;
import id.bizdir.model.Anggota;
import id.bizdir.model.AnggotaCategory;
import id.bizdir.model.AnggotaGallery;
import id.bizdir.model.AnggotaSubCategory;
import id.bizdir.model.AnggotaSubCategoryAssignment;
import id.bizdir.model.Association;
import id.bizdir.model.City;
import id.bizdir.model.Common;
import id.bizdir.model.DownloadRoot;
import id.bizdir.model.DownloadSub;
import id.bizdir.model.Event;
import id.bizdir.model.EventCategory;
import id.bizdir.model.ForumCategory;
import id.bizdir.model.ForumPost;
import id.bizdir.model.ForumThread;
import id.bizdir.model.Interest;
import id.bizdir.model.MemberBizdir;
import id.bizdir.model.NewsBusiness;
import id.bizdir.model.NewsBusinessCategory;
import id.bizdir.model.NewsKadin;
import id.bizdir.model.NewsStock;
import id.bizdir.model.Opportunity;
import id.bizdir.model.OpportunityCategory;
import id.bizdir.model.Promotion;
import id.bizdir.model.Province;
import id.bizdir.model.TableSynch;
import id.bizdir.model.WalkThrough;

/**
 * Created by Hendry on 02/05/2015.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private DatabaseInitializer initializer;

    public DatabaseHelper(Context context) {
        super(context, Const.DATABASE_NAME, null, Const.DATABASE_VERSION);

        DatabaseInitializer initializer = new DatabaseInitializer(context);
        this.initializer = initializer;
        try {
            initializer.createDatabase();
            initializer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqliteDatabase,
                         ConnectionSource connectionSource) {
        try {
            /*
            TableUtils.createTable(connectionSource, Category.class);
            TableUtils.createTable(connectionSource, CategoryAssigment.class);
            TableUtils.createTable(connectionSource, CategorySub.class);
            TableUtils.createTable(connectionSource, MemberKadin.class);
            TableUtils.createTable(connectionSource, City.class);
            TableUtils.createTable(connectionSource, Common.class);
            TableUtils.createTable(connectionSource, DownloadRoot.class);
            TableUtils.createTable(connectionSource, DownloadSub.class);
            TableUtils.createTable(connectionSource, Province.class);
            TableUtils.createTable(connectionSource, Event.class);
            TableUtils.createTable(connectionSource, EventCategory.class);
            TableUtils.createTable(connectionSource, ForumCategory.class);
            TableUtils.createTable(connectionSource, SyncDate.class);
            TableUtils.createTable(connectionSource, ForumPost.class);
            TableUtils.createTable(connectionSource, ForumThread.class);
            TableUtils.createTable(connectionSource, NewsBusiness.class);
            TableUtils.createTable(connectionSource, NewsBusinessCategory.class);
            TableUtils.createTable(connectionSource, NewsKadin.class);
            TableUtils.createTable(connectionSource, Opportunity.class);
            TableUtils.createTable(connectionSource, OpportunityCategory.class);
            TableUtils.createTable(connectionSource, Promotion.class);
            */
            //SyncModDao syncModController = new SyncModDao();
            //syncModController.addDefaultData(this);

            TableUtils.createTable(connectionSource, Anggota.class);
            TableUtils.createTable(connectionSource, AnggotaCategory.class);
            TableUtils.createTable(connectionSource, AnggotaGallery.class);
            TableUtils.createTable(connectionSource, AnggotaSubCategory.class);
            TableUtils.createTable(connectionSource, AnggotaSubCategoryAssignment.class);
            TableUtils.createTable(connectionSource, City.class);
            TableUtils.createTable(connectionSource, Common.class);
            TableUtils.createTable(connectionSource, DownloadRoot.class);
            TableUtils.createTable(connectionSource, DownloadSub.class);
            TableUtils.createTable(connectionSource, Event.class);
            TableUtils.createTable(connectionSource, EventCategory.class);
            TableUtils.createTable(connectionSource, ForumCategory.class);
            TableUtils.createTable(connectionSource, ForumPost.class);
            TableUtils.createTable(connectionSource, ForumThread.class);
            TableUtils.createTable(connectionSource, NewsBusiness.class);
            TableUtils.createTable(connectionSource, NewsBusinessCategory.class);
            TableUtils.createTable(connectionSource, NewsKadin.class);
            TableUtils.createTable(connectionSource, NewsStock.class);
            TableUtils.createTable(connectionSource, Opportunity.class);
            TableUtils.createTable(connectionSource, OpportunityCategory.class);
            TableUtils.createTable(connectionSource, Promotion.class);
            TableUtils.createTable(connectionSource, Province.class);
            TableUtils.createTable(connectionSource, TableSynch.class);
            //TableUtils.createTable(connectionSource, Weather.class);
            TableUtils.createTable(connectionSource, Interest.class);
            TableUtils.createTable(connectionSource, MemberBizdir.class);
            TableUtils.createTable(connectionSource, AdsObject.class);
            TableUtils.createTable(connectionSource, WalkThrough.class);
            //TableUtils.createTable(connectionSource, Newsticker.class);
            TableUtils.createTable(connectionSource, Association.class);
            //DatabaseInitializers databaseInitializers = new DatabaseInitializers();
            //databaseInitializers.AddAllData();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqliteDatabase,
                          ConnectionSource connectionSource, int oldVer, int newVer) {
        /*
        try {
            initializer.deleteAndCreateDatabase();
            initializer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        try {
            /*
            TableUtils.dropTable(connectionSource, Category.class, true);
            TableUtils.dropTable(connectionSource, CategoryAssigment.class, true);
            TableUtils.dropTable(connectionSource, CategorySub.class, true);
            TableUtils.dropTable(connectionSource, MemberKadin.class, true);
            TableUtils.dropTable(connectionSource, City.class, true);
            TableUtils.dropTable(connectionSource, Common.class, true);
            TableUtils.dropTable(connectionSource, DownloadRoot.class, true);
            TableUtils.dropTable(connectionSource, DownloadSub.class, true);
            TableUtils.dropTable(connectionSource, Province.class, true);
            TableUtils.dropTable(connectionSource, Event.class, true);
            TableUtils.dropTable(connectionSource, EventCategory.class, true);
            TableUtils.dropTable(connectionSource, ForumCategory.class, true);
            TableUtils.dropTable(connectionSource, SyncDate.class, true);
            TableUtils.dropTable(connectionSource, ForumPost.class, true);
            TableUtils.dropTable(connectionSource, ForumThread.class, true);
            TableUtils.dropTable(connectionSource, NewsBusiness.class, true);
            TableUtils.dropTable(connectionSource, NewsBusinessCategory.class, true);
            TableUtils.dropTable(connectionSource, NewsKadin.class, true);
            TableUtils.dropTable(connectionSource, Opportunity.class, true);
            TableUtils.dropTable(connectionSource, OpportunityCategory.class, true);
            TableUtils.dropTable(connectionSource, Promotion.class, true);
            */
            TableUtils.dropTable(connectionSource, Anggota.class, true);
            TableUtils.dropTable(connectionSource, AnggotaCategory.class, true);
            TableUtils.dropTable(connectionSource, AnggotaGallery.class, true);
            TableUtils.dropTable(connectionSource, AnggotaSubCategory.class, true);
            TableUtils.dropTable(connectionSource, AnggotaSubCategoryAssignment.class, true);
            TableUtils.dropTable(connectionSource, City.class, true);
            TableUtils.dropTable(connectionSource, Common.class, true);
            TableUtils.dropTable(connectionSource, DownloadRoot.class, true);
            TableUtils.dropTable(connectionSource, DownloadSub.class, true);
            TableUtils.dropTable(connectionSource, Event.class, true);
            TableUtils.dropTable(connectionSource, EventCategory.class, true);
            TableUtils.dropTable(connectionSource, ForumCategory.class, true);
            TableUtils.dropTable(connectionSource, ForumPost.class, true);
            TableUtils.dropTable(connectionSource, ForumThread.class, true);
            TableUtils.dropTable(connectionSource, NewsBusiness.class, true);
            TableUtils.dropTable(connectionSource, NewsBusinessCategory.class, true);
            TableUtils.dropTable(connectionSource, NewsKadin.class, true);
            TableUtils.dropTable(connectionSource, NewsStock.class, true);
            TableUtils.dropTable(connectionSource, Opportunity.class, true);
            TableUtils.dropTable(connectionSource, OpportunityCategory.class, true);
            TableUtils.dropTable(connectionSource, Promotion.class, true);
            TableUtils.dropTable(connectionSource, Province.class, true);
            TableUtils.dropTable(connectionSource, TableSynch.class, true);
            //TableUtils.dropTable(connectionSource, Weather.class, true);
            TableUtils.dropTable(connectionSource, Interest.class, true);
            TableUtils.dropTable(connectionSource, MemberBizdir.class, true);
            TableUtils.dropTable(connectionSource, AdsObject.class, true);
            TableUtils.dropTable(connectionSource, WalkThrough.class, true);
            //TableUtils.dropTable(connectionSource, Newsticker.class, true);
            TableUtils.dropTable(connectionSource, Association.class, true);
            onCreate(sqliteDatabase, connectionSource);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearTable(Class<?> dataClass) {
        ConnectionSource connectionSource = getConnectionSource();
        try {
            TableUtils.dropTable(connectionSource, dataClass, true);
            TableUtils.createTable(connectionSource, dataClass);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}