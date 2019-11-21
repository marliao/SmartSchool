package com.marliao.smartschool.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.marliao.smartschool.AppClient;
import com.marliao.smartschool.bean.Club;
import com.marliao.smartschool.bean.Complaint;
import com.marliao.smartschool.bean.Grade;
import com.marliao.smartschool.bean.Market;
import com.marliao.smartschool.bean.News;
import com.marliao.smartschool.bean.Notice;
import com.marliao.smartschool.bean.Recipe;
import com.marliao.smartschool.bean.User;

public class MyHelper extends OrmLiteSqliteOpenHelper {

    private static MyHelper myHelper;

    public MyHelper(Context context) {
        super(context, "smartschool", null, 2);
    }

    public static MyHelper getInstance() {
        if (myHelper == null) {
            synchronized (MyHelper.class) {
                if (myHelper == null) {
                    myHelper = new MyHelper(AppClient.mContext);
                }
            }
        }
        return myHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Notice.class);
            TableUtils.createTableIfNotExists(connectionSource, News.class);
            TableUtils.createTableIfNotExists(connectionSource, Complaint.class);
            TableUtils.createTableIfNotExists(connectionSource, Grade.class);
            TableUtils.createTableIfNotExists(connectionSource, Club.class);
            TableUtils.createTableIfNotExists(connectionSource, Market.class);
            TableUtils.createTableIfNotExists(connectionSource, Recipe.class);
            TableUtils.createTableIfNotExists(connectionSource, User.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {

    }
}
