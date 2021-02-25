package com.example.fyp2.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.coorchice.library.SuperTextView;
import com.coorchice.library.gifdecoder.GifDrawable;
import com.coorchice.library.utils.STVUtils;
import com.coorchice.library.utils.ThreadPool;
import com.example.fyp2.Activity.NotesActivity;
import com.example.fyp2.BaseApp.AppManager;
import com.example.fyp2.R;
import com.fujiyuu75.sequent.Sequent;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private SuperTextView stv_4;
    private SuperTextView notebt;
    private LinearLayout ll;
    PieChart pieChart;

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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                stv_4 = (SuperTextView) view.findViewById(R.id.stv_4);
                ll = view.findViewById(R.id.homell);
                notebt = (SuperTextView)view.findViewById(R.id.notesbt);
                pieChart = view.findViewById(R.id.piechart);
            }
        });

        initPie();
notebt.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                AppManager.getAppManager().ToOtherActivity(NotesActivity.class);
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

        ArrayList<PieEntry> subjectdata = new ArrayList<>();

        subjectdata.add(new PieEntry(20, "English"));
        subjectdata.add(new PieEntry(80, "Chinese"));
        subjectdata.add(new PieEntry(5, "Math"));
        subjectdata.add(new PieEntry(10, "Science"));
        subjectdata.add(new PieEntry(40, "Malay"));

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
}