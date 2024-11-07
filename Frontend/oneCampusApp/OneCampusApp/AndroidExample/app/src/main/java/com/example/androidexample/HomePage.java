package com.example.androidexample;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        // Set up the toolbar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        // Set up the drawer layout and toggle button for opening/closing
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Set up navigation view
        NavigationView navigationView = findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_profile) {
                Intent profileIntent = new Intent(HomePage.this, ProfileActivity.class); // Replace with actual activity
                startActivity(profileIntent);
            } else if (id == R.id.nav_chat) {
                Intent chatIntent = new Intent(HomePage.this, ChatActivity.class); // Replace with actual activity
                startActivity(chatIntent);
            } else if (id == R.id.nav_settings) {
                Intent chatIntent = new Intent(HomePage.this, SettingsActivity.class); // Replace with actual activity
                startActivity(chatIntent);
            } else if (id == R.id.nav_logout) {
                finish();
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu_nav.xml
        getMenuInflater().inflate(R.menu.menu_nav, menu);
        return true;
    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.nav_profile:
//                // Handle profile action
//                // Navigate to profile page (you can create a ProfileActivity later)
//                Intent profileIntent = new Intent(HomePage.this, ChatActivity.class);
//                startActivity(profileIntent);
//                break;
//            case R.id.nav_chat:
//                // Navigate to chat page
//                Intent chatIntent = new Intent(HomePage.this, ChatActivity.class);
//                startActivity(chatIntent);
//                break;
//            case R.id.nav_logout:
//                // Handle logout
//                // Add your logout logic here
//                finish();
//                break;
//        }
//        // Close the drawer after selection
//        drawerLayout.closeDrawer(GravityCompat.START);
//        return true;
//    }


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
