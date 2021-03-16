package com.example.fyp2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.fyp2.BaseApp.AppManager;
import com.example.fyp2.BaseApp.BaseActivity;
import com.example.fyp2.R;

public class studentEnroll extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_enroll);
        initAppTitle();


    }

    private void initAppTitle() {
        ((TextView) findViewById(R.id.app_title_tv)).setText("Subjects");
        findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                adddata();
                onBackPressed();
            }
        });
        findViewById(R.id.btn_add_subject).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_add_subject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getAppManager().ToOtherActivity(EnrollNewSubject.class);
            }
        });


    }
}