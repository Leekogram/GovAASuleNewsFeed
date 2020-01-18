package com.leedroids.govaasulenewsfeed;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.os.Bundle;
import android.os.Handler;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;

import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;


import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import adapters.NewsAdapter;
import adapters.SliderAdapter;
import model.News;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,SwipeRefreshLayout.OnRefreshListener {


    private ExpandableHeightListView listview;



    private NewsAdapter newsAdapter;

    ViewPager viewPager ;
    TabLayout indicator;
    int[] images;
    LinearLayout main ;
    FrameLayout cover;



    private static  int TIME_OUT2= 3500;

    private ScrollView scrollView;
    private TextView refreshInstruction,congrats;
    private SwipeRefreshLayout swipeRefreshLayout;

    private List<News> newsModel ;
    LinearLayout header_nav;
    private ShimmerFrameLayout shimmerFrameLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isConnected(MainActivity.this)){
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            CardView networkerror = (CardView)findViewById(R.id.networkDialog) ;
            main = (LinearLayout)findViewById(R.id.main_lay) ;
            shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
            cover = (FrameLayout)findViewById(R.id.cover);
            networkerror.setVisibility(View.VISIBLE);
            main.setVisibility(View.GONE);
            cover.setVisibility(View.GONE);
            shimmerFrameLayout.setVisibility(View.GONE);

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View header = navigationView.getHeaderView(0);

            header_nav = header.findViewById(R.id.nav_header);

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
                    Intent goto_troubleshoot = new Intent(MainActivity.this,Troubleshoot.class);
                    startActivity(goto_troubleshoot);
                }
            });




        }else {
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
             CardView networkerror = (CardView)findViewById(R.id.networkDialog) ;
             shimmerFrameLayout = findViewById(R.id.shimmer_view_container);
             main = (LinearLayout)findViewById(R.id.main_lay) ;
            cover = (FrameLayout)findViewById(R.id.cover);
            networkerror.setVisibility(View.GONE);
            main.setVisibility(View.VISIBLE);
            cover.setVisibility(View.VISIBLE);




            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);
            View header = navigationView.getHeaderView(0);

            header_nav = header.findViewById(R.id.nav_header);


        }





        try {
            //reference to all widgets
            final TextView stories = (TextView)findViewById(R.id.storiesToread);
            viewPager = (ViewPager)findViewById(R.id.viewPager);
            indicator=(TabLayout)findViewById(R.id.indicator);
            listview = (ExpandableHeightListView) findViewById(R.id.listview);
            scrollView = (ScrollView)findViewById(R.id.mScrollView);
            refreshInstruction = (TextView)findViewById(R.id.refreshInstruction);
            swipeRefreshLayout =(SwipeRefreshLayout)findViewById(R.id.swipe_container);
            congrats = (TextView)findViewById(R.id.congrats);

            final AnimationDrawable animationDrawable = (AnimationDrawable)header_nav.getBackground();
            animationDrawable.setEnterFadeDuration(4000);
            animationDrawable.setExitFadeDuration(4000);


            animationDrawable.start();






            swipeRefreshLayout.setOnRefreshListener(this);
            swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_dark,
                    android.R.color.holo_orange_dark,
                    android.R.color.holo_red_dark);

            // reference to silder images, stored in an array
            images = new int[] {
                    R.drawable.image1,
                    R.drawable.image2,
                    R.drawable.image3,
                    R.drawable.p_1,
                    R.drawable.p_2,
                    R.drawable.p_3


            };
            newsModel = new ArrayList<>();
            newsAdapter = new NewsAdapter(MainActivity.this, newsModel);
            listview.setAdapter(newsAdapter);
            //imageSlider adapter

            viewPager.setAdapter(new SliderAdapter(this, images));

            indicator.setupWithViewPager(viewPager, true);

            Timer timer = new Timer();

            timer.scheduleAtFixedRate(new SliderTimer(),1000,30000);




            // show loader and fetch new
            swipeRefreshLayout.post(
                    new Runnable() {
                        @Override
                        public void run() {
                            getNews();
                        }
                    }
            );







            //Handles click on news Item
           listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    News model = newsModel.get(i);



                    String Image1 = model.getFull_picture();
                    String News = model.getMessage();
                    String NewsSub = model.getMessage();
                    String postTime = model.getCreated_time();

                    Bundle dataBundle = new Bundle();
                    dataBundle.putString("newsImage1", Image1);

                    dataBundle.putString("news", News);
                    dataBundle.putString("newsSub", NewsSub);
                    dataBundle.putString("newsPostTime", postTime);



                    Intent intent = new
                            Intent(getApplicationContext(), news_activity.class);
                    intent.putExtras(dataBundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.goup, R.anim.godown);


                }
            });






            final Animation rightToLeft = AnimationUtils.loadAnimation(MainActivity.this, R.anim.right_left);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    stories.setVisibility(View.VISIBLE);


                    stories.startAnimation(rightToLeft);

                    rightToLeft.setAnimationListener(new Animation.AnimationListener() {
                        @Override public void onAnimationStart(Animation animation) {

                        }

                        @Override public void onAnimationEnd(Animation animation) {


                        }

                        @Override public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }
            },TIME_OUT2);



       scrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {

                if (!scrollView.canScrollVertically(-1)){
                    refreshInstruction.setVisibility(View.GONE);
                    congrats.setVisibility(View.VISIBLE);
                    congrats.setSelected(true);
                }else {
                    refreshInstruction.setVisibility(View.VISIBLE);
                    refreshInstruction.setSelected(true);
                    congrats.setVisibility(View.GONE);
                }
            }
        });



                       refreshInstruction.setSelected(true);



        }catch (Exception e){
            Toast.makeText(this,""+e,Toast.LENGTH_LONG).show();
        }






    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        if (id == R.id.action_search) {

            Intent start_search = new Intent(MainActivity.this,Search.class);
            startActivity(start_search);
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_aboutGov) {
            // Handle the about action

            // Sending url to AboutActivity
            String url = "https://en.m.wikipedia.org/wiki/Abdullahi_Sule";
            Intent aboutGov = new Intent(getApplicationContext(), AboutActivity.class);
            // passing array index
            aboutGov.putExtra("url", url);
            startActivity(aboutGov);
            overridePendingTransition(R.anim.goup, R.anim.godown);
        } else if (id == R.id.nav_aboutNas) {

            // Sending url to AboutActivity
            String url = "https://en.m.wikipedia.org/wiki/Nasarawa_State";
            Intent aboutNas = new Intent(getApplicationContext(), AboutActivity.class);
            // passing array index
            aboutNas.putExtra("url", url);
            startActivity(aboutNas);
            overridePendingTransition(R.anim.goup, R.anim.godown);

        } else if (id == R.id.nav_gallery) {

            Intent gotoGal = new Intent(getApplicationContext(),Gallery.class);
            startActivity(gotoGal);
            overridePendingTransition(R.anim.goup, R.anim.godown);

        } else if (id == R.id.nav_share) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://www.playstore.com");
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
            overridePendingTransition(R.anim.goup, R.anim.godown);

        }else if  (id == R.id.nav_feedback){

            Intent feedbackintent = new Intent(Intent.ACTION_SENDTO);
            feedbackintent.setData(Uri.parse("mailto:stupnasarawa@yahoo.com"));
            feedbackintent.putExtra(Intent.EXTRA_EMAIL, "stupnasarawa@yahoo.com");
            feedbackintent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from Gov.Abdullahi Sule NewsApp");
            feedbackintent.putExtra(Intent.EXTRA_TEXT,  "");


            try {
                startActivity(Intent.createChooser(feedbackintent, "Send email using..."));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity.this, "No email clients installed.", Toast.LENGTH_SHORT).show();
            }

        } else if (id == R.id.nav_aboutApp) {

            Intent gotoAboutApp = new Intent(getApplicationContext(),AboutApp.class);
            startActivity(gotoAboutApp);
            overridePendingTransition(R.anim.goup, R.anim.godown);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    private class  SliderTimer extends TimerTask {
        @Override
        public  void  run(){
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (viewPager.getCurrentItem()< images.length - 1){
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    }else {
                        viewPager.setCurrentItem(0);
                    }
                }
            });
        }
    }

    @Override public void onRefresh(){

        // swipe refresh is performed, fetch the messages again
        getNews();
    }

    @Override
    public  void onResume(){
        super.onResume();
        shimmerFrameLayout.startShimmerAnimation();
    }
    @Override
    public  void onPause(){
        shimmerFrameLayout.stopShimmerAnimation();
        super.onPause();
    }


    private void getNews() {
        swipeRefreshLayout.setRefreshing(true);

        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);

        Call<List<News>> call = apiService.getNews();
        call.enqueue(new Callback<List<News>>() {
            @Override
            public void onResponse(Call<List<News>> call, Response<List<News>> response) {
                // clear the inbox
                newsModel.clear();

                // add all the messages
                // messages.addAll(response.body());

                // TODO - avoid looping
                // the loop was performed to add colors to each message
                for (News news : response.body()) {
                    // generate a random color
                   // message.setColor(getRandomMaterialColor("400"));
                    newsModel.add(news);
                }

                newsAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);

                shimmerFrameLayout.stopShimmerAnimation();
                shimmerFrameLayout.setVisibility(View.GONE);
                scrollView.smoothScrollTo(0,scrollView.getTop());
            }

            @Override
            public void onFailure(Call<List<News>> call, Throwable t) {
              //  Toast.makeText(getApplicationContext(), "Unable to fetch json: " + t.getMessage(), Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
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
