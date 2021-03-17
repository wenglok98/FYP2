package com.example.fyp2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.fyp2.BaseApp.BaseActivity;
import com.example.fyp2.Class.EnrollClass;
import com.example.fyp2.Class.UsersClass;
import com.example.fyp2.R;
import com.example.fyp2.StudentEnrolledAdapter;
import com.example.fyp2.Utils.SharedPreferenceUtil;
import com.example.fyp2.Utils.SnackUtil;
import com.example.fyp2.databinding.ActivityEnrollNewSubjectBinding;
import com.example.fyp2.databinding.ActivitySubjectDetailBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class SubjectDetailActivity extends BaseActivity {
    String subjectName, subjectCode, subjectDescription, subjectType;
    ActivitySubjectDetailBinding activitySubjectDetailBinding;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String UID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySubjectDetailBinding = ActivitySubjectDetailBinding.inflate(getLayoutInflater());
        View view = activitySubjectDetailBinding.getRoot();
        setContentView(view);
        fAuth = FirebaseAuth.getInstance();
        UID = fAuth.getCurrentUser().getUid();
        initButtonValidation();
        initAppTitle();
        initSubData();
        initStudentEnrolled();
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
                String name = SharedPreferenceUtil.getFromPrefs(getApplicationContext(), "username", "");
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = dateFormat.format(date);
                DocumentReference enrolltofirebase = fStore.collection("Enrollment").document();
                EnrollClass enrollClass = new EnrollClass();
                enrollClass.setStudentID(UID);
                enrollClass.setStudentName(name);
                enrollClass.setStudyMinutes("0");
                enrollClass.setSubjectCode(i.getStringExtra("subjectCode"));
                enrollClass.setTimeStamp(strDate);
                enrollClass.setSubjectType(i.getStringExtra("subjectType"));
                enrolltofirebase.set(enrollClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        SnackUtil.show(getApplication(), "Enrolled Successfully");
                        onBackPressed();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        SnackUtil.show(getApplication(), "Enrolled Failed");
                    }
                });

            }
        });

    }

    private void initButtonValidation() {
        Intent i = getIntent();

        Task<QuerySnapshot> documentReference = fStore.collection("Enrollment").whereEqualTo("subjectCode", i.getStringExtra("subjectCode")).whereEqualTo("studentID", UID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if (!queryDocumentSnapshots.getDocuments().isEmpty()) {
                    activitySubjectDetailBinding.enrollBt.setText("Enrolled");
                    activitySubjectDetailBinding.enrollBt.setEnabled(false);
                }

            }
        });


    }

    private void initStudentEnrolled() {
        Intent i = getIntent();
        ArrayList<String> studentIDArrayList = new ArrayList<String>();
        Task<QuerySnapshot> documentReference = fStore.collection("Enrollment")
                .whereEqualTo("subjectCode", i.getStringExtra("subjectCode"))
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            studentIDArrayList.add(documentSnapshot.getString("studentID"));

                        }

                        getStudnetInformation(studentIDArrayList);


                    }
                });


    }

    private void getStudnetInformation(ArrayList<String> arrayList) {
        ArrayList<UsersClass> usersClassArrayList = new ArrayList<>();

        StudentEnrolledAdapter studentEnrolledAdapter = new StudentEnrolledAdapter(getApplication(),usersClassArrayList);
        activitySubjectDetailBinding.peopleRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        activitySubjectDetailBinding.peopleRecycler.setAdapter(studentEnrolledAdapter);
        ArrayList<String> studentIDArrayList = arrayList;
        for (int i = 0; i < studentIDArrayList.size(); i++) {
            Task<DocumentSnapshot> documentReference = fStore.collection("Users").document(studentIDArrayList.get(i))
                    .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            UsersClass tempUser = new UsersClass();
                            tempUser.setUserAddress(documentSnapshot.getString("userAddress"));
                            tempUser.setUserCgpa(documentSnapshot.getString("userCgpa"));
                            tempUser.setUseremail(documentSnapshot.getString("useremail"));
//                            tempUser.setUserGender(documentSnapshot.getString(""));
                            tempUser.setUserGpa(documentSnapshot.getString("userGpa"));
                            tempUser.setUserid(documentSnapshot.getString("userid"));
                            tempUser.setUsername(documentSnapshot.getString("username"));
                            tempUser.setUserPhone(documentSnapshot.getString("userPhone"));

                            usersClassArrayList.add(tempUser);

                            studentEnrolledAdapter.notifyDataSetChanged();


                        }
                    });
        }



    }

}