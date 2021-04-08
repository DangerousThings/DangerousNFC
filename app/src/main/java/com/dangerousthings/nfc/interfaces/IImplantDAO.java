package com.dangerousthings.nfc.interfaces;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.dangerousthings.nfc.models.Implant;

import java.util.List;

@Dao
public interface IImplantDAO
{
    @Query("SELECT * FROM implant")
    List<Implant> getImplantList();

    @Query("SELECT * FROM implant WHERE UID LIKE :search LIMIT 1")
    Implant getImplantByUID(String search);

    @Insert
    void insertImplant(Implant implant);

    @Update
    void updateImplant(Implant implant);

    @Delete
    void deleteImplant(Implant implant);
}
