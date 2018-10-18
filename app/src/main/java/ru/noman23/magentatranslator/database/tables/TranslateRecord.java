package ru.noman23.magentatranslator.database.tables;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class TranslateRecord {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String text;
    private String lang;
    private int saveType = 1;

    // TODO: Думаю очевидно - нужно убрать и хранить по человечески в нереляционной бд.
    private String json;

    @Ignore
    public TranslateRecord(long id) {
        this.id = id;
    }

    public TranslateRecord(String text, String lang, String json) {
        this.text = text;
        this.lang = lang;
        this.json = json;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public int getSaveType() {
        return saveType;
    }

    public void setSaveType(int saveType) {
        this.saveType = saveType;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
