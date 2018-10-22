package ru.noman23.magentatranslator.database.tables;

import android.arch.persistence.room.RoomDatabase;

import com.google.gson.Gson;

import java.util.LinkedList;
import java.util.List;

import ru.noman23.magentatranslator.network.translate.TranslateEntity;

public class TranslatesDaoWrapper extends TranslatesDao_Impl {

    private Gson gson;

    public TranslatesDaoWrapper(RoomDatabase __db, Gson gson) {
        super(__db);
        this.gson = gson;
    }

    public long insert(TranslateEntity translate) {
        return super.insert(new TranslateRecord(translate.getText(), translate.getLang(), gson.toJson(translate)));
    }

    public List<Long> insertAll(TranslateEntity... translates) {
        TranslateRecord records[] = new TranslateRecord[translates.length];
        for (int i = 0; i < translates.length; i++) {
            records[i] = new TranslateRecord(translates[i].getText(), translates[i].getLang(), gson.toJson(translates[i]));
        }
        return super.insertAll(records);
    }

    public void delete(TranslateEntity translate) {
        super.delete(new TranslateRecord(translate.getId()));
    }

    public TranslateEntity findEntity(String text, String lang) {
        return convertRecord(super.findRecord(text, lang));
    }

    public TranslateEntity findEntityById(long id) {
        return convertRecord(super.findRecordById(id));
    }

    public List<TranslateEntity> getAllEnts() {
        return convertLists(super.getAll());
    }

    public List<TranslateEntity> getHistoryEnts() {
        return convertLists(super.getHistory());
    }

    public List<TranslateEntity> getSavedEnts() {
        return convertLists(super.getSaved());
    }

    private TranslateEntity convertRecord(TranslateRecord record) {
        if (record == null) return null;
        TranslateEntity entity = gson.fromJson(record.getJson(), TranslateEntity.class);
        entity.setId(record.getId());
        entity.setText(record.getText());
        entity.setLang(record.getLang());
        entity.setSaveType(record.getSaveType());
        return entity;
    }

    private List<TranslateEntity> convertLists(List<TranslateRecord> records) {
        return new LinkedList<TranslateEntity>() {{
            for (TranslateRecord record : records) {
                add(convertRecord(record));
            }
        }};
    }
}
