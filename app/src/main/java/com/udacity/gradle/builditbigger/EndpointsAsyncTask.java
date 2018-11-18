package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.udacity.gradle.builditbigger.backend.myApi.MyApi;
import com.udacity.gradle.builditbigger.free.SimpleIdlingResource;

import java.io.IOException;

import javax.annotation.Nullable;

public class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private static MyApi myApiService = null;
    private Context context;
    OnUpdateListener listener;
    @Nullable
    public SimpleIdlingResource simpleIdlingResource;

    public interface OnUpdateListener {
        void onUpdate(String s);
    }

    public void setUpdateListener(OnUpdateListener listener) {
        this.listener = listener;
    }
    @Override
    protected String doInBackground(Pair<Context, String>... params) {
        // The IdlingResource is null in production.

        if (myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    //.setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setRootUrl("http://192.168.100.7:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        context = params[0].first;
        String name = params[0].second;

        if (simpleIdlingResource != null) {
            simpleIdlingResource.setIdleState(false);
        }
        try {
            return myApiService.sayHi(name).execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        //super.onPostExecute(s);
        if (listener != null) {
            if (simpleIdlingResource != null) {
                simpleIdlingResource.setIdleState(true);
            }
            listener.onUpdate(result);
        }
        //Toast.makeText(context, result, Toast.LENGTH_LONG).show();
    }
}
