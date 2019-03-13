package com.example.second_generation_house.DataBase;



import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;
@Dao
public interface MyDao {

    @Insert
    public void addUser(User user);

    @Query("SELECT * FROM user")
    List<User> getAll();


    @Delete
    void delete(User user);
}
