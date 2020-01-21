package life.ppgoal.healthplanner.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import life.ppgoal.healthplanner.Adapter.VideoSearchAdapter;
import life.ppgoal.healthplanner.Interface.AdShow;
import life.ppgoal.healthplanner.Item.VideoList;
import life.ppgoal.healthplanner.R;
import life.ppgoal.healthplanner.Util.Constant_Api;
import life.ppgoal.healthplanner.Util.Method;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class VideoSearch extends AppCompatActivity {

    private Method method;
    private AdShow adShow;
    private Toolbar toolbar;
    private String search;
    private ProgressBar progressBar;
    private TextView textView_noData;
    private List<VideoList> videoLists;
    private RecyclerView recyclerView;
    private VideoSearchAdapter videoSearchAdapter;
    private LayoutAnimationController animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_video);

        Method.forceRTLIfSupported(getWindow(), VideoSearch.this);

        adShow = new AdShow() {
            @Override
            public void position(int position, String type, String size, String day, String id, String image) {
                if (videoLists.get(position).getVideo_type().equals("youtube")) {
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent(VideoSearch.this, Constant_Api.YOUR_DEVELOPER_KEY, videoLists.get(position).getVideo_id(), 0, true, false);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(VideoSearch.this, VideoPlayer.class)
                            .putExtra("Video_url", videoLists.get(position).getVideo_url()));
                }

            }
        };
        method = new Method(VideoSearch.this, adShow);
        method.setStatusBarGradiant();

        videoLists = new ArrayList<>();

        Intent intent = getIntent();
        search = intent.getStringExtra("search");

        int resId = R.anim.layout_animation_fall_down;
        animation = AnimationUtils.loadLayoutAnimation(VideoSearch.this, resId);

        toolbar = findViewById(R.id.toolbar_video);
        toolbar.setTitle(search);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LinearLayout linearLayout = findViewById(R.id.linearLayout_video);

        if (Method.personalization_ad) {
            Method.showPersonalizedAds(linearLayout, VideoSearch.this);
        } else {
            Method.showNonPersonalizedAds(linearLayout, VideoSearch.this);
        }

        progressBar = findViewById(R.id.progressbar_video);
        textView_noData = findViewById(R.id.textView_video);
        recyclerView = findViewById(R.id.recyclerView_video);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(VideoSearch.this);
        recyclerView.setLayoutManager(layoutManager);

        textView_noData.setVisibility(View.GONE);

        if (Method.isNetworkAvailable(VideoSearch.this)) {
            search_data();
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }

    }

    public void search_data() {

        String uri = Constant_Api.search_video + search;

        videoLists.clear();
        progressBar.setVisibility(View.VISIBLE);

        AsyncHttpClient client = new AsyncHttpClient();
        client.cancelAllRequests(true);
        client.get(uri, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                Log.d("Response", new String(responseBody));
                String res = new String(responseBody);

                try {
                    JSONObject jsonObject = new JSONObject(res);

                    JSONArray jsonArray = jsonObject.getJSONArray(Constant_Api.tag);

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);
                        String id = object.getString("id");
                        String video_type = object.getString("video_type");
                        String video_title = object.getString("video_title");
                        String video_url = object.getString("video_url");
                        String video_id = object.getString("video_id");
                        String video_thumbnail_b = object.getString("video_thumbnail_b");
                        String video_thumbnail_s = object.getString("video_thumbnail_s");
                        String video_duration = object.getString("video_duration");

                        videoLists.add(new VideoList("", id, video_type, video_title, video_url, video_id, video_thumbnail_b, video_thumbnail_s, video_duration));

                    }

                    if (videoLists.size() == 0) {
                        textView_noData.setVisibility(View.VISIBLE);
                    } else {
                        textView_noData.setVisibility(View.GONE);
                        videoSearchAdapter = new VideoSearchAdapter(VideoSearch.this, videoLists, adShow);
                        recyclerView.setAdapter(videoSearchAdapter);
                        recyclerView.setLayoutAnimation(animation);
                    }

                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    textView_noData.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

            }


            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
