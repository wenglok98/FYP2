package com.example.fyp2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.fyp2.BaseApp.BaseActivity;
import com.example.fyp2.R;
import com.example.fyp2.databinding.ActivityEnrollNewSubjectBinding;
import com.example.fyp2.databinding.ActivitySubjectDetailBinding;

public class SubjectDetailActivity extends BaseActivity {
    String subjectName, subjectCode, subjectDescription, subjectType;
    ActivitySubjectDetailBinding activitySubjectDetailBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySubjectDetailBinding = ActivitySubjectDetailBinding.inflate(getLayoutInflater());
        View view = activitySubjectDetailBinding.getRoot();
        setContentView(view);

        initAppTitle();
        initSubData();
    }

    private void initAppTitle() {
        ((TextView) findViewById(R.id.app_title_tv)).setText(R.string.enrollment);
        findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                adddata();
                onBackPressed();
            }
        });
        findViewById(R.id.btn_add_subject).setVisibility(View.INVISIBLE);


    }

    private void initSubData() {
        Intent i = getIntent();
        subjectCode = i.getStringExtra("subjectCode");
        subjectName = i.getStringExtra("subjectName");
        subjectDescription = i.getStringExtra("subjectDescription");
        subjectType = i.getStringExtra("subjectType");

        activitySubjectDetailBinding.subjectCodeTv.setText(subjectCode);
        activitySubjectDetailBinding.subjectNameTv.setText(subjectName);
        activitySubjectDetailBinding.subjectDescriptionTv.setText(subjectDescription);

        activitySubjectDetailBinding.enrollBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

}