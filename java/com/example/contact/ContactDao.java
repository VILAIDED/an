package com.example.contact;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface ContactDao {
    @Query("SELECT * FROM Contact")
    public List<Contact> getAllConteact();

    @Query("SELECT * FROM Contact WHERE id = :contactId")
    public List<Contact> getContactById(int contactId);
    @Insert
    public void insertAll(Contact... contacts);
    @Update
    public void updateAll(Contact... contacts);
}
