package com.example.fyp2.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fyp2.BaseApp.AppManager;
import com.example.fyp2.CircleTransformation;
import com.example.fyp2.Class.UsersClass;
import com.example.fyp2.R;
import com.example.fyp2.Utils.SharedPreferenceUtil;
import com.example.fyp2.Utils.SnackUtil;
import com.example.fyp2.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding profileBinding;
    Animation midtoleft, midtoright, lefttomid, righttomid;

    String UID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String name = "";
    String id = "";
    String phone = "";
    String email = "";
    String address = "";
    String cgpa = "";
    String gpa = "";
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        View view = profileBinding.getRoot();
        setContentView(view);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        fAuth = FirebaseAuth.getInstance();
        UID = fAuth.getCurrentUser().getUid();


        //Date
        initData();

        //View
        initAppTitle();
        init_bt_state();
        init_animation();
        init_bt_onClick();



    }

    private void init_animation() {

        midtoleft = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.mid_to_left);
        midtoright = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.mid_to_right);
        lefttomid = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.left_to_mid);
        righttomid = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.right_to_mid);

    }

    private void init_bt_state() {
        profileBinding.saveBt.setVisibility(View.INVISIBLE);
        profileBinding.cancelBt.setVisibility(View.INVISIBLE);
    }

    private void init_bt_onClick() {

        profileBinding.profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        profileBinding.editBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileBinding.editBt.setVisibility(View.INVISIBLE);
                profileBinding.cancelBt.setVisibility(View.VISIBLE);
                profileBinding.cancelBt.startAnimation(midtoleft);
                profileBinding.saveBt.setVisibility(View.VISIBLE);
                profileBinding.saveBt.startAnimation(midtoright);


                profileBinding.studentAddress.setEnabled(true);
                profileBinding.studentEmail.setEnabled(true);
                profileBinding.studentID.setEnabled(true);
                profileBinding.studentName.setEnabled(true);
                profileBinding.studentPhone.setEnabled(true);
            }
        });

        profileBinding.cancelBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cancelOrSaveView();

            }
        });


        profileBinding.saveBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrSaveView();

                id = profileBinding.studentID.getText().toString();
                email = profileBinding.studentEmail.getText().toString();
                phone = profileBinding.studentPhone.getText().toString();
                address = profileBinding.studentAddress.getText().toString();
//                cgpa = profileBinding.studentName.getText().toString();
//                gpa = profileBinding.studentName.getText().toString();
                name = profileBinding.studentName.getText().toString();

                cgpa = "2.87";
                gpa = "2.12";


                DocumentReference documentReference = fStore.collection("Users").document(UID);
                UsersClass newReg = new UsersClass();
                newReg.setUserid(UID);
                newReg.setUserStuid(id);
                newReg.setUseremail(email);
                newReg.setUserPhone(phone);
                newReg.setUserAddress(address);
                newReg.setUserCgpa(cgpa);
                newReg.setUserGpa(gpa);
                newReg.setUsername(name);
                documentReference.set(newReg).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        SnackUtil.show(ProfileActivity.this,"Personal Information Updated Successfully ! ");
//                        Snackbar.make(getCurrentFocus(), "Personal Information Updated Successfully ! ", BaseTransientBottomBar.LENGTH_SHORT).show();
                        SharedPreferenceUtil.saveToPrefs(getApplicationContext(), "username", name);
                        SharedPreferenceUtil.saveToPrefs(getApplicationContext(), "userStuid", id);
                        SharedPreferenceUtil.saveToPrefs(getApplicationContext(), "userPhone", phone);
                        SharedPreferenceUtil.saveToPrefs(getApplicationContext(), "useremail", email);
                        SharedPreferenceUtil.saveToPrefs(getApplicationContext(), "userAddress", address);
                    }
                });


            }
        });
    }

    private void initAppTitle() {
        ((TextView) findViewById(R.id.app_title_tv)).setText("Profile Data ");
        findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }

    private void initData() {

        profileBinding.studentAddress.setEnabled(false);
        profileBinding.studentEmail.setEnabled(false);
        profileBinding.studentID.setEnabled(false);
        profileBinding.studentName.setEnabled(false);
        profileBinding.studentPhone.setEnabled(false);


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
                        .into(profileBinding.profilePicture);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                Snackbar.make(getCurrentFocus(),"Profile Picture Retrieve Failed !", BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });



        DocumentReference documentReference = fStore.collection("Users").document(UID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {


                try {
                    name = task.getResult().getString("username").toString();

                    try {
                        id = task.getResult().getString("userStuid").toString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        phone = task.getResult().getString("userPhone").toString();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        email = task.getResult().getString("useremail").toString();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        address = task.getResult().getString("userAddress").toString();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    SharedPreferenceUtil.saveToPrefs(getApplicationContext(), "username", name);
                    SharedPreferenceUtil.saveToPrefs(getApplicationContext(), "userStuid", id);
                    SharedPreferenceUtil.saveToPrefs(getApplicationContext(), "userPhone", phone);
                    SharedPreferenceUtil.saveToPrefs(getApplicationContext(), "useremail", email);
                    SharedPreferenceUtil.saveToPrefs(getApplicationContext(), "userAddress", address);
                    setTextData();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private void setTextData() {
        profileBinding.studentName.setText(SharedPreferenceUtil.getFromPrefs(getApplicationContext(), "username", ""));
        profileBinding.studentID.setText(SharedPreferenceUtil.getFromPrefs(getApplicationContext(), "userStuid", ""));
        profileBinding.studentPhone.setText(SharedPreferenceUtil.getFromPrefs(getApplicationContext(), "userPhone", ""));
        profileBinding.studentEmail.setText(SharedPreferenceUtil.getFromPrefs(getApplicationContext(), "useremail", ""));
        profileBinding.studentAddress.setText(SharedPreferenceUtil.getFromPrefs(getApplicationContext(), "userAddress", ""));


    }

    private void cancelOrSaveView() {
        profileBinding.studentAddress.setEnabled(false);
        profileBinding.studentEmail.setEnabled(false);
        profileBinding.studentID.setEnabled(false);
        profileBinding.studentName.setEnabled(false);
        profileBinding.studentPhone.setEnabled(false);

        profileBinding.cancelBt.startAnimation(lefttomid);
        profileBinding.cancelBt.setVisibility(View.INVISIBLE);

        profileBinding.saveBt.startAnimation(righttomid);
        profileBinding.saveBt.setVisibility(View.INVISIBLE);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                profileBinding.editBt.setVisibility(View.VISIBLE);

            }
        }, 200);

    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();

            int avatarSize = getResources().getDimensionPixelSize(R.dimen._300sdp);
            Picasso.with(getApplicationContext())
                    .load(imageUri)
                    .placeholder(R.drawable.img_circle_placeholder)
                    .resize(avatarSize, avatarSize)
                    .centerCrop()
                    .transform(new CircleTransformation())
                    .into(profileBinding.profilePicture);



            uploadPicture();

        }

    }

    private void uploadPicture() {

        StorageReference imageRef = storageReference.child("images/" + UID);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Snackbar.make(getCurrentFocus(), "Profile Picture Upload Successfully!", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(getCurrentFocus(), "Profile Picture Upload Failed!", BaseTransientBottomBar.LENGTH_SHORT).show();

                    }
                });

    }
}