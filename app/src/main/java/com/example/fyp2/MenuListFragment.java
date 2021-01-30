package com.example.fyp2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fyp2.Activity.LoginActivity;
import com.example.fyp2.BaseApp.AppManager;
import com.example.fyp2.Utils.SharedPreferenceUtil;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class MenuListFragment extends Fragment {

    private ImageView ivMenuUserProfilePhoto;
    private TextView drawerNameView;
    FirebaseAuth fAuth;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fAuth = FirebaseAuth.getInstance();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container,
                false);
        NavigationView vNavigation = (NavigationView) view.findViewById(R.id.vNavigation);

        View parview = vNavigation.getHeaderView(0);
        drawerNameView = parview.findViewById(R.id.drawerNameView);
//        SharedPreferences sharedPreferences = PreferenceManager
//                .getDefaultSharedPreferences(getContext());
//        String name = sharedPreferences.getString("username", "");
      String name =  SharedPreferenceUtil.getFromPrefs(getContext(),"username","");
        drawerNameView.setText(name);
        ivMenuUserProfilePhoto = (ImageView) parview.findViewById(R.id.ivMenuUserProfilePhoto);
//        ivMenuUserProfilePhoto.setImageDrawable(getResources().getDrawable(R.mipmap.ic_launcher));

        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case (R.id.logout):
                        fAuth.getInstance().signOut();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finishAffinity();
                        break;


                }

                return false;
            }
        });
        setupHeader();
        return view;
    }

    private void setupHeader() {

//        ivMenuUserProfilePhoto.setImageResource(R.drawable.jisoo);
        int avatarSize = getResources().getDimensionPixelSize(R.dimen._64sdp);
        Picasso.with(getActivity())
                .load(R.drawable.jisoo)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(ivMenuUserProfilePhoto);

    }

}
