package com.ab.rakshasutra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.rakshasutra.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class News extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Window w = getWindow();

        TextView tv = (TextView) findViewById(R.id.faq_covid);
        TextView tv1 = (TextView) findViewById(R.id.faq_about);

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(News.this, faq_covid.class);
                startActivity(intent);

            }
        });

        tv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(News.this, faq_about.class);
                startActivity(intent);

            }
        });







        w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.news);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext()
                                , MainActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.hotspot:
                        startActivity(new Intent(getApplicationContext()
                                , HotspotActivity.class));
                        Toast.makeText(News.this, "Hotspots", Toast.LENGTH_SHORT).show();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.resource:
                        startActivity(new Intent(getApplicationContext()
                                , activity_resource.class));
                        Toast.makeText(News.this, "Resources", Toast.LENGTH_SHORT).show();
                        overridePendingTransition(0, 0);
                        return true;

                }
                Toast.makeText(News.this, "News", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }
}