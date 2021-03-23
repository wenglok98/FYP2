package com.example.fyp2;



import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.applozic.mobicomkit.uiwidgets.conversation.ConversationUIService;
import com.applozic.mobicomkit.uiwidgets.conversation.activity.ConversationActivity;
import com.bumptech.glide.Glide;
import com.example.fyp2.Activity.SubjectDetailActivity;
import com.example.fyp2.BaseApp.AppManager;
import com.example.fyp2.Class.EnrollClass;
import com.example.fyp2.Class.SubjectClassModel;
import com.example.fyp2.Class.UsersClass;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.auth.User;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StudentEnrolledAdapter extends RecyclerView.Adapter<StudentEnrolledAdapter.MyViewHolder> {
    Context context;
    ArrayList<UsersClass> subjectListAdapterArrayList = new ArrayList<UsersClass>();
    ArrayList<String> enrolledTimeList = new ArrayList<String>();
    StorageReference storageReference;
    FirebaseStorage storage;


    public StudentEnrolledAdapter(Context ct, ArrayList<UsersClass> subjectListAdapters, ArrayList<String> enrolledTimeList1) {
        context = ct;
        subjectListAdapterArrayList = subjectListAdapters;
        enrolledTimeList = enrolledTimeList1;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_student_enroll_amount, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UsersClass sb = subjectListAdapterArrayList.get(position);

        holder.stuName.setText(sb.getUsername());
        holder.enrollemntDate.setText("Enrolled Date : " + enrolledTimeList.get(position).toString());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
holder.itemView.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra(ConversationUIService.USER_ID, sb.getUserid());
        intent.putExtra(ConversationUIService.DISPLAY_NAME, sb.getUsername()); //put it for displaying the title.
        intent.putExtra(ConversationUIService.TAKE_ORDER,true); //Skip chat list for showing on back press
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }
});
        storageReference.child("images/" + sb.getUserid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(context).load(uri).into(holder.stuGender);
//                int avatarSize = context.getDimensionPixelSize(R.dimen._300sdp);
//                Picasso.with(getApplicationContext())
//                        .load(uri)
//                        .placeholder(R.drawable.img_circle_placeholder)
//                        .resize(avatarSize, avatarSize)
//                        .centerCrop()
//                        .error(R.drawable.jisoo)
//                        .transform(new CircleTransformation())
//                        .into(profileBinding.profilePicture);

            }
        });


    }

    @Override
    public int getItemCount() {
        return subjectListAdapterArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView stuName, enrollemntDate;
        private CircleImageView stuGender;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            stuName = itemView.findViewById(R.id.studentName_tv);
            enrollemntDate = itemView.findViewById(R.id.enrollment_date_tv);
            stuGender = itemView.findViewById(R.id.gender_im);


        }
    }

}