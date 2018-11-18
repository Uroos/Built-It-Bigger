package com.udacity.gradle.builditbigger.free;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.mylibrary.LibraryActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.udacity.gradle.builditbigger.EndpointsAsyncTask;
import com.udacity.gradle.builditbigger.R;

import androidx.test.espresso.IdlingResource;

public class MainActivity extends AppCompatActivity implements EndpointsAsyncTask.OnUpdateListener{
    // The Idling Resource which will be null in production.
    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AdView mAdView = (AdView) findViewById(R.id.adView);
        // Create an ad request. Check logcat output for the hashed device ID to
        // get test ads on a physical device. e.g.
        // "Use AdRequest.Builder.addTestDevice("ABCDEF012345") to get test ads on this device."
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }

    public void tellMeAJoke(View view) {
            EndpointsAsyncTask currentTask = new EndpointsAsyncTask();
            currentTask.simpleIdlingResource=mIdlingResource;
            currentTask.setUpdateListener(this);
            currentTask.execute(new Pair<Context, String>(this, "Manfred"));
            //Toast.makeText(this, "Button working in Free final project!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdate(String result) {
        //Toast.makeText(this, "result is: " + result ,Toast.LENGTH_SHORT).show();
        Intent myintent=new Intent(this, LibraryActivity.class);
        myintent.putExtra("joke", result);
        startActivity(myintent);
    }
    /**
     * Only called from test, creates and returns a new {@link SimpleIdlingResource}.
     */
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }
}
