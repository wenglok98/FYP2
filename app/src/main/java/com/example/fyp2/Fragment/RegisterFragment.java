package com.example.fyp2.Fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.fyp2.Activity.MainActivity;
import com.example.fyp2.BaseApp.AppManager;
import com.example.fyp2.Class.UsersClass;
import com.example.fyp2.R;
import com.example.fyp2.Utils.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.royrodriguez.transitionbutton.TransitionButton;
import com.unstoppable.submitbuttonview.SubmitButton;

public class RegisterFragment extends Fragment {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    private TextInputLayout usernameinputlayout, emailinputlayout, passwordinputlayout, cfm_passwordinputlayout;
    String userid = "";
    String password;
    String username;
    private SubmitButton submitButton;
    private Dialog mDialog;
    private LottieAnimationView lottieAnimationView;
    private TextInputEditText edt_username, edt_useremail, edt_password, edt_confirmpassword;
    String email;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_register, container, false);
        mDialog = new Dialog(getContext());
        mDialog.setContentView(R.layout.register_dialog);
        mDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        fAuth = FirebaseAuth.getInstance();

        //Find View BY ID
        lottieAnimationView = mDialog.findViewById(R.id.Viewlottie);
        submitButton = view.findViewById(R.id.submitbutton);
        edt_useremail = view.findViewById(R.id.edt_email);
        edt_username = view.findViewById(R.id.edt_username);
        edt_password = view.findViewById(R.id.edt_password);
        edt_confirmpassword = view.findViewById(R.id.edt_confirmpassword);
        usernameinputlayout = view.findViewById(R.id.usernametextinputlayout);
        emailinputlayout = view.findViewById(R.id.emailinputlayout);
        passwordinputlayout = view.findViewById(R.id.passwordinputlayout);
        cfm_passwordinputlayout = view.findViewById(R.id.cfm_password_inputlayout);


        //End of find view by id
        init();
        return view;
    }

    private void init() {
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkValidate();


            }
        });
    }

    private void checkValidate() {
        if (Util.editIsEmpty(edt_username)) {
            usernameinputlayout.setError("Please Enter Username");
        }
        if (Util.editIsEmpty(edt_useremail)) {
            emailinputlayout.setError("Please Enter Email");
        }
        if (Util.editIsEmpty(edt_password)) {
            passwordinputlayout.setError("Please Enter Password");
        }
        if (Util.editIsEmpty(edt_confirmpassword)) {
            cfm_passwordinputlayout.setError("Please Enter Confirm Password");
        }
        if (!Util.editIsEmpty(edt_username, edt_confirmpassword, edt_useremail, edt_password)) {
            if (!edt_password.getText().toString().trim().equals(edt_confirmpassword.getText().toString().trim())) {
                Snackbar.make(getView(), "Password and Confirm Password is different", BaseTransientBottomBar.LENGTH_SHORT).show();
            } else {
                //Init of loading
                mDialog.show();
                lottieAnimationView.playAnimation();
                lottieAnimationView.loop(true);
                lottieAnimationView.setMinAndMaxFrame(0, 46);
                //End of loading
                insertUser();
            }
        }
        submitButton.reset();

    }

    private void insertUser() {
        email = edt_useremail.getText().toString();
        password = edt_password.getText().toString();
        username = edt_username.getText().toString();

        fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    lottieAnimationView.setMaxFrame(94);
                    lottieAnimationView.loop(false);
                    submitButton.reset();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            mDialog.dismiss();
                        }
                    }, 1000);
                    userid = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("Users").document(userid);
                    UsersClass newReg = new UsersClass(username, email, userid);
                    documentReference.set(newReg);
                    AppManager.getAppManager().ToOtherActivity(MainActivity.class);
                    getActivity().finishAffinity();


                } else {
                    submitButton.reset();
                    mDialog.dismiss();
                    Snackbar.make(getView(), task.getException().getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        });
    }
}