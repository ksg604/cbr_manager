package com.example.cbr_manager.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cbr_manager.R;
import com.example.cbr_manager.ui.allclients.AllClientsActivity;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    List<ViewPagerModel> models;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        setupAllClientsButton();
        setupAddClientButton();
        // Test data
        models = new ArrayList<>();
        models.add(new ViewPagerModel(R.drawable.dog, "Peter Tran", "Simon Fraser University") );
        models.add(new ViewPagerModel(R.drawable.dog, "Paul Erdos", "Budapest, Germany") );
        models.add(new ViewPagerModel(R.drawable.dog, "Leonhard Euler", "Switzerland") );
        models.add(new ViewPagerModel(R.drawable.dog, "James Stewart", "Simon Fraser University") );
        models.add(new ViewPagerModel(R.drawable.dog, "Demo Boi", "Simon Fraser University") );

        setupViewPager();
    }

    private void setupAddClientButton() {
        Button button = (Button) findViewById(R.id.addClientButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashboardActivity.this, "Going to add client activity.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupAllClientsButton() {
        Button button = (Button) findViewById(R.id.allClientsButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), AllClientsActivity.class);
                startActivity(startIntent);
            }
        });
    }

    private void setupViewPager() {
        adapter = new ViewPagerAdapter(models, this);
        viewPager = findViewById(R.id.viewPager2);
        viewPager.setAdapter(adapter);
        viewPager.setClipToPadding(false);
        viewPager.setPadding(220,0,220,0);
    }
}