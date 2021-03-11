package com.example.fyp2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_name, tv_code;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_code = itemView.findViewById(R.id.tv_subjectcode);
            tv_name = itemView.findViewById(R.id.tv_subjectname);


        }
    }
    public int getLetterPosition(String letter){
        for (int i = 0 ; i < subjectListAdapterArrayList.size(); i++){
            if(subjectListAdapterArrayList.get(i).getSubjectCode().equals(letter) ){
                return i;
            }
        }
        return -1;
    }
}
