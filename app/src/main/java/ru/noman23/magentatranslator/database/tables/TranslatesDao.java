package ru.noman23.magentatranslator.database.tables;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TranslatesDao {

    int TYPE_HISTORY = 1;   // запись сохранена в истории
    int TYPE_SAVED = 2;     // запись сохранена в сохраненных

    @Insert
    List<Long> insertAll(TranslateRecord... translates);

    @Insert
    long insert(TranslateRecord translate);

    @Delete
    void delete(TranslateRecord translate);

    @Query("SELECT * FROM TranslateRecord")
    List<TranslateRecord> getAll();

    @Query("SELECT * FROM TranslateRecord AS tr WHERE tr.saveType & " + TYPE_HISTORY)
    List<TranslateRecord> getHistory();

    @Query("SELECT * FROM TranslateRecord AS tr WHERE tr.saveType & " + TYPE_SAVED)
    List<TranslateRecord> getSaved();

    @Query("SELECT * FROM TranslateRecord AS tr WHERE tr.text = :text and tr.lang = :lang")
    TranslateRecord findRecord(String text, String lang);
}
