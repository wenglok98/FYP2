package com.example.fyp2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.fyp2.BaseApp.AppManager;
import com.example.fyp2.Class.NotesClass;
import com.example.fyp2.NotesListActivity;
import com.example.fyp2.R;
import com.example.fyp2.Utils.SharedPreferenceUtil;
import com.example.fyp2.Utils.SnackUtil;
import com.example.fyp2.databinding.ActivityEmptyNotesCreate2Binding;
import com.example.fyp2.databinding.ActivityEmptyNotesCreateBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EmptyNotesCreate2 extends AppCompatActivity {
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    ActivityEmptyNotesCreate2Binding activityEmptyNotesCreate2Binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEmptyNotesCreate2Binding = ActivityEmptyNotesCreate2Binding.inflate(getLayoutInflater());
        View view = activityEmptyNotesCreate2Binding.getRoot();
        setContentView(view);
        initAppTitle();
        String visiontext = getIntent().getStringExtra("notes");

        activityEmptyNotesCreate2Binding.createNoteEdt.setText(visiontext);
    }


    @Override
    public void onBackPressed() {
        if (!activityEmptyNotesCreate2Binding.createNoteTitle.getText().toString().equals("")) {
            savetofirebase();

        }
//        super.onBackPressed();
        AppManager.getAppManager().ToOtherActivity(NotesListActivity.class);
        finish();

    }

    private void initAppTitle() {
        ((TextView) findViewById(R.id.app_title_tv)).setText("Create Notes");
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

    private void savetofirebase() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strDate = dateFormat.format(date);
        DocumentReference enrolltofirebase = fStore.collection("Notes").document();
        NotesClass tempNotes = new NotesClass();
        String UID = SharedPreferenceUtil.getFromPrefs(getApplicationContext(), "UID", "");
        tempNotes.setUID(UID);
        tempNotes.setTimeStamp(strDate);
        tempNotes.setTitle(activityEmptyNotesCreate2Binding.createNoteTitle.getText().toString());
        tempNotes.setNotes(activityEmptyNotesCreate2Binding.createNoteEdt.getText().toString());
        tempNotes.setFirebase_id(enrolltofirebase.getId());

        enrolltofirebase.set(tempNotes).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                SnackUtil.show(getApplication(), "Saved");

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                SnackUtil.show(getApplication(), "Failed");
            }
        });
    }
}