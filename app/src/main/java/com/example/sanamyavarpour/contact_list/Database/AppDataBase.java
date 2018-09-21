package com.example.sanamyavarpour.contact_list.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.sanamyavarpour.contact_list.Model.Contact;

@Database(entities = {Contact.class}, version = 1)

public abstract class AppDataBase extends RoomDatabase {

    public abstract ContactDao getContactDAO();

}
