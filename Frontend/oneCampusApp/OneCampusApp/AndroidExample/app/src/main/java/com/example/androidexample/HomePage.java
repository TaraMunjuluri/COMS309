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

/**
 * HomePage is the main activity that provides navigation to various sections of the app.
 * It features a navigation drawer with options to access different activities.
 */
public class HomePage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    /**
     * Called when the activity is created.
     * Sets up the toolbar, navigation drawer, and handles navigation item selections.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState().
     *                           Otherwise, it is null.
     */
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
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            // Handle navigation item selections
            if (id == R.id.nav_profile) {
                navigateToActivity(ProfileActivity.class);
            } else if (id == R.id.nav_match) {
                navigateToActivity(UserMatch.class);
            } else if (id == R.id.nav_chat) {
                navigateToActivity(ChatActivity.class);
            } else if (id == R.id.nav_settings) {
                navigateToActivity(SettingsActivity.class);
            } else if (id == R.id.nav_achievements) {
                navigateToActivity(AchievementsActivity.class);
            } else if (id == R.id.nav_logout) {
                navigateToActivity(LoginActivity.class);
            }

            // Close the navigation drawer
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    /**
     * Inflates the options menu.
     *
     * @param menu The menu object to inflate.
     * @return True if the menu is successfully created.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_nav, menu);
        return true;
    }

    /**
     * Handles the back button press.
     * If the navigation drawer is open, it closes it. Otherwise, the default back press behavior is executed.
     */
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Placeholder for handling navigation item selections.
     * This method is overridden to satisfy the NavigationView.OnNavigationItemSelectedListener interface.
     *
     * @param item The selected menu item.
     * @return False since this implementation is overridden by the lambda in setNavigationItemSelectedListener.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    /**
     * Navigates to the specified activity.
     *
     * @param activityClass The class of the activity to navigate to.
     */
    private void navigateToActivity(Class<?> activityClass) {
        Intent intent = new Intent(HomePage.this, activityClass);
        startActivity(intent);
    }
}
