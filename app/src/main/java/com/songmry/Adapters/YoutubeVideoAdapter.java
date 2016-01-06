package com.songmry.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.songmry.Models.YoutubeVideo;
import com.songmry.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class YoutubeVideoAdapter extends ArrayAdapter<YoutubeVideo>{
    private static class YoutubeVideoHolder {
        TextView videoTitle;
        ImageView videoThumbnail;
    }

    public YoutubeVideoAdapter(Context context, ArrayList<YoutubeVideo> videos){
        super(context, R.layout.item_youtube_video, videos);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        YoutubeVideo video = getItem(position);
        YoutubeVideoHolder videoHolder;


        if(convertView == null){
            videoHolder = new YoutubeVideoHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_youtube_video, parent, false);
            videoHolder.videoTitle = (TextView) convertView.findViewById(R.id.youtubeVideoTitle);
            videoHolder.videoThumbnail = (ImageView) convertView.findViewById(R.id.youtubeVideoThumbnail);
            convertView.setTag(videoHolder);
        } else {
            videoHolder = (YoutubeVideoHolder) convertView.getTag();
        }


        videoHolder.videoTitle.setText(video.title);
        Picasso.with(this.getContext()).load(video.thumbnail)
                .resize(64, 61)
                .centerCrop()
                .into(videoHolder.videoThumbnail);

        return convertView;
    }
}
