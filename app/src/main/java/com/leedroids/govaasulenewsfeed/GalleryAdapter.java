package com.leedroids.govaasulenewsfeed;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import model.GalleryModel;

/**
 * Created by HP-USER on 25-Jul-19.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    Context context;
    private ArrayList<GalleryModel> gallery_modelArrayList;

    public GalleryAdapter(Context context, ArrayList<GalleryModel> gallery_modelArrayList) {
        this.context = context;
        this.gallery_modelArrayList = gallery_modelArrayList;

    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.galleryimage_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryAdapter.ViewHolder holder, int position) {
        holder.bitmap1.setImageResource(gallery_modelArrayList.get(position).getGalleryImage());

    }

    @Override
    public int getItemCount() {
        return gallery_modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView bitmap1;

        public ViewHolder(View itemView) {
            super(itemView);
            bitmap1 = itemView.findViewById(R.id.bitmap1);


        }
    }


}
