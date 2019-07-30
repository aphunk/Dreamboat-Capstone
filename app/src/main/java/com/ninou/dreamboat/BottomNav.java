package com.ninou.dreamboat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
public class BottomNav extends AppCompatActivity {
    //    private TextView mTextMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                        Intent a = new Intent(BottomNav.this, JournalListActivity.class);
                        startActivity(a);
                        break;
                    case R.id.navigation_dashboard:
                        Intent b = new Intent(BottomNav.this, PostJournalActivity.class);
                        startActivity(b);
                        break;
                    case R.id.navigation_notifications:
                        Intent c = new Intent(BottomNav.this, LoginActivity.class);
                        startActivity(c);
                        break;
                }
                return false;
            }
        });
    }
}