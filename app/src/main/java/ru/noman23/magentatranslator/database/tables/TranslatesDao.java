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

    @Query("SELECT * FROM TranslateRecord AS tr WHERE tr.saveType & " + TYPE_HISTORY + "=" + TYPE_HISTORY + " ORDER BY id DESC")
    List<TranslateRecord> getHistory();

    @Query("SELECT * FROM TranslateRecord AS tr WHERE tr.saveType & " + TYPE_SAVED + " = " + TYPE_SAVED + " ORDER BY id DESC")
    List<TranslateRecord> getSaved();

    @Query("SELECT * FROM TranslateRecord AS tr WHERE tr.text = :text and tr.lang = :lang")
    TranslateRecord findRecord(String text, String lang);

    @Query("SELECT * FROM TranslateRecord AS tr WHERE tr.id = :id")
    TranslateRecord findRecordById(long id);

    @Query("UPDATE TranslateRecord SET saveType = ((:flag | (SELECT saveType FROM TranslateRecord WHERE id=:id)) - (:flag & (SELECT saveType FROM TranslateRecord WHERE id=:id))) WHERE id=:id")
    void toggleFlag(long id, int flag);

    @Query("UPDATE TranslateRecord SET saveType = :flag | (SELECT saveType FROM TranslateRecord WHERE id=:id) WHERE id=:id")
    void setFlag(long id, int flag);

    @Query("UPDATE TranslateRecord SET saveType = ~:flag & (SELECT saveType FROM TranslateRecord WHERE id=:id) WHERE id=:id")
    void removeFlag(long id, int flag);
}
