package life.ppgoal.healthplanner.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import life.ppgoal.healthplanner.Adapter.FitnessVideoAdapter;
import life.ppgoal.healthplanner.Interface.AdShow;
import life.ppgoal.healthplanner.Item.VideoList;
import life.ppgoal.healthplanner.R;
import life.ppgoal.healthplanner.Util.Constant_Api;
import life.ppgoal.healthplanner.Util.EndlessRecyclerViewScrollListener;
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
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FitnessVideo extends AppCompatActivity {

    private Method method;
    private AdShow adShow;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private TextView textView_noData;
    private List<VideoList> videoLists;
    private RecyclerView recyclerView;
    private FitnessVideoAdapter fitnessVideoAdapter;
    private LayoutAnimationController animation;
    private Boolean isOver = false;
    private int pagination_index = 1;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness_video);

        Method.forceRTLIfSupported(getWindow(), FitnessVideo.this);

        adShow = new AdShow() {
            @Override
            public void position(int position, String type, String size, String day, String id, String image) {
                if (videoLists.get(position).getVideo_type().equals("youtube")) {
                    Intent intent = YouTubeStandalonePlayer.createVideoIntent(FitnessVideo.this, Constant_Api.YOUR_DEVELOPER_KEY, videoLists.get(position).getVideo_id(), 0, true, false);
                    startActivity(intent);
                } else {
                    startActivity(new Intent(FitnessVideo.this, VideoPlayer.class)
                            .putExtra("Video_url", videoLists.get(position).getVideo_url()));
                }

            }
        };
        method = new Method(FitnessVideo.this, adShow);
        method.setStatusBarGradiant();

        videoLists = new ArrayList<>();

        int resId = R.anim.layout_animation_fall_down;
        animation = AnimationUtils.loadLayoutAnimation(FitnessVideo.this, resId);

        toolbar = findViewById(R.id.toolbar_video);
        toolbar.setTitle(getResources().getString(R.string.fitness_video));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LinearLayout linearLayout = findViewById(R.id.linearLayout_video);

        if (Method.personalization_ad) {
            Method.showPersonalizedAds(linearLayout, FitnessVideo.this);
        } else {
            Method.showNonPersonalizedAds(linearLayout, FitnessVideo.this);
        }

        progressBar = findViewById(R.id.progressbar_video);
        textView_noData = findViewById(R.id.textView_video);
        recyclerView = findViewById(R.id.recyclerView_video);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(FitnessVideo.this);
        recyclerView.setLayoutManager(layoutManager);

        textView_noData.setVisibility(View.GONE);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                if (!isOver) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pagination_index++;
                            callData();
                        }
                    }, 1000);
                } else {
                    fitnessVideoAdapter.hideHeader();
                }
            }
        });

        callData();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.ic_searchView);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener((new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startActivity(new Intent(FitnessVideo.this, VideoSearch.class)
                        .putExtra("search", query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        }));

        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                break;
        }

        return true;
    }


    public void callData() {
        if (Method.isNetworkAvailable(FitnessVideo.this)) {
            videoList();
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }
    }

    public void videoList() {

        String uri = Constant_Api.video + String.valueOf(pagination_index);

        if (fitnessVideoAdapter == null) {
            videoLists.clear();
            progressBar.setVisibility(View.VISIBLE);
        }

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
                        String num = object.getString("num");
                        String id = object.getString("id");
                        String video_type = object.getString("video_type");
                        String video_title = object.getString("video_title");
                        String video_url = object.getString("video_url");
                        String video_id = object.getString("video_id");
                        String video_thumbnail_b = object.getString("video_thumbnail_b");
                        String video_thumbnail_s = object.getString("video_thumbnail_s");
                        String video_duration = object.getString("video_duration");

                        videoLists.add(new VideoList(num, id, video_type, video_title, video_url, video_id, video_thumbnail_b, video_thumbnail_s, video_duration));

                    }

                    if (jsonArray.length() == 0) {
                        if (fitnessVideoAdapter != null) {
                            fitnessVideoAdapter.hideHeader();
                        }
                    }

                    if (fitnessVideoAdapter == null) {
                        if (videoLists.size() == 0) {
                            textView_noData.setVisibility(View.VISIBLE);
                        } else {
                            textView_noData.setVisibility(View.GONE);
                            fitnessVideoAdapter = new FitnessVideoAdapter(FitnessVideo.this, videoLists, adShow);
                            recyclerView.setAdapter(fitnessVideoAdapter);
                            recyclerView.setLayoutAnimation(animation);
                        }
                    } else {
                        fitnessVideoAdapter.notifyDataSetChanged();
                    }
                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    isOver = true;
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
