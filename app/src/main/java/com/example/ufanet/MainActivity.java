package com.example.ufanet;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.example.ufanet.home.homeFragments;

public class MainActivity extends AppCompatActivity{

    homeFragments homeFragments = new homeFragments();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(homeFragments);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}
