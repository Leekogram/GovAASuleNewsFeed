package com.leedroids.govaasulenewsfeed;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import model.News;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by HP-USER on 19-Aug-19.
 */

public class Troubleshoot extends AppCompatActivity {

    RelativeLayout relativeLayout;
    ImageView iphone,brush,imgWifi,imgInternet,imgServer;
    TextView note;
    LinearLayout wifi,internet,server;
    Button startTroubleshooting;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toubleshoot);

        setTitle("Troubleshooting");



        relativeLayout = findViewById(R.id.rel);
        iphone = findViewById(R.id.iphone);
        brush = findViewById(R.id.brush);
        note = findViewById(R.id.note);
        wifi = findViewById(R.id.wifi_layout);
        internet = findViewById(R.id.internet_layout);
        server = findViewById(R.id.server_layout);

        imgWifi = findViewById(R.id.wifi_connection);
        imgInternet = findViewById(R.id.network_connection);
        imgServer = findViewById(R.id.server_connection);
        startTroubleshooting = findViewById(R.id.startTroubleshooting);
        progressBar = findViewById(R.id.my_progressBar);



        final AnimationDrawable animationDrawable = (AnimationDrawable)relativeLayout.getBackground();
        animationDrawable.setEnterFadeDuration(4000);
        animationDrawable.setExitFadeDuration(4000);


        startTroubleshooting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                wifi.setVisibility(View.INVISIBLE);
                internet.setVisibility(View.INVISIBLE);
                server.setVisibility(View.INVISIBLE);
               animationDrawable.start();


                startTroubleshooting.setText("Check Again");


                iphone.setVisibility(View.GONE);
                brush.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                note.setText("Checking your network connection...");


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isWifiConnected(Troubleshoot.this)){
                            wifi.setVisibility(View.VISIBLE);
                            imgWifi.setImageResource(R.drawable.ic_cancel_black_24dp);
                           imgWifi.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                        }else {
                            wifi.setVisibility(View.VISIBLE);

                        }
                        progressBar.setProgress(30);
                        progressBar.setSecondaryProgress(50);
                    }
                },2000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (!isInternetConnected(Troubleshoot.this)){
                            internet.setVisibility(View.VISIBLE);
                            imgInternet.setImageResource(R.drawable.ic_cancel_black_24dp);
                            imgInternet.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                        }else {
                            internet.setVisibility(View.VISIBLE);
                        }
                        progressBar.setProgress(60);
                        progressBar.setSecondaryProgress(75);
                    }
                },4000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getNews();
                        progressBar.setProgress(100);
                        progressBar.setSecondaryProgress(100);
                    }
                },6000);




            }
        });




    }

    public boolean isWifiConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


            if ((wifi != null && wifi.isConnectedOrConnecting()))return true;
            else return false;
        }else {
            return false;
        }
    }

    public boolean isInternetConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){

            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null  && mobile.isConnectedOrConnecting()))return true;
            else return false;
        }else {
            return false;
        }
    }

    private void getNews() {


        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<News>> call = apiService.getNews();
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {

                if (response.isSuccessful()) {
                    if (response.body() != null) {


                        server.setVisibility(View.VISIBLE);


                    } else {

                    }
                }

            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
                //  Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();

                server.setVisibility(View.VISIBLE);
                imgServer.setImageResource(R.drawable.ic_cancel_black_24dp);
                imgServer.setColorFilter(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
            }
        });
    }


}
