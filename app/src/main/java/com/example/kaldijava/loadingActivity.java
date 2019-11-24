package com.example.kaldijava;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;


public class loadingActivity extends Activity {
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        try{
            Thread.sleep(3000);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
