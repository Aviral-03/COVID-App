package com.ab.rakshasutra;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ab.rakshasutra.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class activity_resource extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);
        Window w = getWindow();
        w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //w.setStatusBarColor(ContextCompat.getColor(activity_resource.this,R.color.statusbar));


        ImageButton btn1 = (ImageButton) findViewById(R.id.amazon_intent);
        ImageButton btn2 = (ImageButton) findViewById(R.id.pdf_intent);
        ImageButton btn3 = (ImageButton) findViewById(R.id.prevention1);
        ImageButton btn4 = (ImageButton) findViewById(R.id.prevention2);
        ImageButton btn5 = (ImageButton) findViewById(R.id.protect_measure);

        TextView tv = (TextView) findViewById(R.id.aarogya);



        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=nic.goi.aarogyasetu"));
                startActivity(intent);


            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.amazon.in/s?i=specialty-aps&srs=21433333031&pd_rd_r=04998b7b-4316-4a91-becd-35cdc81cf8fe&pd_rd_w=zXztN&pd_rd_wg=HFWlY&pf_rd_p=1007035c-e763-4d4a-b652-155673e1fd32&pf_rd_r=HT3P2QW1FDZ8NSTKP4S1"));
                startActivity(intent);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.mohfw.gov.in/pdf/Poster_Corona_ad_Eng.pdf"));
                startActivity(intent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.who.int/docs/default-source/epi-win/how-to-use-mask-v0-1-print.pdf?sfvrsn=64ba1493_2"));
                startActivity(intent);
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.who.int/gpsc/5may/How_To_HandWash_Poster.pdf?ua=1"));
                startActivity(intent);
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                        Uri.parse("https://www.mohfw.gov.in/pdf/ProtectivemeasuresEng.pdf"));
                startActivity(intent);
            }
        });


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setSelectedItemId(R.id.resource);

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
                        Toast.makeText(activity_resource.this, "Hotspots", Toast.LENGTH_SHORT).show();
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.news:
                        startActivity(new Intent(getApplicationContext()
                                , News.class));
                        Toast.makeText(activity_resource.this, "News", Toast.LENGTH_SHORT).show();
                        overridePendingTransition(0, 0);
                        return true;

                }
                Toast.makeText(activity_resource.this, "Resources", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

    }
}
