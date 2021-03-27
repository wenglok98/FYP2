package com.example.fyp2.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fyp2.Activity.EnrollNewSubject;
import com.example.fyp2.Activity.studentEnroll;
import com.example.fyp2.Class.EnrollClass;
import com.example.fyp2.EnrolledSubjectAdapter;
import com.example.fyp2.ForecastResultAdapter;
import com.example.fyp2.R;
import com.example.fyp2.SubjectListAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class ForecastFragment extends Fragment {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String UID;
    ArrayList<EnrollClass> enrollClassArrayList = new ArrayList<>();
    ForecastResultAdapter forecastResultAdapter;
    RecyclerView recyclerView;

    public ForecastFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_forecast, container, false);
        recyclerView = view.findViewById(R.id.forecast_recycler);
        fAuth = FirebaseAuth.getInstance();
        UID = fAuth.getCurrentUser().getUid();
        initData();
        return view;
    }


    private void initData() {
        forecastResultAdapter = new ForecastResultAdapter(getContext(), enrollClassArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(forecastResultAdapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));


        Task<QuerySnapshot> docRef = fStore.collection("Enrollment").whereEqualTo("studentID", UID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    EnrollClass enrollClass = new EnrollClass();
                    enrollClass.setTimeStamp(documentSnapshot.get("timeStamp").toString());
                    enrollClass.setSubjectCode(documentSnapshot.get("subjectCode").toString());
                    enrollClass.setStudyMinutes(documentSnapshot.get("studyMinutes").toString());
                    enrollClass.setStudentID(documentSnapshot.get("studentID").toString());
                    enrollClass.setSubjectType(documentSnapshot.get("subjectType").toString());
                    try {
                        enrollClass.setSubjectType(documentSnapshot.get("subjectName").toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    enrollClassArrayList.add(enrollClass);
                    forecastResultAdapter.notifyDataSetChanged();

                }

            }
        });


    }
}