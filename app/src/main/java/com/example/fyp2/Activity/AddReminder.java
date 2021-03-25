package com.example.fyp2.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.fyp2.BaseApp.AppManager;
import com.example.fyp2.BaseApp.BaseActivity;
import com.example.fyp2.Class.EnrollClass;
import com.example.fyp2.EnrolledSubjectAdapter;
import com.example.fyp2.R;
import com.example.fyp2.Utils.SharedPreferenceUtil;
import com.example.fyp2.Utils.SnackUtil;
import com.example.fyp2.databinding.ActivityAddReminderBinding;
import com.example.fyp2.databinding.ActivityMainSubjectBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class AddReminder extends BaseActivity {
    ActivityAddReminderBinding activityMainSubjectBinding;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    ArrayList<String> SubjectData = new ArrayList<>();
    ArrayList<String> ReminderType = new ArrayList<>();
    Integer finalYear = 0;
    Integer finalMonth =0;
    Integer finalDay = 0;
    Integer finalHour = 0;
    Integer finalMinute = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_reminder);
        activityMainSubjectBinding = ActivityAddReminderBinding.inflate(getLayoutInflater());
        View view = activityMainSubjectBinding.getRoot();
        setContentView(view);
        initAppTitle();
        initData();
        initType();

//        insertToCalendar();
//        Calendar beginTime = Calendar.getInstance();
//        beginTime.set(2021, 3, 26, 7, 30);
//
//        Calendar endTime = Calendar.getInstance();
//        endTime.set(2021, 3, 27, 8, 30);
//
//        Intent intent = new Intent(Intent.ACTION_INSERT)
//                .setData(CalendarContract.Events.CONTENT_URI)
//                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
//                        beginTime.getTimeInMillis())
//                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
//                        endTime.getTimeInMillis())
//                .putExtra(CalendarContract.Events.TITLE, "Yoga")
//                .putExtra(CalendarContract.Events.DESCRIPTION, "Group class")
//                .putExtra(CalendarContract.Events.EVENT_LOCATION, "The gym")
//                .putExtra(CalendarContract.Events.AVAILABILITY, CalendarContract.Events.AVAILABILITY_BUSY)
//                .putExtra(Intent.EXTRA_EMAIL, "rowan@example.com,trevor@example.com");
//
//        startActivity(intent);

        activityMainSubjectBinding.addReminderBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
    }

    private void insertToCalendar() {
        long calID = 3;
        long startMillis = 0;
        long endMillis = 0;
        Calendar beginTime = Calendar.getInstance();
        beginTime.set(finalYear, finalMonth, finalDay, finalHour, finalMinute);
        startMillis = beginTime.getTimeInMillis();
        Calendar endTime = Calendar.getInstance();
        endTime.set(finalYear, finalMonth, finalDay, finalHour+2, finalMinute);
        endMillis = endTime.getTimeInMillis();


        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);

        values.put(CalendarContract.Events.TITLE, activityMainSubjectBinding.addReminderSubjectCodeACTV.getText().toString());
        values.put(CalendarContract.Events.DESCRIPTION, activityMainSubjectBinding.addReminderTypeACTV.getText().toString());
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "GMT+8");
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);

// get the event ID that is the last element in the Uri
        long eventID = Long.parseLong(uri.getLastPathSegment());

        SnackUtil.show(getApplication(),"Reminder Successfully Added !");

        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

     onBackPressed();
//                        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
//                        SplashScreenActivity.this.startActivity(intent);
//                        finish();
            }
        }, 1000);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }


    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            ((AddReminder) getActivity()).setTimeText(String.valueOf(hourOfDay), String.valueOf(minute));
            ((AddReminder) getActivity()).setTime(hourOfDay, minute);


            // Do something with the time chosen by the user
        }

    }

    public void setTimeText(String Hour, String Minute) {
        activityMainSubjectBinding.timePickerBT.setText(Hour + " : " + Minute);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            ((AddReminder) getActivity()).setDateText(String.valueOf(year), String.valueOf(month), String.valueOf(day));
            ((AddReminder) getActivity()).setDate(year, month, day);

            // Do something with the date chosen by the user
        }
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void setDateText(String Year, String Month, String Day) {
        activityMainSubjectBinding.datePickerBT.setText(Year + "/" + Month + "/" + Day);
    }

    private void initAppTitle() {
        ((TextView) findViewById(R.id.app_title_tv)).setText("Set a Reminder");
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


    private void initData() {
        String UID = SharedPreferenceUtil.getFromPrefs(getApplicationContext(), "UID", "");
        Task<QuerySnapshot> docRef = fStore.collection("Enrollment").whereEqualTo("studentID", UID).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                    EnrollClass enrollClass = new EnrollClass();
//                    enrollClass.setTimeStamp(documentSnapshot.get("timeStamp").toString());
//                    enrollClass.setSubjectCode(documentSnapshot.get("subjectCode").toString());
//                    enrollClass.setStudyMinutes(documentSnapshot.get("studyMinutes").toString());
//                    enrollClass.setStudentID(documentSnapshot.get("studentID").toString());
//                    enrollClass.setSubjectType(documentSnapshot.get("subjectType").toString());
                    SubjectData.add(documentSnapshot.get("subjectCode").toString());
                }
                ArrayAdapter<String> test = new ArrayAdapter<>(getApplication(), R.layout.dropdown_menu_popup_item, SubjectData);
                activityMainSubjectBinding.addReminderSubjectCodeACTV.setAdapter(test);
            }
        });


    }

    private void initType() {
        ReminderType.add("Test");
        ReminderType.add("Presentation");
        ReminderType.add("Quiz");
        ReminderType.add("Assignment Submission");

        ArrayAdapter<String> test = new ArrayAdapter<>(getApplication(), R.layout.dropdown_menu_popup_item, ReminderType);
        activityMainSubjectBinding.addReminderTypeACTV.setAdapter(test);

    }

    private void setDate(Integer year, Integer month, Integer day) {
        finalYear = year;
        finalMonth = month;
        finalDay = day;

    }

    private void setTime(Integer month, Integer day) {
        finalMinute = day;
        finalHour = month;


    }

    private void validation() {
        if (activityMainSubjectBinding.addReminderTypeACTV.getText().toString().equals("Type")) {
            SnackUtil.show(getApplication(), "Please Select a type");
        }
        if (activityMainSubjectBinding.addReminderSubjectCodeACTV.getText().toString().equals("Subject Code")) {
            SnackUtil.show(getApplication(), "Please Select a Subject");
        }
        if (finalYear == 0) {
            SnackUtil.show(getApplication(), "Please Select a Date");

        }
        if (finalHour == 0) {
            SnackUtil.show(getApplication(), "Please Select a Time");

        }
        if (!activityMainSubjectBinding.addReminderTypeACTV.getText().toString().equals("Type") &&
                !activityMainSubjectBinding.addReminderSubjectCodeACTV.getText().toString().equals("Subject Code") &&
                !finalYear.equals(0) &&
                !finalHour.equals(0)) {
            insertToCalendar();

        }





    }
}