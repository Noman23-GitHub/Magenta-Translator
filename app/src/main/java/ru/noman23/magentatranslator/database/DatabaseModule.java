package ru.noman23.magentatranslator.database;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.google.gson.Gson;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import ru.noman23.magentatranslator.database.tables.TranslatesDaoWrapper;
import ru.noman23.magentatranslator.utils.UtilsModule;

@Module(includes = {UtilsModule.class})
public class DatabaseModule {

    private static final String DATABASE_NAME = "test_database";

    @Provides
    @Singleton
    AppDatabase providesDataBase(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DATABASE_NAME).build();
    }

    @Provides
    @Singleton
    TranslatesDaoWrapper providesTranslatesDaoWrapper(AppDatabase database, Gson gson) {
        return new TranslatesDaoWrapper(database, gson);
    }
}
