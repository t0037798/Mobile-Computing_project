package com.example.mark.disasterguard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.content.Intent;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    private ImageButton imgBtnKnowledge;
    private ImageButton imgBtnFindHospital;
    private ImageButton imgBtnFirstAidKit;
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        imgBtnFindHospital = (ImageButton)findViewById(R.id.imageButton1);
        imgBtnKnowledge = (ImageButton)findViewById(R.id.imageButton2);
        imgBtnFirstAidKit = (ImageButton)findViewById(R.id.imageButton3);
        adView = findViewById(R.id.ad_view);

        imgBtnKnowledge.setOnClickListener(imgBtnListener);
        imgBtnFindHospital.setOnClickListener(imgBtnListener);
        imgBtnFirstAidKit.setOnClickListener(imgBtnListener);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }

    private ImageButton.OnClickListener imgBtnListener =
        new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                imgBtnKnowledge.setClickable(false);
                imgBtnFindHospital.setClickable(false);
                imgBtnFirstAidKit.setClickable(false);

                switch(v.getId()) {
                    case R.id.imageButton1:
                        intent.setClass(MainActivity.this, MapsActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.imageButton2:
                        intent.setClass(MainActivity.this, KnowledgeActivity.class);
                        startActivity(intent);
                        break;
                    case R.id.imageButton3:
                        intent.setClass(MainActivity.this, TraumaTreatmentActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        };

    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
        imgBtnKnowledge.setClickable(true);
        imgBtnFindHospital.setClickable(true);
        imgBtnFirstAidKit.setClickable(true);
    }

    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }
}
