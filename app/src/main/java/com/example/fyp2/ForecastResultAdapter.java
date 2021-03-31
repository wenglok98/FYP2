package com.example.fyp2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fyp2.Activity.studentEnroll;
import com.example.fyp2.Class.EnrollClass;
import com.example.fyp2.Class.ReviewCommentClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ForecastResultAdapter extends RecyclerView.Adapter<ForecastResultAdapter.MyViewHolder> {
    Context context;
    ArrayList<EnrollClass> enrollClassArrayList = new ArrayList<EnrollClass>();
    FirebaseAuth fAuth;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String UID;
    FirebaseStorage storage;

    public ForecastResultAdapter(Context ct, ArrayList<EnrollClass> list) {
        context = ct;
        enrollClassArrayList = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_forecast_result, parent, false);
        return new ForecastResultAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        EnrollClass enrollClass = enrollClassArrayList.get(position);

        holder.subjectCode.setText(enrollClass.getSubjectCode().toString());
//        holder.subjectName.setText(enrollClass.getSubjectName().toString());

        if (enrollClass.getSubjectType().equals("Tutorial")) {
            Glide.with(context).load(R.drawable.tutorial_symbol).into(holder.subject_im);

        } else if (enrollClass.getSubjectType().equals("Lecture")) {
            Glide.with(context).load(R.drawable.practical_symbol).into(holder.subject_im);

        }

        Task<QuerySnapshot> docRef = fStore.collection("Enrollment").whereEqualTo("subjectCode", enrollClass.getSubjectCode()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                int p = 0;
                Double StudyMins = 0.0;
                Double Average = null;
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    EnrollClass enrollClass = new EnrollClass();
                    try
                    {
                        String asdasd = documentSnapshot.get("studyMinutes").toString();
                        StudyMins = StudyMins + Double.parseDouble(documentSnapshot.get("studyMinutes").toString());
                        p = p + 1;

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }


                }
                Average = StudyMins / p;
                if (Double.parseDouble(enrollClass.getStudyMinutes()) < Average -5)
                {
                    holder.subjecctForecast.setText("C");
                }
                else if (Double.parseDouble(enrollClass.getStudyMinutes()) < Average + 5 && Double.parseDouble(enrollClass.getStudyMinutes()) > Average-5)
                {
                    holder.subjecctForecast.setText("B");
                }
                else if(Double.parseDouble(enrollClass.getStudyMinutes()) > Average +5
                )
                {
                    holder.subjecctForecast.setText("A");
                }


            }
        });


    }

    @Override
    public int getItemCount() {
        return enrollClassArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView subjectName, subjectCode, subjecctForecast;
        private CircleImageView subject_im;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            subjecctForecast = itemView.findViewById(R.id.forecast_subject_result);
            subjectName = itemView.findViewById(R.id.forecast_subject_name);
            subjectCode = itemView.findViewById(R.id.forecast_subject_code);
            subject_im = itemView.findViewById(R.id.forecast_subject_im);

        }
    }

}
