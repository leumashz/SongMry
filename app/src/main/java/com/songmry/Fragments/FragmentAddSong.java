package com.songmry.Fragments;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeStandalonePlayer;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.songmry.Adapters.YoutubeVideoAdapter;
import com.songmry.Models.YoutubeVideo;
import com.songmry.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class FragmentAddSong extends Fragment {

    private static final String PROPERTIES_FILENAME = "youtube.properties";
    private static final long NUMBER_OF_VIDEOS_RETURNED = 10;
    private static YouTube youtube;
    public static final String KEY = "AIzaSyB4mQxpi_Wio5uLDJV7_mJ7aXNyrBrThII";
    private View rootView;
    private TextView txtSearch;
    private ImageButton btnSearch;
    YoutubeVideoAdapter resultAdapter;
    ArrayList<YoutubeVideo> arrayOfVideos;
    private ListView resultList;


    public FragmentAddSong() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_agregar_song, container, false);
        txtSearch = (TextView) rootView.findViewById(R.id.txtSearch);
        btnSearch = (ImageButton) rootView.findViewById(R.id.btnSearch);


        btnSearch.setOnClickListener(
                new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchSongYoutube();
                    }
                }
        );

        //btnSearch.setAnimation();

        return rootView;
    }


    public void searchSongYoutube() {
        final Animation animAlpha = AnimationUtils.loadAnimation(this.getContext(), R.anim.alpha_animation);
        try {

            youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
                public void initialize(HttpRequest request) throws IOException {
                }
            }).setApplicationName("songmry-search-video-activity").build();


            String queryTerm = txtSearch.getText().toString() != null ? txtSearch.getText().toString() : "musica";


            YouTube.Search.List search = youtube.search().list("id,snippet");


            String apiKey = KEY;
            search.setKey(apiKey);
            search.setQ(queryTerm);

            search.setType("video");
            search.setVideoCategoryId("music");

            search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);


            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            SearchListResponse searchResponse = search.execute();
            List<SearchResult> searchResultList = searchResponse.getItems();
            if (searchResultList != null) {
                //prettyPrint(searchResultList.iterator(), queryTerm);
                /*if (resultAdapter != null)
                    resultAdapter.clear();*/
                TextView txtResult = (TextView)rootView.findViewById(R.id.txtResult);
                txtResult.setText("Resultados");

                this.getView().setAnimation(animAlpha);
                displayVideoResults(searchResultList.iterator());
            }
        } catch (GoogleJsonResponseException e) {
            System.err.println("There was a service error: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
        } catch (IOException e) {
            System.err.println("There was an IO error: " + e.getCause() + " : " + e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public void displayVideoResults(Iterator<SearchResult> iteratorSearchResults) {
        arrayOfVideos = convertResults(iteratorSearchResults);
        //resultAdapter.clear();
        resultAdapter = new YoutubeVideoAdapter(this.getContext(), arrayOfVideos);
        resultList = (ListView) rootView.findViewById(R.id.youtubeVideoResultList);
        resultList.setFooterDividersEnabled(false);
        resultList.setDivider(null);
        resultList.setDividerHeight(16);

        resultList.setAdapter(resultAdapter);

        resultList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                YoutubeVideo video = arrayOfVideos.get(position);
                //Toast.makeText(resultList.this, , Toast.LENGTH_SHORT).show();
                System.out.println("titulo del video: " + video.title);
                System.out.println("id del video " + video.videoId);
                //Toast.makeText(rootView.getContext(),video.title,Toast.LENGTH_LONG);
                startActivity(YouTubeStandalonePlayer.createVideoIntent(getActivity(),
                        KEY, video.videoId, 0, true, true));
            }
        });
    }

    public ArrayList<YoutubeVideo> convertResults(Iterator<SearchResult> iteratorSearchResults) {
        ArrayList<YoutubeVideo> videos = new ArrayList<YoutubeVideo>();
        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();
                YoutubeVideo video = new YoutubeVideo(singleVideo.getSnippet().getTitle(), rId.getVideoId(), thumbnail.getUrl());
                videos.add(video);
            }
        }

        return videos;
    }

}

/*private static void prettyPrint(Iterator<SearchResult> iteratorSearchResults, String query) {

        System.out.println("\n=============================================================");
        System.out.println(
                "   First " + NUMBER_OF_VIDEOS_RETURNED + " videos for search on \"" + query + "\".");
        System.out.println("=============================================================\n");

        if (!iteratorSearchResults.hasNext()) {
            System.out.println(" There aren't any results for your query.");
        }

        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();

            // Confirm that the result represents a video. Otherwise, the
            // item will not contain a video ID.
            if (rId.getKind().equals("youtube#video")) {
                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getDefault();

                System.out.println(" Video Id" + rId.getVideoId());
                System.out.println(" Title: " + singleVideo.getSnippet().getTitle());
                System.out.println(" Thumbnail: " + thumbnail.getUrl());
                System.out.println("\n-------------------------------------------------------------\n");

            }
        }
    }*/
