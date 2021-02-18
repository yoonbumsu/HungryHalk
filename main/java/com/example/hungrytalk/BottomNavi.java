package com.example.hungrytalk;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Fragment.ChatFragment;
import Fragment.NoteFragment;
import Fragment.OptionFragment;

import Fragment.ProfileFragment;

public class BottomNavi extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;//바텀내비게이션 뷰
    private FragmentManager fm;
    private FragmentTransaction ft;
    private ProfileFragment profileFragment;
    private NoteFragment locationFragment;
    private ChatFragment chatFragment;
    private OptionFragment optionFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new ProfileFragment()).commit();
        bottomNavigationView  = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
               switch(menuItem.getItemId()){
                   case R.id.action_profile:
                       setFrag(0);
                       getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new ProfileFragment()).commit();
                       break;
                   case R.id.action_chat:
                       setFrag(1);
                       getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,new ChatFragment()).commit();
                       break;
                   case R.id.action_note:
                       setFrag(2);
                       break;
                   case R.id.action_option:
                       setFrag(3);
                       break;
               }

                return true;
            }
        });

        profileFragment = new ProfileFragment();
        locationFragment = new NoteFragment();
        optionFragment = new OptionFragment();
        chatFragment = new ChatFragment();
        setFrag(0); //첫 프래그먼트 무엇으로 지정해줄것인지
    }
    // 프래그먼트 교체 일어나는 실행문
    private  void setFrag(int n){
        fm =getSupportFragmentManager();
        ft =fm.beginTransaction();
        switch (n){
            case 0:
                ft.replace(R.id.main_frame, profileFragment);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame,chatFragment);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, locationFragment);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.main_frame, optionFragment);
                ft.commit();
                break;
        }
    }
}
