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
import com.example.fyp2.Class.SubjectClassModel;

import java.util.ArrayList;

public class SubjectListAdapter extends RecyclerView.Adapter<SubjectListAdapter.MyViewHolder> {
    Context context;
    ArrayList<SubjectClassModel> subjectListAdapterArrayList = new ArrayList<SubjectClassModel>();


    public SubjectListAdapter(Context ct, ArrayList<SubjectClassModel> subjectListAdapters) {
        context = ct;
        subjectListAdapterArrayList = subjectListAdapters;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_subject, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        SubjectClassModel sb = subjectListAdapterArrayList.get(position);


        holder.tv_name.setText(sb.getSubjectName());
        holder.tv_code.setText(sb.getSubjectCode());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SubjectDetailActivity.class);
                i.putExtra("subjectName", sb.getSubjectName());
                i.putExtra("subjectCode", sb.getSubjectCode());
                i.putExtra("subjectDesc", sb.getSubjectDescription());
                i.putExtra("subjectType", sb.getSubjectType());
                context.startActivity(i);


//                AppManager.getAppManager().ToOtherActivity(SubjectDetailActivity.class);
            }
        });

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

    public int getLetterPosition(String letter) {
        for (int i = 0; i < subjectListAdapterArrayList.size(); i++) {
            if (String.valueOf(subjectListAdapterArrayList.get(i).getSubjectName().charAt(0)).toLowerCase().
                    equals(letter.toLowerCase())) {
                return i;
            }
        }
        return -1;
    }
}
