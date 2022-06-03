package com.ab.rakshasutra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ab.rakshasutra.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

public class MainActivity extends AppCompatActivity {


    private Button button;
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecyclerViewActivity();

            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case  R.id.resource:
                        startActivity(new Intent(getApplicationContext()
                                ,activity_resource.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case  R.id.hotspot:
                        startActivity(new Intent(getApplicationContext()
                                ,HotspotActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case  R.id.news:
                        startActivity(new Intent(getApplicationContext()
                                ,News.class));
                        overridePendingTransition(0, 0);
                        return true;

                }
                return false;
            }
        });

    }

    public void RecyclerViewActivity(){
        Intent intent = new Intent(this, RecyclerView.class);
        startActivity(intent);
        finish();

    }

}
