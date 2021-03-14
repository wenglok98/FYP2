package com.example.fyp2.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fyp2.Class.SubjectClassModel;
import com.example.fyp2.Repositories.SubjectListRepositories;

import java.util.ArrayList;
import java.util.List;

public class EnrollSubjectViewModel extends ViewModel {

    private MutableLiveData<ArrayList<SubjectClassModel>> mSubjectList;
    private SubjectListRepositories subjectListRepositories;

    public void init() {
        if (mSubjectList != null) {
            return;
        }
        subjectListRepositories = SubjectListRepositories.getInstance();

        mSubjectList = subjectListRepositories.getSubjectList();


    }

    public LiveData<ArrayList<SubjectClassModel>> getSubjectList() {
        return mSubjectList;
    }
}
