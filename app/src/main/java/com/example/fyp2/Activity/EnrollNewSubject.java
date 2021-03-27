package com.example.fyp2.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fyp2.BaseApp.BaseActivity;
import com.example.fyp2.Class.SubjectClassModel;
import com.example.fyp2.R;
import com.example.fyp2.SubjectListAdapter;
import com.example.fyp2.ViewModel.EnrollSubjectViewModel;
import com.example.fyp2.databinding.ActivityEnrollNewSubjectBinding;
import com.example.fyp2.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cc.solart.wave.WaveSideBarView;

public class EnrollNewSubject extends BaseActivity {
    ActivityEnrollNewSubjectBinding activityEnrollNewSubjectBinding;
    EnrollSubjectViewModel enrollSubjectViewModel;
    ArrayList<SubjectClassModel> arrayList = new ArrayList<>();
    SubjectListAdapter adapter;
    private ArrayList<SubjectClassModel> dataSet = new ArrayList<>();

    FirebaseAuth fAuth;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    String UID;
    ItemTouchHelper.SimpleCallback simpleCallback;
    ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityEnrollNewSubjectBinding = ActivityEnrollNewSubjectBinding.inflate(getLayoutInflater());
        View view = activityEnrollNewSubjectBinding.getRoot();
        setContentView(view);

        simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                Toast.makeText(EnrollNewSubject.this, "SWIPED", Toast.LENGTH_SHORT).show();
                Toast.makeText(EnrollNewSubject.this, String.valueOf(viewHolder.getAdapterPosition()), Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();

            }
        };
        itemTouchHelper = new ItemTouchHelper(simpleCallback);


        ViewModelProvider.Factory factory = new ViewModelProvider.NewInstanceFactory();

        fAuth = FirebaseAuth.getInstance();
        UID = fAuth.getCurrentUser().getUid();
        initAppTitle();

