package adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.leedroids.govaasulenewsfeed.R;

import java.util.List;

import model.News;

/**
 * Created by HP-USER on 19-Jul-19.
 */

public class NewsAdapter  extends BaseAdapter {

    Context context;

   List<News>news_model;
   private int lastPostion= -1;






    public NewsAdapter(Context context,List<News> news_model) {


        this.context = context;
        this.news_model = news_model;
    }


    @Override
    public int getCount() {
        return news_model.size();
    }

    @Override
    public Object getItem(int position) {
        return news_model.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.newslist,null,true);



            viewHolder.newsimage1 = (ImageView)convertView.findViewById(R.id.newsimage1);
            viewHolder.newsimage2 = (ImageView)convertView.findViewById(R.id.newsimage2);
            viewHolder.time = (TextView)convertView.findViewById(R.id.time);
            viewHolder.news = (TextView)convertView.findViewById(R.id.news);
            viewHolder.newssub = (TextView)convertView.findViewById(R.id.newssub);



           /** Animation animation = AnimationUtils.loadAnimation(context,(position > lastPostion)? R.anim.up_from_bottom:R.anim.down_from_top);

            convertView.startAnimation(animation);
            lastPostion = position;**/



            convertView.setTag(viewHolder);


        }else {

            viewHolder = (ViewHolder)convertView.getTag();
        }







        News news_model = (News) getItem(position);

        Glide.with(context).load(news_model.getPicture())
                .into(viewHolder.newsimage1);
        Glide.with(context).load(news_model.getFull_picture())
                .into(viewHolder.newsimage2);


        viewHolder.time.setText(news_model.getCreated_time());
        viewHolder.news.setText(news_model.getMessage());
        viewHolder.newssub.setText(news_model.getMessage());








        return convertView;
    }

    private class ViewHolder{

        ImageView newsimage1,  newsimage2;
        TextView time;
        TextView news;
        TextView newssub;







    }



}
