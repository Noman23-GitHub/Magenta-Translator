package ru.noman23.magentatranslator.network.translate.sup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Tr_ {

    @SerializedName("text")
    @Expose
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
