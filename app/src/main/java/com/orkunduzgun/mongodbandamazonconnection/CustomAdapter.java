package com.orkunduzgun.mongodbandamazonconnection;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<TweetDatas> mKisiListesi;

    public CustomAdapter(Activity activity, List<TweetDatas> kisiler) {
        //XML'i alıp View'a çevirecek inflater'ı örnekleyelim
        mInflater = (LayoutInflater) activity.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        //gösterilecek listeyi de alalım
        mKisiListesi = kisiler;
    }

    @Override
    public int getCount() {
        return mKisiListesi.size();
    }

    @Override
    public TweetDatas getItem(int position) {
        //şöyle de olabilir: public Object getItem(int position)
        return mKisiListesi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View satirView;

        satirView = mInflater.inflate(R.layout.tweets_layout, null);
        TextView nameText = (TextView) satirView.findViewById(R.id.username);
        TextView tweetText = (TextView) satirView.findViewById(R.id.tweet);
        ImageView img = (ImageView) satirView.findViewById(R.id.imageViewProf);
        TextView timeText = (TextView) satirView.findViewById(R.id.time);


        //ImageView imageView = (ImageView) satirView.findViewById(R.id.simge);

        TweetDatas tvit = mKisiListesi.get(position);

        Picasso.with(satirView.getContext()).load(tvit.getImg()).into(img);
        nameText.setText(tvit.getName());
        tweetText.setText(tvit.getTweet());
        timeText.setText(tvit.getTime());

        return satirView;
    }
}
