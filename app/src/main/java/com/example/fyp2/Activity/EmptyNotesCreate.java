package com.example.fyp2.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.fyp2.Class.EnrollClass;
import com.example.fyp2.Class.NotesClass;
import com.example.fyp2.R;
import com.example.fyp2.Utils.SharedPreferenceUtil;
import com.example.fyp2.Utils.SnackUtil;
import com.example.fyp2.databinding.ActivityAddReminderBinding;
import com.example.fyp2.databinding.ActivityEmptyNotesCreateBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EmptyNotesCreate extends AppCompatActivity {
    ActivityEmptyNotesCreateBinding activityMainSubjectBinding;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityMainSubjectBinding = ActivityEmptyNotesCreateBinding.inflate(getLayoutInflater());
        View view = activityMainSubjectBinding.getRoot();
        setContentView(view);
//      view  setContentView(R.layout.activity_empty_notes_create);

        initAppTitle();


    }


    @Override
    public void onBackPressed() {
        if (!activityMainSubjectBinding.createNoteTitle.getText().toString().equals(""))
        {
            savetofirebase();

        }
        super.onBackPressed();

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
        tempNotes.setTitle(activityMainSubjectBinding.createNoteTitle.getText().toString());
        tempNotes.setNotes(activityMainSubjectBinding.createNoteEdt.getText().toString());
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