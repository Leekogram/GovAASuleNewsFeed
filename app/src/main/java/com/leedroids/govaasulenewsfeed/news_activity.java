package com.leedroids.govaasulenewsfeed;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.method.ScrollingMovementMethod;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import static android.net.sip.SipErrorCode.TIME_OUT;

/**
 * Created by HP-USER on 23-Jul-19.
 */

public class news_activity extends AppCompatActivity {

    private ImageView newsImage1, newsImage2, newsShare, backBtn;
    private TextView   NewsBody, NewsTime ;
    private float defaultFontSize = 12;

    private LinearLayout linearLayout,innerLayout;
    private ScrollView scrollView;
    private CardView cardView;
    private int TIME_OUT = 2000;
    private TextView newsName,instruction;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_news_layout);

        final ViewGroup transitionsContainer = (ViewGroup)findViewById(R.id.transitions_container);

        final FloatingActionButton more = (FloatingActionButton) findViewById(R.id.more);
        final FloatingActionButton increaseFont = (FloatingActionButton) findViewById(R.id.increaseFont);
        final FloatingActionButton decreaseFont = (FloatingActionButton) findViewById(R.id.decreaseFont);
        final FloatingActionButton nightMode = (FloatingActionButton) findViewById(R.id.activateNightMode);

        newsImage1 = (ImageView) findViewById(R.id.newsimage1);
        newsImage2 = (ImageView) findViewById(R.id.newsimage2);
        newsShare = (ImageView) findViewById(R.id.shareButton);
        backBtn = (ImageView) findViewById(R.id.backButton);

        newsName = (TextView) findViewById(R.id.newsub);
        NewsTime = (TextView) findViewById(R.id.newsPostTime);
        NewsBody = (TextView) findViewById(R.id.newsbody);

        linearLayout = (LinearLayout) findViewById(R.id.body);
        innerLayout = (LinearLayout)findViewById(R.id.innerLayout);
        scrollView = (ScrollView)findViewById(R.id.scollViewBody);
        cardView = (CardView)findViewById(R.id.newsCardview);
        instruction = (TextView)findViewById(R.id.instruction);

        instruction.setSelected(true);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {



                final Animation blink = AnimationUtils.loadAnimation(getBaseContext(), R.anim.blink);
                more.startAnimation(blink);



            }
        },7000);




        Bundle extras = getIntent().getExtras();


        final String icon1 = extras.getString("newsImage1");


        final String news = extras.getString("news");
        final String newstime = extras.getString("newsPostTime");
        final String newssub = extras.getString("newsSub");

        newsName.setText(newssub);
        NewsBody.setText(news);
        NewsTime.setText(newstime);


        Glide.with(getApplicationContext()).load(icon1)
                .into(newsImage1);
        Glide.with(getApplicationContext()).load(icon1)
                .into(newsImage2);


        newsShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        newssub + newstime + "\n" + news);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
            }
        });


        final Animation animation_1 = AnimationUtils.loadAnimation(news_activity.this, R.anim.text_scroll);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


               newsName.startAnimation(animation_1);

                animation_1.setAnimationListener(new Animation.AnimationListener() {
                    @Override public void onAnimationStart(Animation animation) {

                    }

                    @Override public void onAnimationEnd(Animation animation) {


                    }

                    @Override public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        },TIME_OUT);



        more.setOnClickListener(new View.OnClickListener() {
            boolean visible;
            @Override
            public void onClick(View view) {

                if (Build.VERSION.SDK_INT >= 21){
                    TransitionManager.beginDelayedTransition(transitionsContainer, new TransitionSet().
                            addTransition(new Slide(Gravity.END)));

                    visible = !visible;
                    increaseFont.setVisibility(visible ? View.VISIBLE : View.GONE);
                    decreaseFont.setVisibility(visible ? View.VISIBLE : View.GONE);
                    nightMode.setVisibility(visible ? View.VISIBLE : View.GONE);
                }else {
                    increaseFont.setVisibility(View.VISIBLE);
                    decreaseFont.setVisibility(View.VISIBLE);
                    nightMode.setVisibility(View.VISIBLE);
                }



            }
        });


        increaseFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defaultFontSize++;
                NewsBody.setTextSize(defaultFontSize);
            }
        });

        decreaseFont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                defaultFontSize--;
                NewsBody.setTextSize(defaultFontSize);
            }
        });

        nightMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                linearLayout.setBackgroundColor(Color.rgb(0, 0, 0));
                cardView.setCardBackgroundColor(Color.rgb(21, 21, 21));
                scrollView.setBackgroundColor(Color.rgb(0, 0, 0));
                innerLayout.setBackgroundColor(Color.rgb(21,21,21));
                NewsBody.setBackgroundColor(Color.rgb(2,2,2));
                instruction.setVisibility(View.VISIBLE);

                NewsBody.setTextColor(Color.WHITE);


            }
        });

        nightMode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                linearLayout.setBackgroundColor(Color.WHITE);
                cardView.setCardBackgroundColor(Color.LTGRAY);
                scrollView.setBackgroundColor(Color.WHITE);
                instruction.setVisibility(View.GONE);

                //   NewsTime.setTextColor(Color.BLACK);
                NewsBody.setTextColor(Color.BLACK);
                NewsBody.setBackgroundColor(Color.WHITE);
                innerLayout.setBackgroundColor(Color.WHITE);
                return true;
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.godown, R.anim.goup);

    }
}
