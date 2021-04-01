package com.example.fyp2.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.fyp2.BaseApp.BaseActivity;
import com.example.fyp2.R;
import com.example.fyp2.Utils.Util;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;


import java.io.FileInputStream;
import java.io.IOException;

public class NotesActivity extends BaseActivity {

    ImageView im;
    TextView tv;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap imageBitmap;
    Bitmap bmp = null;
    Button capture, detect;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
        im = findViewById(R.id.imview);
        tv = findViewById(R.id.txview);
        capture = findViewById(R.id.cpbt);
        detect = findViewById(R.id.detectbt);
        initAppTitle();


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();



        String filename = getIntent().getStringExtra("image");
        uri = Uri.parse(filename);
        try {
            imageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        try {
//            FileInputStream is = this.openFileInput(filename);
//            bmp = BitmapFactory.decodeStream(is);
//            is.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        im.setImageBitmap(bmp);

        Glide.with(getApplicationContext()).load(imageBitmap).into(im);
//        imageBitmap = (Bitmap) bundle.get("images");

//        im.setImageBitmap(imageBitmap);



        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detectTextfromimage();
            }
        });
    }
    private void detectTextfromimage() {

        FirebaseVisionImage firebaseVisionImage = FirebaseVisionImage.fromBitmap(imageBitmap);


        FirebaseVisionTextRecognizer detector = FirebaseVision.getInstance().getOnDeviceTextRecognizer();

        Task<FirebaseVisionText> result =
                detector.processImage(firebaseVisionImage)
                        .addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                                Intent intent = new Intent(NotesActivity.this, EmptyNotesCreate2.class);


                                intent.putExtra("notes", firebaseVisionText.getText());
                                startActivity(intent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(NotesActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        });
    }

    private void initAppTitle() {
        ((TextView) findViewById(R.id.app_title_tv)).setText("Confirm Image");
        findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
}