package com.example.fyp2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.fyp2.BaseApp.BaseActivity;
import com.example.fyp2.R;
import com.example.fyp2.databinding.ActivityNotesOpenBinding;
import com.example.fyp2.databinding.ActivityProfileBinding;

public class NotesOpen extends BaseActivity {
    ActivityNotesOpenBinding activityNotesOpenBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNotesOpenBinding = ActivityNotesOpenBinding.inflate(getLayoutInflater());
        View view = activityNotesOpenBinding.getRoot();
        setContentView(view);

        String visiontext = getIntent().getStringExtra("notes");

        activityNotesOpenBinding.notesEdt.setText(visiontext);
    }
}