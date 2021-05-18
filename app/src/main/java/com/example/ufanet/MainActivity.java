package com.example.ufanet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.ufanet.home.PermActivity;
import com.example.ufanet.home.homeFragments;

public class MainActivity extends AppCompatActivity{

    homeFragments homeFragments = new homeFragments();
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadFragment(homeFragments);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Уфанет");
        toolbar.setTitleTextAppearance(this, R.style.RobotoBoldTextAppearance);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_setting) {
            Intent intent = new Intent(this, PermActivity.class);
            intent.putExtra("title", "Bluetooth");
            intent.putExtra("id_image", R.drawable.ic_bluetooth_b);
            intent.putExtra("id", 0);
            intent.putExtra("post_title", "Bluetooth нам нужен для поиска устройства" +
                    " и для активации функции администрирования");
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
}
