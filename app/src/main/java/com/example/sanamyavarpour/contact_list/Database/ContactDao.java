package com.example.sanamyavarpour.contact_list.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.sanamyavarpour.contact_list.Model.Contact;

import java.util.List;
@Dao
public interface ContactDao {


    @Insert
    public void insert(Contact contacts);

    @Delete
    public void delete(Contact contact);

    @Update
    public void update(Contact... contactDbs);

    @Query("SELECT * FROM contact")
    public List<Contact> getContacts();

     @Query("DELETE FROM contact")
     public void nukeTable();


}
