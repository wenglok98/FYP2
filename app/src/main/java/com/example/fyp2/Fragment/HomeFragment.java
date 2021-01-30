package com.example.fyp2.Fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.coorchice.library.SuperTextView;
import com.coorchice.library.gifdecoder.GifDrawable;
import com.coorchice.library.utils.STVUtils;
import com.coorchice.library.utils.ThreadPool;
import com.example.fyp2.R;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    private SuperTextView stv_4;

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

        stv_4 = (SuperTextView) view.findViewById(R.id.stv_4);

        pieChart = view.findViewById(R.id.piechart);
        initPie();
        byte[] resBytes = STVUtils.getResBytes(getContext(), R.drawable.book);
        final GifDrawable gifDrawable = GifDrawable.createDrawable(resBytes);
        stv_4.setVisibility(View.VISIBLE);
        stv_4.setDrawable(gifDrawable);

        return view;
    }

    private void initPie(){
        pieChart.setUsePercentValues(true);
        pieChart.setExtraOffsets(5,10,5,5);

        pieChart.setDragDecelerationEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.getDescription().setEnabled(false);
        pieChart.setTransparentCircleRadius(60f);

        ArrayList<PieEntry> subjectdata = new ArrayList<>();

        subjectdata.add(new PieEntry(20,"English"));
        subjectdata.add(new PieEntry(80,"Chinese"));
        subjectdata.add(new PieEntry(5,"Math"));
        subjectdata.add(new PieEntry(10,"Science"));
        subjectdata.add(new PieEntry(40,"Malay"));

        pieChart.animateY(1000, Easing.EaseInOutCubic);
        PieDataSet dataset = new PieDataSet(subjectdata,"");
        dataset.setSliceSpace(3f);
        dataset.setSelectionShift(5f);
        dataset.setColors(ColorTemplate.JOYFUL_COLORS);

        PieData data = new PieData(dataset);
        data.setValueTextSize(10f);
        data.setValueTextColor(Color.YELLOW);

        pieChart.setData(data);
    }
}