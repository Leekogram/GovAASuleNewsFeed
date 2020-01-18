package com.leedroids.govaasulenewsfeed;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;


import android.app.SearchManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import model.News;

/**
 * Created by HP-USER on 18-Aug-19.
 */

public class Search extends AppCompatActivity implements SearchAdapter.SearchAdapterListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private List<News> newsList;
    private SearchAdapter mAdapter;
    private SearchView searchView;
    private static final String URL = "https://startupnasarawa.com/feed.json";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!isConnected(Search.this)){
            setContentView(R.layout.activity_search);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(R.string.toolbar_title);


            CardView networkerror = (CardView)findViewById(R.id.networkDialog) ;
            newsList = new ArrayList<>();
            recyclerView = (RecyclerView) findViewById(R.id.search_recycler_view);
            networkerror.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);

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
                    Intent goto_troubleshoot = new Intent(Search.this,Troubleshoot.class);
                    startActivity(goto_troubleshoot);
                }
            });

        }else {
            setContentView(R.layout.activity_search);
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           getSupportActionBar().setTitle(R.string.toolbar_title);
            CardView networkerror = findViewById(R.id.networkDialog) ;
            newsList = new ArrayList<>();
            recyclerView = findViewById(R.id.search_recycler_view);
            networkerror.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);






        }



        newsList = new ArrayList<>();

        recyclerView = findViewById(R.id.search_recycler_view);

        mAdapter = new SearchAdapter(this, newsList, this);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Search.this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, DividerItemDecoration.VERTICAL, 36));
        recyclerView.setAdapter(mAdapter);




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search_main)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                fetchSearchResult();
                mAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                fetchSearchResult();
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search_main) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // close search view on back button pressed
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
    }
    private void whiteNotificationBar(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = view.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            view.setSystemUiVisibility(flags);
            getWindow().setStatusBarColor(Color.WHITE);
        }
    }

    @Override
    public void onSearchSelected(News news) {
        //Toast.makeText(getApplicationContext(), "Selected: " + contact.getName() + ", " + contact.getPhone(), Toast.LENGTH_LONG).show();

        String Image1 = news.getFull_picture();
        String News = news.getMessage();
        String NewsSub = news.getMessage();
        String postTime = news.getCreated_time();

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
    /**
     * fetches json by making http calls
     */
    private void fetchSearchResult() {
        JsonArrayRequest request = new JsonArrayRequest(URL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        if (response == null) {
                            Toast.makeText(getApplicationContext(), "Couldn't fetch the contacts! Pleas try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        List<News> items = new Gson().fromJson(response.toString(), new TypeToken<List<News>>() {
                        }.getType());

                        // adding search result to list
                        newsList.clear();
                        newsList.addAll(items);

                        // refreshing recycler view
                        mAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // error in getting json
                Log.e(TAG, "Error: " + error.getMessage());

                //Toast.makeText(getApplicationContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        MyApplication.getInstance().addToRequestQueue(request);
    }

        }