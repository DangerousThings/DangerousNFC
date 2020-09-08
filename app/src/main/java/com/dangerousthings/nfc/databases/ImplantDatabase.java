package com.dangerousthings.nfc.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.dangerousthings.nfc.interfaces.ImplantDao;
import com.dangerousthings.nfc.models.Implant;

@Database(entities = Implant.class, exportSchema = false, version = 1)
public abstract class ImplantDatabase extends RoomDatabase
{
    private static final String DB_NAME = "implant_db";
    private static ImplantDatabase instance;

    public static synchronized ImplantDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), ImplantDatabase.class, DB_NAME)
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build();
        }
        return instance;
    }

    public abstract ImplantDao implantDao();
}
