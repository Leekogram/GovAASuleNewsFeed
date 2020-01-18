package com.leedroids.govaasulenewsfeed;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * Created by HP-USER on 23-Jul-19.
 */

public class AboutActivity extends AppCompatActivity {

    private String postUrl;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isConnected(AboutActivity.this)){
            setContentView(R.layout.about_layout);

            CardView networkerror = (CardView)findViewById(R.id.networkDialog) ;
            //Reference to webview widget
            webView = (WebView) findViewById(R.id.webView);
            networkerror.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);

            final Button tryagain = findViewById(R.id.RetryBtn);
            final Button runtroublesshoot = findViewById(R.id.Rundiagnostics);


            tryagain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    startActivity(getIntent());
                }
            });
            runtroublesshoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent goto_troubleshoot = new Intent(AboutActivity.this,Troubleshoot.class);
                    startActivity(goto_troubleshoot);
                }
            });



        }else {
            setContentView(R.layout.about_layout);
            CardView networkerror = (CardView)findViewById(R.id.networkDialog) ;
            //Reference to webview widget
            webView = (WebView) findViewById(R.id.webView);
            networkerror.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);





        }





        // get intent data
        Intent i = getIntent();

        // Selected image id
        postUrl = i.getExtras().getString("url");



        try {



            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(postUrl);
            webView.setHorizontalScrollBarEnabled(false);



        }catch (Exception e){

            Toast.makeText(AboutActivity.this,""+e,Toast.LENGTH_LONG).show();

        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.godown, R.anim.goup);

    }

    public boolean isConnected(Context context){
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()){
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null  && mobile.isConnectedOrConnecting())||(wifi != null && wifi.isConnectedOrConnecting()))return true;
            else return false;
        }else {
            return false;
        }
    }

}
