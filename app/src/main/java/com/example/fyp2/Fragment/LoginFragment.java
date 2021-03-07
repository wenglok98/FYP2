package com.example.fyp2.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fyp2.Activity.MainActivity;
import com.example.fyp2.R;
import com.example.fyp2.Utils.SharedPreferenceUtil;
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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.royrodriguez.transitionbutton.TransitionButton;

public class LoginFragment extends Fragment {
    private TransitionButton transitionButton;
    private FirebaseAuth fAuth;
    private TextInputEditText username, password;
    private TextInputLayout usernameLayout, passwordLayout;
    String UID;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        LayoutInflater lf = getActivity().getLayoutInflater();
        View view = lf.inflate(R.layout.fragment_login, container, false);
        fAuth = FirebaseAuth.getInstance();


        //Re-enabled

        transitionButton = view.findViewById(R.id.transition_button);
        username = view.findViewById(R.id.edt_username);
        password = view.findViewById(R.id.edt_password);
        usernameLayout = view.findViewById(R.id.usernametextinputlayout);
        passwordLayout = view.findViewById(R.id.passwordlayout);


        init();
        return view;
    }

    private void init() {
        transitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidate();

            }
        });
    }

    private void checkValidate() {
        if (Util.editIsEmpty(username)) {
            usernameLayout.setError("Please Enter Email");
        }
        if (Util.editIsEmpty(password)) {
            passwordLayout.setError("Please Enter Password");
        }

        if (!Util.editIsEmpty(username, password)) {
            transitionButton.startAnimation();

            login();
        }
    }

    private void login() {
        String email = username.getText().toString();
        String passwords = password.getText().toString();
        fAuth.signInWithEmailAndPassword(email, passwords).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    UID = fAuth.getCurrentUser().getUid();
                    retrievename();
                    transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.EXPAND, new TransitionButton.OnAnimationStopEndListener() {
                        @Override
                        public void onAnimationStopEnd() {

                            Intent intent = new Intent(getContext(), MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(intent);
                        }
                    });

                }
                else
                {
                    transitionButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null);
                    Snackbar.make(getView(),task.getException().getMessage(), BaseTransientBottomBar.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void retrievename() {
        DocumentReference documentReference = fStore.collection("Users").document(UID);
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                String name = task.getResult().getString("username").toString();
                SharedPreferenceUtil.saveToPrefs(getContext(), "username", name);

            }
        });

    }
}