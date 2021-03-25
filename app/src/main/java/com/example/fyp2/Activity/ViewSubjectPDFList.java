package com.example.fyp2.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.fyp2.Class.Material;
import com.example.fyp2.MaterialListAdapter;
import com.example.fyp2.R;
import com.example.fyp2.databinding.ActivityMainSubjectBinding;
import com.example.fyp2.databinding.ActivityViewSubjectPDFListBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ViewSubjectPDFList extends AppCompatActivity {
    ActivityViewSubjectPDFListBinding activityMainSubjectBinding;
    FirebaseFirestore fstore = FirebaseFirestore.getInstance();
    ArrayList<Material> materiallist = new ArrayList<>();
    String idoffirst;

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_subject_p_d_f_list);

        activityMainSubjectBinding = ActivityViewSubjectPDFListBinding.inflate(getLayoutInflater());
        View view = activityMainSubjectBinding.getRoot();
        setContentView(view);

        initAppTitle();
        initAdapter();
    }


    private void initAppTitle() {
        ((TextView) findViewById(R.id.app_title_tv)).setText("Study Material");
        findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                adddata();
                onBackPressed();
//                Toast.makeText(SubjectDetailActivity.this, String.valueOf(activitySubjectDetailBinding.ratingBar.getRating()), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_add_subject).setVisibility(View.INVISIBLE);


    }

    private void initAdapter() {
        final MaterialListAdapter adapter = new MaterialListAdapter(getApplicationContext(), R.layout.adapter_view_material, materiallist);
        String subjectCode = getIntent().getStringExtra("subjectCode");
        String studyTime = getIntent().getStringExtra("studyTime");

        fstore.collection("Subjects").whereEqualTo("subjectCode", subjectCode)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        if (queryDocumentSnapshots != null) {
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
//                            tx1.setText(snapshot.getId());
                                idoffirst = snapshot.getId();
//                                totalmins = snapshot.getString("visitedminutes");
                                fstore.collection("Subjects").document(idoffirst).collection("Material")
                                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                            @Override
                                            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                                if (queryDocumentSnapshots != null) {
                                                    for (DocumentSnapshot snapshot : queryDocumentSnapshots) {


                                                        Material mt = new Material(snapshot.getString("fileName"));
                                                        materiallist.add(mt);

                                                    }
                                                }
                                                activityMainSubjectBinding.viewMaterialList.setAdapter(adapter);
                                            }

                                        });

                            }
                        }
                    }
                });

activityMainSubjectBinding.viewMaterialList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                materiallist.get(i).getFileName();

                Intent intent = new Intent(getApplicationContext(), PDFView.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("name",);
                intent.putExtra("materialName", materiallist.get(i).getFileName());
                String subjectCode = getIntent().getStringExtra("subjectCode");
                intent.putExtra("subjectCode", subjectCode);
                String studyTime = getIntent().getStringExtra("studyTime");
                intent.putExtra("studyTime", studyTime);


//                bundle.putString("code", data);
//                bundle.putString("enrolluid", enrolluid);
//                intent.putExtras(bundle);
//                intent.putExtra("name",materiallist.get(i).getFileName());
//                intent.putExtra("code",data);
                startActivity(intent);


                //Start of using Fragment
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                Bundle bundle = new Bundle();
//                bundle.putString("name",materiallist.get(i).getFileName());
//                bundle.putString("code",data);
//                Fragment_ViewMaterial fr = new Fragment_ViewMaterial();
//                fr.setArguments(bundle);
//
//                fragmentTransaction.replace(R.id.fragment,fr);
//                fragmentTransaction.commit();
                //End of using fragment
            }
        });

    }
}