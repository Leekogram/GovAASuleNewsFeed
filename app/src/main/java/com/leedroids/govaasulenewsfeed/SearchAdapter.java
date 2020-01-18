package com.leedroids.govaasulenewsfeed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Filter;
import android.widget.Toast;


import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import model.News;

/**
 * Created by HP-USER on 18-Aug-19.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder> implements Filterable {

    private Context context;
    private List<News> newsList;
    private List<News> newsListFiltered;
    private SearchAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView newsimage1,  newsimage2;
        public TextView time;
        public TextView news;
        public TextView newssub;

        public MyViewHolder(View view) {
            super(view);
            newsimage1 = (ImageView)view.findViewById(R.id.newsimage1);
            newsimage2 = (ImageView)view.findViewById(R.id.newsimage2);
            time = (TextView)view.findViewById(R.id.time);
            news = (TextView)view.findViewById(R.id.news);
            newssub = (TextView)view.findViewById(R.id.newssub);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // send selected contact in callback
                    listener.onSearchSelected(newsListFiltered.get(getAdapterPosition()));
                }
            });
        }
    }
    public SearchAdapter(Context context, List<News>  newsList, SearchAdapterListener listener) {
        this.context = context;
        this.listener = listener;
        this. newsList =  newsList;
        this.newsListFiltered = newsList;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.newslist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final News news_model =newsListFiltered.get(position);

        Glide.with(context).load(news_model.getPicture())
                .into(holder.newsimage1);
        Glide.with(context).load(news_model.getFull_picture())
                .into(holder.newsimage2);


        holder.time.setText(news_model.getCreated_time());
        holder.news.setText(news_model.getMessage());
        holder.newssub.setText(news_model.getMessage());

    }

    @Override
    public int getItemCount() {
        return newsListFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    newsListFiltered = newsList;
                } else {
                    List<News> filteredList = new ArrayList<>();
                    for (News news : newsList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match

                      try {
                          if (news.getMessage().toLowerCase().contains(charString.toLowerCase()) || news.getMessage().contains(charString) || news.getMessage().contains(charSequence)) {
                              filteredList.add(news);
                          }
                      }catch (Exception e){

                      }



                    }

                    newsListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = newsListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                newsListFiltered = (ArrayList<News>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public interface SearchAdapterListener {
        void onSearchSelected(News news);
    }
}