//
//        enrollSubjectViewModel = new ViewModelProvider(EnrollNewSubject.this, factory).get(EnrollSubjectViewModel.class);
//
//        enrollSubjectViewModel.init();
//        enrollSubjectViewModel.getSubjectList().observe(this, new Observer<List<SubjectClassModel>>() {
//            @Override
//            public void onChanged(List<SubjectClassModel> subjectClassModels) {
//                adapter.notifyDataSetChanged();
//            }
//        });

        initAdapter();


    }

    private void initAdapter() {

        adapter = new SubjectListAdapter(EnrollNewSubject.this, dataSet);
        activityEnrollNewSubjectBinding.subjectRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        activityEnrollNewSubjectBinding.subjectRecyclerView.setAdapter(adapter);

        activityEnrollNewSubjectBinding.subjectRecyclerView.addItemDecoration(new DividerItemDecoration(activityEnrollNewSubjectBinding.subjectRecyclerView.getContext(), DividerItemDecoration.VERTICAL));

        itemTouchHelper.attachToRecyclerView(activityEnrollNewSubjectBinding.subjectRecyclerView);
        Task<QuerySnapshot> documentReference = fStore.collection("Subjects")
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                            SubjectClassModel tempSub = new SubjectClassModel();
                            tempSub.setSubjectImage(documentSnapshot.get("subjectCode").toString());
                            tempSub.setSubjectName(documentSnapshot.get("subjectName").toString());
                            tempSub.setSubjectCode(documentSnapshot.get("subjectCode").toString());
                            try {
                                tempSub.setSubjectPeople(documentSnapshot.get("subjectPeople").toString());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            tempSub.setSubjectType(documentSnapshot.get("subjectType").toString());
                            try {
                                tempSub.setSubjectDescription(documentSnapshot.get("subjectDescription").toString());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dataSet.add(tempSub);
                        }
                        dataSet.sort(Comparator.comparing(a -> a.getSubjectName()));
                        adapter.notifyDataSetChanged();

                    }


                });


        activityEnrollNewSubjectBinding.sideView.setOnTouchLetterChangeListener(new WaveSideBarView.OnTouchLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int pos = adapter.getLetterPosition(letter);

                if (pos != -1) {
                    activityEnrollNewSubjectBinding.subjectRecyclerView.scrollToPosition(pos);
                }
            }
        });


    }

    private void initAppTitle() {
        ((TextView) findViewById(R.id.app_title_tv)).setText(R.string.enrollment);
        findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                adddata();
//                adddata2();
//                adddata3();
//                adddata4();
                onBackPressed();
            }
        });
        findViewById(R.id.btn_add_subject).setVisibility(View.INVISIBLE);


    }


    private void adddata() {

        DocumentReference documentReference = fStore.collection("Subjects").document();
        DocumentReference documentReference2 = fStore.collection("Subjects").document();
        DocumentReference documentReference3 = fStore.collection("Subjects").document();
        DocumentReference documentReference4 = fStore.collection("Subjects").document();
        DocumentReference documentReference5 = fStore.collection("Subjects").document();
        DocumentReference documentReference6 = fStore.collection("Subjects").document();
        DocumentReference documentReference7 = fStore.collection("Subjects").document();
        SubjectClassModel newSub = new SubjectClassModel("asdf", "UCCD1004", "This course introduces the fundamentals of programming languages. It starts with an overview" +
                "of computers, programming and system development. The concepts of programming logic," +
                "programming structure and programming design are then introduced through flowcharts," +
                "pseudo code, algorithms, selections, loops and functions. The concept of manipulating a file in" +
                "a program is also covered. Areas like derived types such as arrays, structures and strings are" +
                "introduced.", "PROGRAMMING CONCEPTS AND PRACTICES", "20", "Tutorial");
        SubjectClassModel newSub2 = new SubjectClassModel("asdf", "UCCD1203", "The course covers database vocabulary and concepts, Entity Relationship Diagram (ERD)," +
                "translating business rules into data model components, database normalization, physical" +
                "design, and query database using Structured Query Language. ", "DATABASE DEVELOPMENT AND APPLICATIONS", "15", "Lecture");
        SubjectClassModel newSub3 = new SubjectClassModel("asdf", "UCCM2233", "This course will provide the basic concepts of statistics and probability theory to solve applied" +
                "problems. Explore statistical techniques to organize and presenting data. Also, it will cover on" +
                "statistical hypotheses tests and estimation to prepare for more advanced courses related to" +
                "probability and statistics. ", "STATISTICS", "42", "Lecture");
        SubjectClassModel newSub4 = new SubjectClassModel("asdf", "UCCN1004", "This unit introduces the fundamentals of data communication and computer networking from" +
                "the perspective of both theory and application. It starts with a general overview of the concepts" +
                "and terminologies used in networking, followed by several basic yet important topics on the" +
                "TCP/IP layered model. The students are guided on designing and testing IP subnets for" +
                "connectivity, setting up and configuring basic servers and routers. The pertinent technical" +
                "issues of the physical, data link, network, transport and application layers are explored with" +
                "emphasis on practical applications. The unit conclude with simple network monitoring and" +
                "network analysis in prepping students for solving real world network related problems as well" +
                "as designing innovative solutions in addressing IoT technology. ", "DATA COMMUNICATIONS AND NETWORKING", "12", "Tutorial");
        SubjectClassModel newSub5 = new SubjectClassModel("asdf", "UBMM1011", "Sun Zi also known as Sun Wu, a famous ancient Chinese general wrote the classic work on military" +
                "strategies – Art of War. His treatise shed light on military warfare. There are great similarities between" +
                "military strategy and business strategy. Most of the classic intelligence of Sun Zi’s Art of War can be" +
                "adapted in today’s business environment and technology practices in order to create a competitive" +
                "advantage over the oppenents and to take advantage of the situation fully", "SUN ZI'S ART OF WAR AND BUSINESS STRATEGIES", "90", "Lecture");
        SubjectClassModel newSub6 = new SubjectClassModel("asdf", "UCCD2103", "This course covers the concepts, structure, and mechanisms of operating systems. Its purpose" +
                "is to present, as clearly and completely as possible, the nature and characteristics of modern" +
                "day operating systems. ", "OPERATING SYSTEMS", "60", "Tutorial");
        SubjectClassModel newSub7 = new SubjectClassModel("asdf", "MPU32143", "This course is designed to develop students’ ability to use all the language skills they have" +
                "acquired to understand, communicate and express themselves effectively both in speech" +
                "and in writing, in an academic and professional environment in general and in an IT" +
                "environment in particular.", "ENGLISH FOR INFORMATION TECHNOLOGY", "32", "Lecture");
        documentReference.set(newSub);
        documentReference2.set(newSub2);
        documentReference3.set(newSub3);
        documentReference4.set(newSub4);
        documentReference5.set(newSub5);
        documentReference6.set(newSub6);
        documentReference7.set(newSub7);


    }

    private void adddata2() {

        DocumentReference documentReference = fStore.collection("Subjects").document();
        DocumentReference documentReference2 = fStore.collection("Subjects").document();
        DocumentReference documentReference3 = fStore.collection("Subjects").document();
        DocumentReference documentReference4 = fStore.collection("Subjects").document();
        DocumentReference documentReference5 = fStore.collection("Subjects").document();
        DocumentReference documentReference6 = fStore.collection("Subjects").document();
        DocumentReference documentReference7 = fStore.collection("Subjects").document();
        SubjectClassModel newSub = new SubjectClassModel();
        newSub.setSubjectCode("UCCD1013");
        newSub.setSubjectName("Analysis and Design of Information Systems");
        newSub.setSubjectType("Lecture");
        newSub.setSubjectDescription("The course covers the concepts, skills, methodologies, techniques, tools, and perspectives\n" +
                "essential for systems analysts to successfully develop information systems. It will begin with an\n" +
                "introduction to system concepts. Then students will be exposed to processes involve in\n" +
                "information systems and management. The following topic will covered the overview of System\n" +
                "Development Life Cycle (SDLC) and various system development methodologies. The later\n" +
                "topics will be on the 4 main phases of SDLC: Planning Phase, Analysis Phase, Design Phase\n" +
                "and Implementation Phases. \n");
        SubjectClassModel newSub2 = new SubjectClassModel();
        newSub2.setSubjectCode("UCCD1024");
        newSub2.setSubjectName("Data Structure and Algorithmic Problem Solving");
        newSub2.setSubjectType("Tutorial");
        newSub2.setSubjectDescription("This course introduces various types of data structures and algorithms used in problem solving. It\n" +
                "starts with the introduction to some topics which are used in data structures such as problemsolving techniques, C++ structure type, pointers and recursion. This is followed by detailed\n" +
                "presentation of a few data structures such as linked lists, stacks, queues, trees and graphs. Sorting\n" +
                "algorithms such as bubble sort, selection sort, insertion sort, merge sort and quick sort will be\n" +
                "introduced next. Then the searching algorithms such as linear search, binary search. Lastly, hash\n" +
                "table will be explained.");
        SubjectClassModel newSub3 = new SubjectClassModel();
        newSub3.setSubjectCode("UCCD1133");
        newSub3.setSubjectName("INTRODUCTION TO COMPUTER ORGANISATION\n" +
                "AND ARCHITECTURE ");
        newSub3.setSubjectType("Tutorial");
        newSub3.setSubjectDescription("This unit examines the related computer components that build the computer architectural\n" +
                "system in depth. It elaborates on how various digital logic blocks being organised and\n" +
                "integrated into simple computer system, based on the fundamental of digital logic theories.\n" +
                "The unit also elaborate on how high level program being translated into low level assembly\n" +
                "program, to be processed by MIPS processor architecture, by providing understanding on\n" +
                "compiler, assembler, linker, loader, and Instruction Set Architecture. Lastly, the unit\n" +
                "progresses by studying typical few examples of computer bus system and Input/Output\n" +
                "peripherals operations and configurations. Each exemplar application is chosen with a view to\n" +
                "illustrating sub-components and configurations arising during building computer architectural\n" +
                "system.");
        SubjectClassModel newSub4 = new SubjectClassModel();
        newSub4.setSubjectCode("UCCM1363");
        newSub4.setSubjectName("Discrete Mathematics");
        newSub4.setSubjectType("Lecture");
        newSub4.setSubjectDescription("Synopsis: This course will firstly introduce logic of compound statements and logic of\n" +
                "quantified statements, followed by the discussions on several important topics such as\n" +
                "mathematical induction, series and recursive relations, relations and digraphs, order relations\n" +
                "and structures, and trees. \n");
        SubjectClassModel newSub5 = new SubjectClassModel();
        newSub5.setSubjectCode("UCCD2003");
        newSub5.setSubjectName("Object-Oriented Systems Analysis and Design ");
        newSub5.setSubjectType("Lecture");
        newSub5.setSubjectDescription("This course will introduce the fundamentals and requirements of adopting the object-oriented\n" +
                "approach in systems analysis and design. It starts off with comparing the object-oriented approach\n" +
                "with other forms of development approach. This course also entails the discussion of\n" +
                "characteristics of the object-oriented approach, steps involve in performing system analysis and\n" +
                "design, the various views of (functional, structural and behavioral) of object-oriented systems and\n" +
                "modeling of the various views using the unified modeling language (UML). ");
        SubjectClassModel newSub6 = new SubjectClassModel();
        newSub6.setSubjectCode("UCCD2203");
        newSub6.setSubjectName("Database Systems");
        newSub6.setSubjectType("Tutorial");
        newSub6.setSubjectDescription("The course covers database conceptual modelling, various aspects of relational model such\n" +
                "as constraints, languages, and design, database design theory and methodology, data\n" +
                "storage and indexing issues, transaction processing concepts, distributed database. ");
        SubjectClassModel newSub7 = new SubjectClassModel();
        newSub7.setSubjectCode("UCCD2223");
        newSub7.setSubjectName("Website Design and Development ");
        newSub7.setSubjectType("Lecture");
        newSub7.setSubjectDescription("Synopsis: This subject introduces students to the development of interactive and dynamic web\n" +
                "sites with audio and multimedia incorporated. Students are also introduced to the server-side\n" +
                "web application development. This subject is designed to equip students with practical\n" +
                "knowledge and skills that are required by web developers. Students are required to undertake\n" +
                "hands-on web programming. \n");

        documentReference.set(newSub);
        documentReference2.set(newSub2);
        documentReference3.set(newSub3);
        documentReference4.set(newSub4);
        documentReference5.set(newSub5);
        documentReference6.set(newSub6);
        documentReference7.set(newSub7);


    }

    private void adddata3() {

        DocumentReference documentReference = fStore.collection("Subjects").document();
        DocumentReference documentReference2 = fStore.collection("Subjects").document();
        DocumentReference documentReference3 = fStore.collection("Subjects").document();
        DocumentReference documentReference4 = fStore.collection("Subjects").document();
        DocumentReference documentReference5 = fStore.collection("Subjects").document();
        DocumentReference documentReference6 = fStore.collection("Subjects").document();
        DocumentReference documentReference7 = fStore.collection("Subjects").document();
        SubjectClassModel newSub = new SubjectClassModel();
        newSub.setSubjectCode("UCCN2243");
        newSub.setSubjectName("Internetworking Principles and Practices");
        newSub.setSubjectType("Lecture");
        newSub.setSubjectDescription("This unit provides students with an understanding of the implementation of TCP/IP protocol\n" +
                "suite, and the protocols implemented in each of the protocol suite layer. Students are taught to\n" +
                "apply various design techniques in both IPv4 and IPv6 networks. Students also learn how to\n" +
                "inspect network traffics and troubleshoot certain network problems by using packet capturing\n" +
                "tools with protocol analysis. IPv6 and its migration issues from IPv4 are also discussed. ");
        SubjectClassModel newSub2 = new SubjectClassModel();
        newSub2.setSubjectCode("UCCD2502");
        newSub2.setSubjectName("Introduction to Inventive Problem Solving and\n" +
                "Proposal Writing ");
        newSub2.setSubjectType("Tutorial");
        newSub2.setSubjectDescription(
                "This course aims at building students’ skills and abilities to formulate and propose innovative\n" +
                        "projects. The materials are delivered through lecture and tutorial sessions. In the lecture\n" +
                        "sessions, students are taught various techniques for solving inventive problems and\n" +
                        "writing/presenting proposals. The workshop-type tutorial sessions, on the other hand, shall\n" +
                        "provide students with case study and group discussion to apply the techniques learnt. At the\n" +
                        "end of the unit course, students are required to write and present a proposal for a research\n" +
                        "and and/or development project in the areas of ICT.\n"
        );
        SubjectClassModel newSub3 = new SubjectClassModel();
        newSub3.setSubjectCode("UCCD1133");
        newSub3.setSubjectName("Introduction to Inventive Problem Solving and\n" +
                "Proposal Writing ");
        newSub3.setSubjectType("Tutorial");
        newSub3.setSubjectDescription("This course aims at building students’ skills and abilities to formulate and propose innovative\n" +
                "projects. The materials are delivered through lecture and tutorial sessions. In the lecture\n" +
                "sessions, students are taught various techniques for solving inventive problems and\n" +
                "writing/presenting proposals. The workshop-type tutorial sessions, on the other hand, shall\n" +
                "provide students with case study and group discussion to apply the techniques learnt. At the\n" +
                "end of the unit course, students are required to write and present a proposal for a research\n" +
                "and and/or development project in the areas of ICT.\n");
        SubjectClassModel newSub4 = new SubjectClassModel();
        newSub4.setSubjectCode("UCCM1363");
        newSub4.setSubjectName("Discrete Mathematics");
        newSub4.setSubjectType("Lecture");
        newSub4.setSubjectDescription("Synopsis: This course will firstly introduce logic of compound statements and logic of\n" +
                "quantified statements, followed by the discussions on several important topics such as\n" +
                "mathematical induction, series and recursive relations, relations and digraphs, order relations\n" +
                "and structures, and trees. \n");
        SubjectClassModel newSub5 = new SubjectClassModel();
        newSub5.setSubjectCode("UCCD2513");
        newSub5.setSubjectName("Mini Project ");
        newSub5.setSubjectType("Lecture");
        newSub5.setSubjectDescription("Synopsis: The Mini Project course covers a system development process in developing a project.\n" +
                "Students are grouped into teams in accordance to their discipline of study, where each team shall be\n" +
                "assigned with a relevant project title. Students shall undertake the project under the close supervision of\n" +
                "qualified supervisor in which the students are assessed based on their individual (60%) and team (40%)\n" +
                "performance in various aspects such as problem understanding and solving capability, technical\n" +
                "capability, effort, team participation, milestone achievement, presentation, report writing, innovativeness\n" +
                "and attitude. The project teams are expected to design possible solutions to problems, taking into\n" +
                "account various aspects such as time, cost and quality.");
        SubjectClassModel newSub6 = new SubjectClassModel();
        newSub6.setSubjectCode("UCCN1213");
        newSub6.setSubjectName("Fundamentals of Computer and Information Security");
        newSub6.setSubjectType("Tutorial");
        newSub6.setSubjectDescription("This unit covers the fundamental concepts of computer and information security. Students are\n" +
                "taught the fundamental of security best practices, laws and standards that will enable students\n" +
                "to build a secured environment. Recent advances in security tools and platform-specific\n" +
                "defences will be covered. ");
        SubjectClassModel newSub7 = new SubjectClassModel();
        newSub7.setSubjectCode("UCCD2213");
        newSub7.setSubjectName("SOFTWARE ENGINEERING PRINCIPLES");
        newSub7.setSubjectType("Lecture");
        newSub7.setSubjectDescription("Introduction to Software Engineering, Software Design, Software Testing, Software\n" +
                "Maintenance, and Software Management. ");

        documentReference.set(newSub);
        documentReference2.set(newSub2);
        documentReference3.set(newSub3);
        documentReference4.set(newSub4);
        documentReference5.set(newSub5);
        documentReference6.set(newSub6);
        documentReference7.set(newSub7);


    }

    private void adddata4() {

        DocumentReference documentReference = fStore.collection("Subjects").document();
        DocumentReference documentReference2 = fStore.collection("Subjects").document();
        DocumentReference documentReference3 = fStore.collection("Subjects").document();
        DocumentReference documentReference4 = fStore.collection("Subjects").document();
        DocumentReference documentReference5 = fStore.collection("Subjects").document();
        DocumentReference documentReference6 = fStore.collection("Subjects").document();
        SubjectClassModel newSub = new SubjectClassModel();
        newSub.setSubjectCode("UCCD3583");
        newSub.setSubjectName(" Project I ");
        newSub.setSubjectType("Lecture");
        newSub.setSubjectDescription("Synopsis: The final year project 1 provides students with the opportunity to bring together their\n" +
                "knowledge, experience and interest to produce a single piece of work, and to demonstrate\n" +
                "their competency in applying what they have learned throughout their studies. This course\n" +
                "aims to introduce the recent trends of ICT to the students, and to guide them through the\n" +
                "basics of project development. The students will also learn various skills such as problem\n" +
                "formulation, development-tool selection, research method identification, project planning,\n" +
                "proposal writing, presentation, in order to commence their final year projects.");
        SubjectClassModel newSub2 = new SubjectClassModel();
        newSub2.setSubjectCode("UCCD3033");
        newSub2.setSubjectName("Technopreneurship");
        newSub2.setSubjectType("Tutorial");
        newSub2.setSubjectDescription("Synopsis: Technopreneurship offers individuals the opportunity to establish and control\n" +
                "their own business enterprises and profits. This course offers students the insight and\n" +
                "knowledge that are necessary to explore and propose new business ventures. \n");
        SubjectClassModel newSub3 = new SubjectClassModel();
        newSub3.setSubjectCode("UCCD3053");
        newSub3.setSubjectName(" Information Technology Professional Ethics ");
        newSub3.setSubjectType("Tutorial");
        newSub3.setSubjectDescription("The subject is addressing legal, ethical and social issues of information and communication\n" +
                "technology. From the legal perspectives student will be introduced with basic concept of law\n" +
                "and its purpose. Student will explore the various forms of protection for Intellectual properties\n" +
                "namely copyright, patents, trademarks and others. Controversial legal issues and Computer\n" +
                "Crime topics will be introduced as well. This subject will also cover ethical issues for IT\n" +
                "professionals with the reference Page 2 of 3 to the International Computer societies (IEEE and\n" +
                "ACM) codes of conducts, Social issues in IT workplace as well as issues of privacy and\n" +
                "content controls.");
        SubjectClassModel newSub4 = new SubjectClassModel();
        newSub4.setSubjectCode("UCCD3223");
        newSub4.setSubjectName("Mobile Applications Development ");
        newSub4.setSubjectType("Lecture");
        newSub4.setSubjectDescription("Mobile devices need to interact with other devices and systems; such wireless devices require\n" +
                "well designed and coded programs to provide useful services. Current mobile platform will be\n" +
                "used as a development environment in this course. The course will impart to students\n" +
                "knowledge that includes wireless and mobile application logic, design and development;\n" +
                "wireless services; communication and memory management, wireless and mobile security.\n" +
                "The subject is designed to equip students with sufficient knowledge and skills required by\n" +
                "competent programmers in object-oriented programming in building mobile applications.\n" +
                "Students will undertake hands-on programming practical throughout the course of study. \n");
        SubjectClassModel newSub5 = new SubjectClassModel();
        newSub5.setSubjectCode("UCCD2513");
        newSub5.setSubjectName("Mini Project ");
        newSub5.setSubjectType("Lecture");
        newSub5.setSubjectDescription("Synopsis: The Mini Project course covers a system development process in developing a project.\n" +
                "Students are grouped into teams in accordance to their discipline of study, where each team shall be\n" +
                "assigned with a relevant project title. Students shall undertake the project under the close supervision of\n" +
                "qualified supervisor in which the students are assessed based on their individual (60%) and team (40%)\n" +
                "performance in various aspects such as problem understanding and solving capability, technical\n" +
                "capability, effort, team participation, milestone achievement, presentation, report writing, innovativeness\n" +
                "and attitude. The project teams are expected to design possible solutions to problems, taking into\n" +
                "account various aspects such as time, cost and quality.");
        SubjectClassModel newSub6 = new SubjectClassModel();
        newSub6.setSubjectCode("UCCD3596");
        newSub6.setSubjectName("Project II ");
        newSub6.setSubjectType("Tutorial");
        newSub6.setSubjectDescription("The final year project encourages students to bring together their discoveries as a single piece\n" +
                "of work and to demonstrate their competency in applying what they have learnt throughout their\n" +
                "studies. Students are encouraged to innovate, research and test their hypothesis, and present\n" +
                "in their work, their knowledge and expertise in disciplines other than what they have learnt. The\n" +
                "project work presents students the opportunity to advance their ideas, and pursue their interests\n" +
                "to postgraduate research problem formulation, development-tool selection, research method\n" +
                "identification, project planning, proposal writing, presentation, in order to commence their final\n" +
                "year projects. ");

        documentReference.set(newSub);
        documentReference2.set(newSub2);
        documentReference3.set(newSub3);
        documentReference4.set(newSub4);
        documentReference5.set(newSub5);
        documentReference6.set(newSub6);



    }

    private void commentReview() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                getApplicationContext(), R.style.BottomSheetDialogTheme
        );
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(
                R.layout.layout_bottom_sheet,
                findViewById(R.id.bottomSheetContainer)
        );
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();

    }


}