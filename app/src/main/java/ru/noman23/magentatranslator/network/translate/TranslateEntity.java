package ru.noman23.magentatranslator.network.translate;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.noman23.magentatranslator.network.translate.sup.Def;
import ru.noman23.magentatranslator.network.translate.sup.Head;

public class TranslateEntity {

    @SerializedName("head")
    @Expose
    private Head head;
    @SerializedName("def")
    @Expose
    private List<Def> def = null;

    private long id;
    private int saveType;
    private String text;
    private String lang;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getSaveType() {
        return saveType;
    }

    public void setSaveType(int saveType) {
        this.saveType = saveType;
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

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public List<Def> getDef() {
        return def;
    }

    public void setDef(List<Def> def) {
        this.def = def;
    }
}
