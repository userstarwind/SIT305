package com.example.itubeapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.jetbrains.annotations.Contract;

import java.net.MalformedURLException;
import java.net.URL;

public class VideoActivity extends AppCompatActivity {
private WebView YoutubeVideoWebView;
private String VideoURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Intent intent=getIntent();
        VideoURL=intent.getStringExtra("VIDEO_URL");
        YoutubeVideoWebView=findViewById(R.id.youtubeVideoWebView);
        WebSettings webSettings = YoutubeVideoWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        loadYouTubeVideo(extractVideoIdFromUrl(VideoURL));

    }
    public void loadYouTubeVideo(String videoId) {
        String html = getHtmlString(videoId);
        YoutubeVideoWebView.loadData(html, "text/html", "UTF-8");
    }
    public static String extractVideoIdFromUrl(String videoUrl) {
        try {
            URL url = new URL(videoUrl);
            String query = url.getQuery();
            String[] queryPairs = query.split("&");
            for (String pair : queryPairs) {
                int idx = pair.indexOf("=");
                String queryParam = pair.substring(0, idx);
                String queryValue = pair.substring(idx + 1);

                if ("v".equals(queryParam)) {
                    return queryValue;
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }
    @NonNull
    @Contract(pure = true)
    private String getHtmlString(String videoId) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<body>" +
                "<div id=\"player\"></div>" +
                "<script>" +
                "  var tag = document.createElement('script');" +
                "  tag.src = \"https://www.youtube.com/iframe_api\";" +
                "  var firstScriptTag = document.getElementsByTagName('script')[0];" +
                "  firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);" +
                "  var player;" +
                "  function onYouTubeIframeAPIReady() {" +
                "    player = new YT.Player('player', {" +
                "      height: '180'," +
                "      width: '320'," +
                "      videoId: '" + videoId + "'," +
                "      events: {" +
                "        'onReady': onPlayerReady," +
                "        'onStateChange': onPlayerStateChange" +
                "      }" +
                "    });" +
                "  }" +
                "  function onPlayerReady(event) {" +
                "    event.target.playVideo();" +
                "  }" +
                "  function onPlayerStateChange(event) {" +
                "    if (event.data == YT.PlayerState.ENDED) {" +
                "    }" +
                "  }" +
                "</script>" +
                "</body>" +
                "</html>";
    }

public void backToHome(View view){
        Intent intent=new Intent(this, HomeActivity.class);
        startActivity(intent);
        finish();
}
}