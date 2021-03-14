package com.example.fyp2.Repositories;

import androidx.lifecycle.MutableLiveData;

import com.example.fyp2.Class.SubjectClassModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class SubjectListRepositories {

    private static SubjectListRepositories instance;
    private ArrayList<SubjectClassModel> dataSet = new ArrayList<>();
    FirebaseAuth fAuth;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    public static SubjectListRepositories getInstance() {
        if (instance == null) {
            instance = new SubjectListRepositories();
        }
        return instance;
    }

    public MutableLiveData<ArrayList<SubjectClassModel>> getSubjectList() {



        MutableLiveData<ArrayList<SubjectClassModel>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;


    }

    private void retrieveFromFirebase() {


    }

    interface callback{

        MutableLiveData<ArrayList<SubjectClassModel>> awakeData();
    }

}
