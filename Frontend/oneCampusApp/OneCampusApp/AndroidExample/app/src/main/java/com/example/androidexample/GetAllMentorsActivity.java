package com.example.androidexample;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class GetAllMentorsActivity extends AppCompatActivity {

    private ListView mentorListView;
    private ArrayAdapter<String> mentorAdapter;
    private ArrayList<String> mentorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_mentors);

        mentorListView = findViewById(R.id.mentorListView);
        mentorList = new ArrayList<>();

        mentorAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mentorList);
        mentorListView.setAdapter(mentorAdapter);

        fetchMentorsFromDatabase();
    }

    private void fetchMentorsFromDatabase() {
        Log.d("GetAllMentorsActivity", "Fetching mentors...");
        String url = "http://10.90.74.238:8080/mentor/all";

        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("GetAllMentorsActivity", "Response received: " + response.toString());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject mentor = response.getJSONObject(i);

                                // Extracting mentor details
                                JSONObject user = mentor.getJSONObject("user");
                                String username = user.getString("username");
                                String major = mentor.getString("major");
                                String classification = mentor.getString("classification");

                                // Combine the information into a string for display
                                String mentorInfo = "Username: " + username + ", Major: " + major + ", Classification: " + classification;

                                // Add to the list
                                mentorList.add(mentorInfo);
                            }
                            mentorAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e("GetAllMentorsActivity", "Error parsing JSON: " + e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("GetAllMentorsActivity", "Failed to fetch mentors: " + error.getMessage());
                        Toast.makeText(GetAllMentorsActivity.this, "Failed to load mentors", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }


}
