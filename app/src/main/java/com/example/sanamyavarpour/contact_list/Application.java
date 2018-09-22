package com.example.sanamyavarpour.contact_list;

import android.arch.persistence.room.Room;

import com.example.sanamyavarpour.contact_list.database.AppDataBase;

public class Application extends android.app.Application {

    private static AppDataBase mDataBase;

    @Override
    public void onCreate() {
        super.onCreate();
        //CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
        //.setDefaultFontPath("IRANSansMobile(FaNum).ttf")
        //.setFontAttrId(R.attr.fontPath).build());
        //mDaoSession = new DaoMaster(new DaoMaster.DevOpenHelper(this, "database.db").getWritableDb()).newSession();
        mDataBase = Room.databaseBuilder(this, AppDataBase.class, "contact")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }

    public static AppDataBase getDB() {
        return mDataBase;
    }
}
