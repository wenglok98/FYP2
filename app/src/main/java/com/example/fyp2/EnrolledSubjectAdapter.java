package com.example.fyp2;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fyp2.Activity.SubjectDetailActivity;
import com.example.fyp2.BaseApp.AppManager;
import com.example.fyp2.Class.EnrollClass;
import com.example.fyp2.Class.SubjectClassModel;

import java.util.ArrayList;

public class EnrolledSubjectAdapter extends RecyclerView.Adapter<EnrolledSubjectAdapter.MyViewHolder> {
    Context context;
    ArrayList<EnrollClass> subjectListAdapterArrayList = new ArrayList<EnrollClass>();


    public EnrolledSubjectAdapter(Context ct, ArrayList<EnrollClass> subjectListAdapters) {
        context = ct;
        subjectListAdapterArrayList = subjectListAdapters;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_enrolled_subject, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EnrollClass sb = subjectListAdapterArrayList.get(position);


        holder.tv_name.setText(sb.getTimeStamp());
        holder.tv_code.setText(sb.getSubjectCode());


        if (sb.getSubjectType().equals("Tutorial")) {
            Glide.with(context).load(R.drawable.tutorial_symbol).into(holder.im_subject);
        } else if (sb.getSubjectType().equals("Lecture")) {
            Glide.with(context).load(R.drawable.practical_symbol).into(holder.im_subject);

        }

    }

    @Override
    public int getItemCount() {
        return subjectListAdapterArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_code;
        private ImageView im_subject;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_code = itemView.findViewById(R.id.tv_subjectcode);
            tv_name = itemView.findViewById(R.id.tv_subjectname);
            im_subject = itemView.findViewById(R.id.locsymbol);

        }
    }

}
