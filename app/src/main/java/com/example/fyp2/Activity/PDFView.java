package com.example.fyp2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.example.fyp2.BaseApp.BaseActivity;
import com.example.fyp2.R;
import com.example.fyp2.Utils.SharedPreferenceUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PDFView extends BaseActivity {
    FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    StorageReference mStorageRef;
    com.github.barteksc.pdfviewer.PDFView book1;
    int startinghour, startingminutes, closinghour, closingminutes;
    FirebaseAuth fAuth;
    String subjectCode;
    String UID;
    String docID;
    String sub1, sub2, sub3, sub4, writein, previoustotal;
    int totalhour, totalmins, totaloverall, totalbeforesum, lasttotal;

    String studyTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_p_d_f_view);
        String materialName = getIntent().getStringExtra("materialName");
        subjectCode = getIntent().getStringExtra("subjectCode");
        studyTime = getIntent().getStringExtra("studyTime");

        fAuth = FirebaseAuth.getInstance();

        mStorageRef = FirebaseStorage.getInstance().getReference().child("Material/" + subjectCode + "/" + materialName + ".pdf");
        book1 = findViewById(R.id.pdfViewFull);

        try {
            final File localFile = File.createTempFile(materialName, "pdf");
            mStorageRef.getFile(localFile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                            book1.fromFile(localFile).load(); // open and load the pdf
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }

        initStartingTime();
    }

    private void initStartingTime() {

        Date currentdate = new Date();
        SimpleDateFormat hour = new SimpleDateFormat("hh");
        SimpleDateFormat min = new SimpleDateFormat("mm");

        String strthour = hour.format(currentdate);
        startinghour = Integer.parseInt(strthour);
        String strtmins = min.format(currentdate);
        startingminutes = Integer.parseInt(strtmins);
        UID = SharedPreferenceUtil.getFromPrefs(getApplicationContext(), "UID", "");
        Task<QuerySnapshot> documentReference = fstore.collection("Enrollment").whereEqualTo("studentID", UID).whereEqualTo("subjectCode", subjectCode)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            docID = documentSnapshot.getId();
                            previoustotal = documentSnapshot.getString("studyMinutes");
                        }
                    }
                });

    }

    @Override
    public void onBackPressed() {

        Date exittime = new Date();
        SimpleDateFormat exhour = new SimpleDateFormat("hh");
        SimpleDateFormat exmin = new SimpleDateFormat("mm");

        String exithour = exhour.format(exittime);
        closinghour = Integer.parseInt(exithour);
        String exitminutes = exmin.format(exittime);
        closingminutes = Integer.parseInt(exitminutes);


        totalhour = closinghour - startinghour;


        totalmins = closingminutes - startingminutes;


        totaloverall = (totalhour * 60) + totalmins;



        totalbeforesum = Integer.parseInt(previoustotal);
        lasttotal = totalbeforesum + totaloverall;
        writein = Integer.toString(lasttotal);
        Map<String, Object> map = new HashMap<>();
        map.put("studyMinutes", writein);

        fstore.collection("Enrollment").document(docID).set(map, SetOptions.merge());
        Toast.makeText(PDFView.this, "The total is" + Integer.toString(totaloverall), Toast.LENGTH_SHORT).show();
        super.onBackPressed();

    }
}