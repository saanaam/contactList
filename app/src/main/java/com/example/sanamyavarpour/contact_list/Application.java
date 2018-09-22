package com.example.sanamyavarpour.contact_list;

import android.arch.persistence.room.Room;

import com.example.sanamyavarpour.contact_list.database.AppDataBase;

public class Application extends android.app.Application {

    private static AppDataBase mDataBase;

    @Override
    public void onCreate() {

        super.onCreate();
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
