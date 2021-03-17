package com.example.fyp2.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.fyp2.BaseApp.AppManager;
import com.example.fyp2.BaseApp.BaseActivity;
import com.example.fyp2.Class.EnrollClass;
import com.example.fyp2.Class.SubjectClassModel;
import com.example.fyp2.EnrolledSubjectAdapter;
import com.example.fyp2.R;
import com.example.fyp2.SubjectListAdapter;
import com.example.fyp2.databinding.ActivityEnrollNewSubjectBinding;
import com.example.fyp2.databinding.ActivityStudentEnrollBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class studentEnroll extends BaseActivity {
    private ArrayList<EnrollClass> dataSet = new ArrayList<>();
    EnrolledSubjectAdapter adapter;
    ActivityStudentEnrollBinding activityEnrollNewSubjectBinding;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEnrollNewSubjectBinding = ActivityStudentEnrollBinding.inflate(getLayoutInflater());
        View view = activityEnrollNewSubjectBinding.getRoot();
        setContentView(view);

        fAuth = FirebaseAuth.getInstance();
        UID = fAuth.getCurrentUser().getUid();
//        setContentView(R.layout.activity_student_enroll);
        initAppTitle();
        initData();


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

    private void initData() {
        adapter = new EnrolledSubjectAdapter(studentEnroll.this, dataSet);
        activityEnrollNewSubjectBinding.enrolledSubjectRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        activityEnrollNewSubjectBinding.enrolledSubjectRecycler.setAdapter(adapter);

        Task<QuerySnapshot> docRef = fStore.collection("Enrollment").whereEqualTo("studentID", UID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots)
                {
                    EnrollClass enrollClass = new EnrollClass();
                    enrollClass.setTimeStamp(documentSnapshot.get("timeStamp").toString());
                    enrollClass.setSubjectCode(documentSnapshot.get("subjectCode").toString());
                    enrollClass.setStudyMinutes(documentSnapshot.get("studyMinutes").toString());
                    enrollClass.setStudentID(documentSnapshot.get("studentID").toString());
                    enrollClass.setSubjectType(documentSnapshot.get("subjectType").toString());
                    dataSet.add(enrollClass);
                }
                adapter.notifyDataSetChanged();
            }
        });

    }
}