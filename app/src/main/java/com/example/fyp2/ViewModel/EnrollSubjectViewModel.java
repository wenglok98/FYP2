package com.example.fyp2.ViewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.fyp2.Class.SubjectClassModel;

import java.util.ArrayList;
import java.util.List;

public class EnrollSubjectViewModel extends ViewModel {

    private MutableLiveData<ArrayList<SubjectClassModel>> mSubjectList = new MutableLiveData<>();

    public void init(){

    }

    public LiveData<ArrayList<SubjectClassModel>> getSubjectList(){
        return  mSubjectList;
    }
}
