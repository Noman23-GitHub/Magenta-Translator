package ru.noman23.magentatranslator.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.noman23.magentatranslator.database.tables.TranslateRecord;
import ru.noman23.magentatranslator.database.tables.TranslatesDao;

@Database(entities = {TranslateRecord.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TranslatesDao translatesDao();
}
