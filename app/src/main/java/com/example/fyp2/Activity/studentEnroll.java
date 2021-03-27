package com.example.fyp2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp2.BaseApp.AppManager;
import com.example.fyp2.BaseApp.BaseActivity;
import com.example.fyp2.CircleTransformation;
import com.example.fyp2.Class.EnrollClass;
import com.example.fyp2.Class.ReviewCommentClass;
import com.example.fyp2.Class.SubjectClassModel;
import com.example.fyp2.EnrolledSubjectAdapter;
import com.example.fyp2.R;
import com.example.fyp2.SubjectListAdapter;
import com.example.fyp2.Utils.SharedPreferenceUtil;
import com.example.fyp2.Utils.SnackUtil;
import com.example.fyp2.databinding.ActivityEnrollNewSubjectBinding;
import com.example.fyp2.databinding.ActivityStudentEnrollBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;
import per.wsj.library.AndRatingBar;

public class studentEnroll extends BaseActivity {
    private ArrayList<EnrollClass> dataSet = new ArrayList<>();
    EnrolledSubjectAdapter adapter;
    ActivityStudentEnrollBinding activityEnrollNewSubjectBinding;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String UID;
    FirebaseStorage storage;
    StorageReference storageReference;
    ItemTouchHelper.SimpleCallback simpleCallback;
    ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityEnrollNewSubjectBinding = ActivityStudentEnrollBinding.inflate(getLayoutInflater());
        View view = activityEnrollNewSubjectBinding.getRoot();
        setContentView(view);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        fAuth = FirebaseAuth.getInstance();
        UID = fAuth.getCurrentUser().getUid();
//        setContentView(R.layout.activity_student_enroll);
        initAppTitle();

        simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                Toast.makeText(EnrollNewSubject.this, "SWIPED", Toast.LENGTH_SHORT).show();
                switch (direction) {
                    case ItemTouchHelper.RIGHT:
                        commentReview(viewHolder.getAdapterPosition());
                        break;
                    case ItemTouchHelper.LEFT:
                        showDeleteDialog(dataSet.get(viewHolder.getAdapterPosition()).getSubjectCode());
                        break;
                }
//                Toast.makeText(studentEnroll.this, String.valueOf(viewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();

            }
        };
        itemTouchHelper = new ItemTouchHelper(simpleCallback);
        initData();


    }

    private void initAppTitle() {
        ((TextView) findViewById(R.id.app_title_tv)).setText("Subjects");
        findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                adddata();
                onBackPressed();

            }
        });
        findViewById(R.id.btn_add_subject).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_add_subject).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getAppManager().ToOtherActivity(EnrollNewSubject.class);
            }
        });


    }

    private void initData() {
        adapter = new EnrolledSubjectAdapter(studentEnroll.this, dataSet);
        activityEnrollNewSubjectBinding.enrolledSubjectRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        activityEnrollNewSubjectBinding.enrolledSubjectRecycler.setAdapter(adapter);
        activityEnrollNewSubjectBinding.enrolledSubjectRecycler.addItemDecoration(new DividerItemDecoration(activityEnrollNewSubjectBinding.enrolledSubjectRecycler.getContext(), DividerItemDecoration.VERTICAL));

        itemTouchHelper.attachToRecyclerView(activityEnrollNewSubjectBinding.enrolledSubjectRecycler);
        dataSet.clear();
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
                    dataSet.add(enrollClass);
                }
                adapter.notifyDataSetChanged();
            }
        });


    }

    private void commentReview(Integer position) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                this, R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(this)
                .inflate(
                        R.layout.layout_bottom_sheet,
                        (RelativeLayout) findViewById(R.id.bottomSheetContainer)

                );

        CircleImageView circleImageView;
        circleImageView = bottomSheetView.findViewById(R.id.bottom_sheet_CIM);
        TextView name;
        name = bottomSheetView.findViewById(R.id.bottomsheet_name_tv);
        EditText edt;
        edt = bottomSheetView.findViewById(R.id.bottomsheet_comment_edt);
        Task<QuerySnapshot> documentReference = fStore.collection("Review")
                .whereEqualTo("uid", UID).whereEqualTo("subjectCode", dataSet.get(position)
                        .getSubjectCode()).get();
        documentReference.addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    bottomSheetView.findViewById(R.id.bottomsheet_submit_bt).setEnabled(false);
                    edt.setHint(documentSnapshot.get("comment").toString());
                    AndRatingBar ratingBar;
                    ratingBar = bottomSheetView.findViewById(R.id.review_bottomsheet_ratingbar);
                    ratingBar.setEnabled(false);
                    Float asdfsdaf = Float.parseFloat(documentSnapshot.get("rating").toString());
                    ratingBar.setRating(Float.parseFloat(documentSnapshot.get("rating").toString()));
                    edt.setBackgroundColor(Color.parseColor("#b3b3b3"));
                    edt.setEnabled(false);

                }
//                if (!queryDocumentSnapshots.getDocuments().isEmpty()) {
//
//                }
            }
        });
        storageReference.child("images/" + UID).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                int avatarSize = getResources().getDimensionPixelSize(R.dimen._300sdp);
                Picasso.with(getApplicationContext())
                        .load(uri)
                        .placeholder(R.drawable.img_circle_placeholder)
                        .resize(avatarSize, avatarSize)
                        .centerCrop()
                        .error(R.drawable.jisoo)
                        .transform(new CircleTransformation())
                        .into(circleImageView);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Snackbar.make(getCurrentFocus(),"Profile Picture Retrieve Failed !", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        name.setText(SharedPreferenceUtil.getFromPrefs(getApplicationContext(), "username", ""));


        bottomSheetView.findViewById(R.id.bottomsheet_submit_bt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndRatingBar andRatingBar;
                andRatingBar = bottomSheetView.findViewById(R.id.review_bottomsheet_ratingbar);

                String starValue = String.valueOf(andRatingBar.getRating());
                String comment_review = edt.getText().toString();
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String strDate = dateFormat.format(date);

                ReviewCommentClass tempComment = new ReviewCommentClass();
                tempComment.setComment(comment_review);
                tempComment.setEnrollmentDate(dataSet.get(position).getTimeStamp());
                tempComment.setName(SharedPreferenceUtil.getFromPrefs(getApplicationContext(), "username", ""));
                tempComment.setRating(starValue);
                tempComment.setLeaveCommentDate(strDate);

                tempComment.setUid(UID);
                tempComment.setSubjectCode(dataSet.get(position).getSubjectCode());

                DocumentReference addCommentoFirebase = fStore.collection("Review").document();

                addCommentoFirebase.set(tempComment).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        SnackUtil.show(getApplication(), "Submit Review Successed");
                        bottomSheetDialog.dismiss();
                    }
                });

            }
        });
//        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet, findViewById(R.id.bottomSheetContainer));
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }

    private void showDeleteDialog(String subjectCode) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Drop Subject")
                .setMessage("Are you sure you want to drop Subject?")
                .setPositiveButton("Drop", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        deleteSubject(subjectCode);


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .show();
    }

    private void deleteSubject(String subjectCode) {

        Task<QuerySnapshot> documentReference = fStore.collection("Enrollment")
                .whereEqualTo("studentID", UID)
                .whereEqualTo("subjectCode", subjectCode).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        fStore.collection("Enrollment").document(queryDocumentSnapshots.getDocuments().get(0).getId())
                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                SnackUtil.show(getApplication(), "Subject Dropped");

                                initData();
                            }
                        });

                    }
                });

        adapter.notifyDataSetChanged();
    }
}