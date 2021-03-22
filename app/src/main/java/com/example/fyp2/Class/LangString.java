package com.example.fyp2.Class;

import io.realm.RealmObject;

public class LangString extends RealmObject {
    String langS;

    public String getLangS() {
        return langS;
    }

    public LangString() {
    }

    public void setLangS(String langS) {
        this.langS = langS;
    }

    public LangString(String langS) {
        this.langS = langS;
    }
}
