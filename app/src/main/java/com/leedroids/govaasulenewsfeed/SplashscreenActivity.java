package com.leedroids.govaasulenewsfeed;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by HP-USER on 23-Jul-19.
 */

public class SplashscreenActivity extends Activity {

    private static  int SPLASH_TIME_OUT = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashsreen_layout);

        //Reference to all widgets
        final LinearLayout linearLayout = (LinearLayout)findViewById(R.id.splashlinearlayout);
        final TextView newsFeed = (TextView)findViewById(R.id.newsFeed);



        final Animation animation_1 = AnimationUtils.loadAnimation(getBaseContext(), R.anim.zoom_in);
        final Animation shake = AnimationUtils.loadAnimation(getBaseContext(), R.anim.shake);



        linearLayout.startAnimation(animation_1);
        animation_1.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {

            }

            @Override public void onAnimationEnd(Animation animation) {

                newsFeed.startAnimation(shake);

            }

            @Override public void onAnimationRepeat(Animation animation) {

            }
        });




        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.goup, R.anim.godown);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });






    }
}
