package com.example.fyp2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.fyp2.Class.NotesClass;
import com.example.fyp2.R;
import com.example.fyp2.Utils.SnackUtil;
import com.example.fyp2.databinding.ActivityEmptyNotesCreateBinding;
import com.example.fyp2.databinding.ActivityReadNotesBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class ReadNotesActivity extends AppCompatActivity {
    ActivityReadNotesBinding activityReadNotesBinding;
    String title, notes, timeStamp, UID, firebaseid;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String newTitle, newNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityReadNotesBinding = ActivityReadNotesBinding.inflate(getLayoutInflater());
        View view = activityReadNotesBinding.getRoot();
        setContentView(view);

        try {
            Intent intent = getIntent();
            title = intent.getStringExtra("title");
            notes = intent.getStringExtra("notes");
            timeStamp = intent.getStringExtra("timesStamp");
            UID = intent.getStringExtra("UID");
            firebaseid = intent.getStringExtra("firebaseid");

        } catch (Exception e) {
            e.printStackTrace();
        }
        initAppTitle();
        activityReadNotesBinding.createNoteEdt.setText(notes);
        activityReadNotesBinding.createNoteTitle.setText(title);
    }

    @Override
    public void onBackPressed() {


        initFirebase();

        super.onBackPressed();
    }
    private void initAppTitle() {
        ((TextView) findViewById(R.id.app_title_tv)).setText("Read Notes");
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

    private void initFirebase() {
        DocumentReference enrolltofirebase = fStore.collection("Notes").document(firebaseid);
        NotesClass notesClass = new NotesClass();
        notesClass.setUID(UID);
        notesClass.setTimeStamp(timeStamp);
        notesClass.setTitle(activityReadNotesBinding.createNoteTitle.getText().toString());
        notesClass.setNotes(activityReadNotesBinding.createNoteEdt.getText().toString());
        notesClass.setFirebase_id(firebaseid);
        enrolltofirebase.set(notesClass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                SnackUtil.show(getApplication(),"Saved");
            }
        });

    }
}