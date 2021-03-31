package com.example.fyp2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import com.example.fyp2.Class.LangString;

import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class LangUtils {

    Realm realm;

    public void checkLang(Context ct) {
        Realm.init(ct);
        realm = Realm.getDefaultInstance();

        LangString langResult = realm.where(LangString.class).findFirst();
        String lang1 = "";
        try {
            lang1 = langResult.getLangS();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (lang1.equals("")) {
            realm.beginTransaction();
            LangString realmLang = realm.createObject(LangString.class);
            realmLang.setLangS("en");
            realm.commitTransaction();
//            Toast.makeText(this, "KOSONG", Toast.LENGTH_SHORT).show();
        } else if (lang1.equals("en")) {
            setLocale(lang1, ct);
        } else if
        (lang1.equals("zh")) {
            setLocale(lang1, ct);
        }

        setLocale(lang1, ct);
    }

    public void setLocale(String lang, Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Realm.init(context);
                realm = Realm.getDefaultInstance();
                Locale locale = new Locale(lang);
                Resources res = context.getResources();
                DisplayMetrics dm = res.getDisplayMetrics();
                Configuration conf = res.getConfiguration();
                conf.locale = locale;
                res.updateConfiguration(conf, dm);
                try {
                    LangString langset = realm.where(LangString.class).findFirst();

                    Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            langset.setLangS(lang);
                            realm.insertOrUpdate(langset);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }



            }
        }).start();

    }


}
