package com.dangerousthings.nfc.databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.dangerousthings.nfc.interfaces.IImplantDAO;
import com.dangerousthings.nfc.models.Implant;
import com.dangerousthings.nfc.utilities.Converters;

@Database(entities = Implant.class, exportSchema = false, version = 1)
@TypeConverters({Converters.class})
public abstract class ImplantDatabase extends RoomDatabase
{
    private static final String DB_Name = "implant_db";
    private static ImplantDatabase instance;

    public abstract IImplantDAO implantDAO();

    public static synchronized ImplantDatabase getInstance(Context context)
    {
        if(instance == null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(), ImplantDatabase.class, DB_Name)
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
