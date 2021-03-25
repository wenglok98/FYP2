package com.example.fyp2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp2.BaseApp.BaseActivity;
import com.example.fyp2.Class.LangString;
import com.example.fyp2.LangUtils;
import com.example.fyp2.R;
import com.example.fyp2.Utils.SnackUtil;
import com.example.fyp2.databinding.ActivityProfileBinding;
import com.example.fyp2.databinding.ActivitySettingBinding;

import java.util.ArrayList;

import io.realm.Realm;

public class SettingActivity extends BaseActivity {
    ArrayList<String> language = new ArrayList<>();
    ArrayList<String> languageCode = new ArrayList<>();
    ActivitySettingBinding activitySettingBinding;
    Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySettingBinding = ActivitySettingBinding.inflate(getLayoutInflater());
        View view = activitySettingBinding.getRoot();
        setContentView(view);

        language.add("English");
        language.add("Chinese");
        languageCode.add("en");
        languageCode.add("zh");
        initAppTitle();

        initLang();

        ArrayAdapter<String> test = new ArrayAdapter<>(getApplication(), R.layout.dropdown_menu_popup_item, language);

        activitySettingBinding.locationoutlet.setAdapter(test);

        activitySettingBinding.locationoutlet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showChangeLanguageDialog(position);
//                Toast.makeText(SettingActivity.this, language.get(position).toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void showChangeLanguageDialog(Integer position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.logout));
        builder.setMessage(R.string.changeLanguageTips);
        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LangUtils langUtils;
                langUtils = new LangUtils();
                langUtils.setLocale(languageCode.get(position).toString(), SettingActivity.this);
                dialog.dismiss();
                SnackUtil.show(getApplicationContext(), "Please Restart Apps to get effect");
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    private void initLang() {
        Realm.init(SettingActivity.this);
        realm = Realm.getDefaultInstance();

        LangString langResult = realm.where(LangString.class).findFirst();
        String lang1 = "";
        try {
            lang1 = langResult.getLangS();
            if (lang1.equals("en")) {
                activitySettingBinding.locationoutlet.setText("English");
            } else if (lang1.equals("zh")) {
                activitySettingBinding.locationoutlet.setText("Chinese");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void initAppTitle() {
        ((TextView) findViewById(R.id.app_title_tv)).setText(R.string.setting);
        findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                adddata();
                onBackPressed();
//                Toast.makeText(SubjectDetailActivity.this, String.valueOf(activitySubjectDetailBinding.ratingBar.getRating()), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_add_subject).setVisibility(View.INVISIBLE);


    }
}