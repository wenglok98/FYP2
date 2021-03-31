package com.example.fyp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp2.Activity.EmptyNotesCreate;
import com.example.fyp2.Activity.EnrollNewSubject;
import com.example.fyp2.Activity.NotesActivity;
import com.example.fyp2.BaseApp.AppManager;
import com.example.fyp2.BaseApp.BaseActivity;
import com.example.fyp2.Class.NotesClass;
import com.example.fyp2.Class.UsersClass;
import com.example.fyp2.Utils.SharedPreferenceUtil;
import com.example.fyp2.databinding.ActivityNotesListBinding;
import com.example.fyp2.databinding.ActivityNotesOpenBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import cc.solart.wave.WaveSideBarView;

public class NotesListActivity extends BaseActivity {

    ImageView btn_capture;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;
    Uri uri2;
    ActivityNotesListBinding activityNotesOpenBinding;
    Bitmap srcbmp;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNotesOpenBinding = ActivityNotesListBinding.inflate(getLayoutInflater());
        View view = activityNotesOpenBinding.getRoot();
        setContentView(view);

        initAppTitle();

        initView();

        activityNotesOpenBinding.addNotesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getAppManager().ToOtherActivity(EmptyNotesCreate.class);
            }
        });
        initFirebase();
    }


    private void initAppTitle() {
        ((TextView) findViewById(R.id.app_title_tv)).setText("Notes");
        findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_capture = findViewById(R.id.btn_camera);
        btn_capture.setVisibility(View.VISIBLE);
        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AppManager.getAppManager().ToOtherActivity(NotesActivity.class);
                dispatchTakePictureIntent();
            }

        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    private void initView() {


    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {

            Intent intent = new Intent(NotesListActivity.this, NotesActivity.class);
            Bundle extras = data.getExtras();
            String asdasd = data.getDataString();
            Uri sdfasdf = data.getData();
//            imageBitmap = (Bitmap) extras.get("data");
            imageBitmap = (Bitmap) extras.get("data");
            uri2 = getImageUri(NotesListActivity.this, imageBitmap);

            try {
                srcbmp = BitmapFactory.decodeStream(NotesListActivity.this.getContentResolver().openInputStream(uri2), null, null);

//            srcbmp  = BitmapFactory.decodeStream(this.getContentResolver().openInputStream(uri2),null,null);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Uri tempUri = getImageUri(getApplicationContext(), srcbmp);
//            String filename = "bitmap.png";
//            FileOutputStream stream = null;
//            try {
//                stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
//            imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);


            intent.putExtra("image", tempUri.toString());
            startActivity(intent);
//            AppManager.getAppManager().ToOtherActivity(NotesActivity.class);
//            imageBitmap = (Bitmap) extras.get("data");
//            im.setImageBitmap(imageBitmap);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private void initFirebase() {
        ArrayList<NotesClass> notesClasses = new ArrayList<>();
        String UID = SharedPreferenceUtil.getFromPrefs(getApplicationContext(), "UID", "");
        Task<QuerySnapshot> documentReference = fStore.collection("Notes")
                .whereEqualTo("uid", UID)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            NotesClass notesClass = new NotesClass();
                            notesClass.setNotes(documentSnapshot.getString("notes"));
                            notesClass.setTitle(documentSnapshot.getString("title"));
                            notesClass.setTimeStamp(documentSnapshot.getString("timeStamp"));
                            notesClass.setUID(documentSnapshot.getString("uid"));
                            notesClass.setFirebase_id(documentSnapshot.getId());
                            notesClasses.add(notesClass);

                        }
                        NotesListAdapter adapter;
                        adapter = new NotesListAdapter(NotesListActivity.this, notesClasses);
                        activityNotesOpenBinding.notelistrecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        activityNotesOpenBinding.notelistrecycler.setAdapter(adapter);
                        activityNotesOpenBinding.notelistrecycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL));


                    }
                });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initFirebase();
    }
}