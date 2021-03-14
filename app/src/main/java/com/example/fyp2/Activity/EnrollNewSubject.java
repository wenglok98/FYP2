package com.example.fyp2.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.fyp2.Class.SubjectClassModel;
import com.example.fyp2.R;
import com.example.fyp2.SubjectListAdapter;
import com.example.fyp2.ViewModel.EnrollSubjectViewModel;
import com.example.fyp2.databinding.ActivityEnrollNewSubjectBinding;
import com.example.fyp2.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cc.solart.wave.WaveSideBarView;

public class EnrollNewSubject extends AppCompatActivity {
    ActivityEnrollNewSubjectBinding activityEnrollNewSubjectBinding;
    EnrollSubjectViewModel enrollSubjectViewModel;
    ArrayList<SubjectClassModel> arrayList = new ArrayList<>();
    SubjectListAdapter adapter;
    private ArrayList<SubjectClassModel> dataSet = new ArrayList<>();

    FirebaseAuth fAuth;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityEnrollNewSubjectBinding = ActivityEnrollNewSubjectBinding.inflate(getLayoutInflater());
        View view = activityEnrollNewSubjectBinding.getRoot();
        setContentView(view);
        ViewModelProvider.Factory factory = new ViewModelProvider.NewInstanceFactory();

        fAuth = FirebaseAuth.getInstance();
        UID = fAuth.getCurrentUser().getUid();
        initAppTitle();

//
//        enrollSubjectViewModel = new ViewModelProvider(EnrollNewSubject.this, factory).get(EnrollSubjectViewModel.class);
//
//        enrollSubjectViewModel.init();
//        enrollSubjectViewModel.getSubjectList().observe(this, new Observer<List<SubjectClassModel>>() {
//            @Override
//            public void onChanged(List<SubjectClassModel> subjectClassModels) {
//                adapter.notifyDataSetChanged();
//            }
//        });

        initAdapter();


    }

    private void initAdapter() {

        adapter = new SubjectListAdapter(EnrollNewSubject.this, dataSet);
        activityEnrollNewSubjectBinding.subjectRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        activityEnrollNewSubjectBinding.subjectRecyclerView.setAdapter(adapter);

        Task<QuerySnapshot> documentReference = fStore.collection("Subjects").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    SubjectClassModel tempSub = new SubjectClassModel();
                    tempSub.setSubjectImage(documentSnapshot.get("subjectCode").toString());
                    tempSub.setSubjectName(documentSnapshot.get("subjectName").toString());
                    tempSub.setSubjectCode(documentSnapshot.get("subjectCode").toString());
                    dataSet.add(tempSub);

                }
                dataSet.sort(Comparator.comparing(a -> a.getSubjectName()));
                adapter.notifyDataSetChanged();

            }


        });


        activityEnrollNewSubjectBinding.sideView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = adapter.getLetterPosition(letter);

                if (pos != -1) {
                    activityEnrollNewSubjectBinding.subjectRecyclerView.scrollToPosition(pos);
                }
            }
        });


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


    }

    private void adddata() {

        DocumentReference documentReference = fStore.collection("Subjects").document();
        DocumentReference documentReference2 = fStore.collection("Subjects").document();
        DocumentReference documentReference3 = fStore.collection("Subjects").document();
        DocumentReference documentReference4 = fStore.collection("Subjects").document();
        DocumentReference documentReference5 = fStore.collection("Subjects").document();
        DocumentReference documentReference6 = fStore.collection("Subjects").document();
        DocumentReference documentReference7 = fStore.collection("Subjects").document();
        SubjectClassModel newSub = new SubjectClassModel("asdf", "UCCD1234", "CHINESE");
        SubjectClassModel newSub2 = new SubjectClassModel("asdf", "UCCD1422", "Bahasa Melayu");
        SubjectClassModel newSub3 = new SubjectClassModel("asdf", "UCCD1085", "Maths");
        SubjectClassModel newSub4 = new SubjectClassModel("asdf", "UCCD17232", "Science");
        SubjectClassModel newSub5 = new SubjectClassModel("asdf", "UCCD168934", "Physics");
        SubjectClassModel newSub6 = new SubjectClassModel("asdf", "UCCD159834", "Chemist");
        SubjectClassModel newSub7 = new SubjectClassModel("asdf", "UCCD177334", "Biology");
        documentReference.set(newSub);
        documentReference2.set(newSub2);
        documentReference3.set(newSub3);
        documentReference4.set(newSub4);
        documentReference5.set(newSub5);
        documentReference6.set(newSub6);
        documentReference7.set(newSub7);


    }

}