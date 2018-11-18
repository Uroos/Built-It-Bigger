package com.example.mylibrary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

public class LibraryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library);
        String joke=getIntent().getStringExtra("joke");
        TextView tv = findViewById(R.id.textViewLibrary);
        if(joke!=null|| !TextUtils.isEmpty(joke)) {
            tv.setText(joke);
        }else{
            tv.setText("Error receiving from cloud.");
        }
    }
}
