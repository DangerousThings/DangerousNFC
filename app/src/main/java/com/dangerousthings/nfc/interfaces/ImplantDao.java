package com.dangerousthings.nfc.interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dangerousthings.nfc.models.Implant;

import java.util.List;

@Dao
public interface ImplantDao
{
    @Query("Select * from implant")
    List<Implant> getImplantList();

    @Query("SELECT * FROM implant WHERE implantUid LIKE :search")
    List<Implant> findImplantByUid(String search);

    @Insert
    void insertImplant(Implant implant);

    @Update
    void updateImplant(Implant implant);

    @Delete
    void deleteImplant(Implant implant);
}
