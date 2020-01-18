package com.leedroids.govaasulenewsfeed;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import adapters.GallerySlider;
import adapters.SliderAdapter;
import model.GalleryModel;

import static android.net.sip.SipErrorCode.TIME_OUT;

/**
 * Created by HP-USER on 23-Jul-19.
 */

public class Gallery  extends AppCompatActivity {



    ImageView imageView;


    ViewPager viewPager ;
    TabLayout indicator;
    int[] images;




    private GalleryAdapter galleryAdapter;
    private RecyclerView recyclerview;
    private ArrayList<GalleryModel> galleryModelArrayList;
    Integer bitmap1[]={R.drawable.image1,
                       R.drawable.image2,
                       R.drawable.image3,
                       R.drawable.p_1,
                       R.drawable.p_2,
                       R.drawable.p_3,
                       R.drawable.p_4,
                       R.drawable.p_5,
                       R.drawable.p_6,
                       R.drawable.p_7,
                       R.drawable.p_8,
                       R.drawable.p_9,
                       R.drawable.p_10};

    LinearLayout gallery_layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_layout);

        try {

            recyclerview = (RecyclerView) findViewById(R.id.recycler5);
            RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            recyclerview.setLayoutManager(layoutManager);
            recyclerview.setItemAnimator(new DefaultItemAnimator());
         //   imageView = (ImageView) findViewById(R.id.gallerysliderImage);
            galleryModelArrayList = new ArrayList<>();
            viewPager = (ViewPager)findViewById(R.id.viewPagergallery);
            gallery_layout = findViewById(R.id.gallery_grid);

            for (int i = 0; i < bitmap1.length; i++) {
                GalleryModel view1 = new GalleryModel(bitmap1[i]);
                galleryModelArrayList.add(view1);
            }
            galleryAdapter = new GalleryAdapter(Gallery.this, galleryModelArrayList);
            recyclerview.setAdapter(galleryAdapter);



            // reference to silder images, stored in an array
            images = new int[] {
                    R.drawable.image1,
                    R.drawable.image2,
                    R.drawable.image3,
                    R.drawable.p_1,
                    R.drawable.p_2,
                    R.drawable.p_3


            };


            final AnimationDrawable animationDrawable = (AnimationDrawable)gallery_layout.getBackground();
            animationDrawable.setEnterFadeDuration(4000);
            animationDrawable.setExitFadeDuration(4000);
            animationDrawable.start();
            //imageSlider adapter

            viewPager.setAdapter(new GallerySlider(this, images));

            Timer timer = new Timer();

            timer.scheduleAtFixedRate(new SliderTimer(),1000,30000);





            final Animation animation_1 = AnimationUtils.loadAnimation(Gallery.this, R.anim.scrollup);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {


                    viewPager.startAnimation(animation_1);

                    animation_1.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {


                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });

                }
            }, TIME_OUT);
            /**
             * On Click event for Single Gridview Item
             * */
            recyclerview.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerview, new RecyclerTouchListener.ClickListener() {
                @Override
                public void onClick(View view, int position) {

                    GalleryModel item = galleryModelArrayList.get(position);

                    int imageId = item.getGalleryImage();

                    // Sending image id to FullScreenActivity
                    Bundle dataBundle = new Bundle();
                    dataBundle.putInt("imageId", imageId);


                    Intent i = new Intent(getApplicationContext(), FullImageActivity.class);
                    // passing array index
                    i.putExtras(dataBundle);
                    startActivity(i);
                    overridePendingTransition(R.anim.goup, R.anim.godown);
                }

                @Override
                public void onLongClick(View view, int positon) {

                }
            }));

        }catch (Exception e){
            Toast.makeText(Gallery.this,""+e,Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.godown, R.anim.goup);

    }
    private class  SliderTimer extends TimerTask {
        @Override
        public  void  run(){
            Gallery.this.runOnUiThread(new Runnable() {
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

}
