package com.example.fyp2.Fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.coorchice.library.SuperTextView;
import com.coorchice.library.gifdecoder.GifDrawable;
import com.coorchice.library.utils.STVUtils;
import com.coorchice.library.utils.ThreadPool;
import com.example.fyp2.Activity.AddReminder;
import com.example.fyp2.Activity.MainSubjectActivity;
import com.example.fyp2.Activity.NotesActivity;
import com.example.fyp2.Activity.studentEnroll;
import com.example.fyp2.BaseApp.AppManager;
import com.example.fyp2.Class.EnrollClass;
import com.example.fyp2.EnrolledSubjectAdapter;
import com.example.fyp2.NotesListActivity;
import com.example.fyp2.R;
import com.example.fyp2.Utils.SharedPreferenceUtil;
import com.fujiyuu75.sequent.Sequent;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private SuperTextView stv_4;
    private SuperTextView notebt;
    private SuperTextView reminder;
    private LinearLayout ll;
    PieChart pieChart;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    FirebaseAuth fAuth;
    ArrayList<EnrollClass> arrayList = new ArrayList<>();
    String UID;
    ArrayList<PieEntry> subjectdata = new ArrayList<>();

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_home, container, false);

        stv_4 = (SuperTextView) view.findViewById(R.id.stv_4);
        ll = view.findViewById(R.id.homell);
        notebt = (SuperTextView) view.findViewById(R.id.notesbt);
        reminder = (SuperTextView) view.findViewById(R.id.reminder);
        pieChart = view.findViewById(R.id.piechart);
        initData();


        reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getAppManager().ToOtherActivity(AddReminder.class);

            }
        });
        stv_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getAppManager().ToOtherActivity(MainSubjectActivity.class);
            }
        });
        notebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        AppManager.getAppManager().ToOtherActivity(NotesListActivity.class);
                    }
                }).start();
            }
        });


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                byte[] resBytes = STVUtils.getResBytes(getContext(), R.drawable.book);
                final GifDrawable gifDrawable = GifDrawable.createDrawable(resBytes);
                stv_4.setVisibility(View.VISIBLE);
                stv_4.setDrawable(gifDrawable);
            }
        });


        return view;
    }

    private void initPie() {

        pieChart.setUsePercentValues(true);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.getDescription().setEnabled(false);
        pieChart.setTransparentCircleRadius(60f);

//
//        subjectdata.add(new PieEntry(20, "English"));
//        subjectdata.add(new PieEntry(80, "Chinese"));
//        subjectdata.add(new PieEntry(5, "Math"));
//        subjectdata.add(new PieEntry(10, "Science"));
//        subjectdata.add(new PieEntry(40, "Malay"));

        pieChart.animateY(1000, Easing.EaseInOutCubic);
        PieDataSet dataset = new PieDataSet(subjectdata, "");
        dataset.setSliceSpace(3f);
        dataset.setSelectionShift(5f);
        dataset.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(dataset);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        new Thread(new Runnable() {
            @Override
            public void run() {
                pieChart.setData(data);

            }
        }).start();
    }

    private void initData() {
        fAuth = FirebaseAuth.getInstance();
        UID = fAuth.getCurrentUser().getUid();

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
                    arrayList.add(enrollClass);
                    subjectdata.add(new PieEntry(Integer.valueOf(documentSnapshot.get("studyMinutes").toString()), documentSnapshot.get("subjectCode").toString()));
                }

                initPie();
            }
        });


    }
}