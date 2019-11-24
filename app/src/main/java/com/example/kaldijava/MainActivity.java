package com.example.kaldijava;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.NavigationView;
import android.content.pm.PackageManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBarDrawerToggle;

import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.Glide;

import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.content.Intent;
import android.speech.*;
import android.view.*;
import android.Manifest;
import android.widget.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextToSpeech tts;
    ViewFlipper v_fllipper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int images[] = {
                R.drawable.p1, R.drawable.p2,
                R.drawable.p3, R.drawable.p4,
                R.drawable.p5, R.drawable.p6,
                R.drawable.p7, R.drawable.p8,
                R.drawable.p9, R.drawable.p10,
                R.drawable.p11, R.drawable.p12,
                R.drawable.p13, R.drawable.p14,
                R.drawable.p15, R.drawable.p16,
                R.drawable.p17, R.drawable.p18,
                R.drawable.p19, R.drawable.p20
        };

        Random ran = new Random();
        int num = ran.nextInt(images.length);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        v_fllipper = findViewById(R.id.gif_main);
//        for(int image : images) {
//            fllipperImages(image);
//        }
        ImageView display = (ImageView) findViewById(R.id.gif_main);
        display.setBackgroundResource(images[num]);

//        ImageView display = (ImageView) findViewById(R.id.gif_main);
//        GlideDrawableImageViewTarget gifImage = new GlideDrawableImageViewTarget(display);
//        Glide.with(this).load(R.drawable.main).into(gifImage);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 5);
            Toast.makeText(getApplicationContext(), "음성인식 권한을 넘겨주세요!!", Toast.LENGTH_SHORT).show();
        }


        FloatingActionButton voice = findViewById(R.id.voice);
        final TextView txt = new TextView(this);
        voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { inputVoice(txt); }
        });
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                tts.setLanguage(Locale.KOREAN);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }
    public void fllipperImages(int image) {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundResource(image);

        v_fllipper.addView(imageView);      // 이미지 추가
        v_fllipper.setFlipInterval(4000);       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
        v_fllipper.setAutoStart(true);          // 자동 시작 유무 설정

        // animation
        v_fllipper.setInAnimation(this,android.R.anim.slide_in_left);
        v_fllipper.setOutAnimation(this,android.R.anim.slide_out_right);
    }
    public void inputVoice(final TextView txt) {
        try {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, this.getPackageName());
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");
            final SpeechRecognizer stt = SpeechRecognizer.createSpeechRecognizer(this);

            stt.setRecognitionListener(new RecognitionListener() {
                @Override
                public void onReadyForSpeech(Bundle params) {
                    Toast.makeText(getApplicationContext(), "찾고 싶은 커피나 카페를 말씀하세요.", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onBeginningOfSpeech() {
                }
                @Override
                public void onRmsChanged(float rmsdB) {
                }
                @Override
                public void onBufferReceived(byte[] buffer) {
                }
                @Override
                public void onEndOfSpeech() {
                    Toast.makeText(getApplicationContext(), "음성 입력 종료!", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onError(int error)
                {
                    Toast.makeText(getApplicationContext(), "오류 발생 :"+ error, Toast.LENGTH_SHORT).show();
                    stt.destroy();
                }
                @Override
                public void onResults(Bundle results) {
                    ArrayList<String> result = (ArrayList<String>) results.get(SpeechRecognizer.RESULTS_RECOGNITION);
                    //final EditText editText = (EditText) findViewById(R.id.feedEditText);   //먹이 입력 변수
                    replyAnswer(result.get(0), txt);
                    //editText.setText(result.get(0));
                    stt.destroy();
                }
                @Override
                public void onPartialResults(Bundle partialResults) {
                }
                @Override
                public void onEvent(int eventType, Bundle params) {
                }
            });
            stt.startListening(intent);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
    private void replyAnswer(String input, TextView txt){

        try{
            Toast.makeText(getApplicationContext(), input + "로 검색합니다.", Toast.LENGTH_SHORT).show();
            //tts.speak(input + "로 검색합니다.", TextToSpeech.QUEUE_FLUSH, null);
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_information) {
            Intent intent = new Intent(getApplicationContext(), activity_information.class);
            startActivity(intent);
        } else if (id == R.id.nav_coffee) {
            Intent inte = new Intent(this , com.example.kaldijava.cafe.activity.MainActivity.class);
            startActivity(inte);
            
        } else if (id == R.id.nav_coffeeShop) {

        } else if (id == R.id.nav_alarm) {

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
