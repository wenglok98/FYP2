package com.example.fyp2;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.fyp2.Class.EnrollClass;
import com.example.fyp2.Class.ReviewCommentClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import per.wsj.library.AndRatingBar;

public class EnrollReviewAdapter extends RecyclerView.Adapter<EnrollReviewAdapter.MyViewHolder> {
    Context context;
    ArrayList<ReviewCommentClass> subjectListAdapterArrayList = new ArrayList<ReviewCommentClass>();
    FirebaseStorage storage;
    StorageReference storageReference;

    public EnrollReviewAdapter(Context ct, ArrayList<ReviewCommentClass> subjectListAdapters) {
        context = ct;
        subjectListAdapterArrayList = subjectListAdapters;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.adapter_review_subject, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ReviewCommentClass sb = subjectListAdapterArrayList.get(position);

        holder.name.setText(sb.getName());
        holder.enrolldate.setText("Commented on : " +  sb.getLeaveCommentDate());
        holder.comments.setText(sb.getComment());

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        storageReference.child("images/" + sb.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                int avatarSize = context.getResources().getDimensionPixelSize(R.dimen._300sdp);

                Picasso.with(context)
                        .load(uri)
                        .placeholder(R.drawable.img_circle_placeholder)
                        .resize(avatarSize, avatarSize)
                        .centerCrop()
                        .error(R.drawable.jisoo)
                        .transform(new CircleTransformation())
                        .into(holder.pp_im);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Snackbar.make(getCurrentFocus(),"Profile Picture Retrieve Failed !", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });



    }

    @Override
    public int getItemCount() {
        return subjectListAdapterArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, enrolldate, comments;
        private CircleImageView pp_im;
        private AndRatingBar ratingBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.reviewName_tv);
            enrolldate = itemView.findViewById(R.id.enrollDate_tv);
            comments = itemView.findViewById(R.id.reviewComment_tv);
            pp_im = itemView.findViewById(R.id.reviewPP_CIM);
            ratingBar = itemView.findViewById(R.id.ratingBar);


        }
    }

}
