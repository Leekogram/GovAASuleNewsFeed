package com.leedroids.govaasulenewsfeed;

import java.util.List;

import model.News;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by HP-USER on 15-Aug-19.
 */

public interface ApiInterface {

    @GET("feed.json")
    Call<List<News>> getNews();
}
