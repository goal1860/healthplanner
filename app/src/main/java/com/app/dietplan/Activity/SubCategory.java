package com.app.dietplan.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.dietplan.Adapter.SubCategoryAdapter;
import com.app.dietplan.Interface.AdShow;
import com.app.dietplan.Item.SubCategoryList;
import com.app.dietplan.R;
import com.app.dietplan.Util.Constant_Api;
import com.app.dietplan.Util.Method;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SubCategory extends AppCompatActivity {

    private Method method;
    public Toolbar toolbar;
    private AdShow adShow;
    private String category_id;
    private RecyclerView recyclerView;
    private TextView textView;
    private ImageView imageView;
    private LinearLayout linearLayout;
    private ProgressBar progressBar;
    private SubCategoryAdapter subCategoryAdapter;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        Method.forceRTLIfSupported(getWindow(), SubCategory.this);

        Constant_Api.subCategoryLists = new ArrayList<>();

        adShow = new AdShow() {
            @Override
            public void position(int position, String type, String size, String day, String id, String image) {
                startActivity(new Intent(SubCategory.this, SubCategoryDetail.class)
                        .putExtra("position", position)
                        .putExtra("size", Integer.parseInt(size))
                        .putExtra("day", day)
                        .putExtra("type", "sub_category"));
            }
        };
        method = new Method(SubCategory.this, adShow);
        method.setStatusBarGradiant();

        Intent intent = getIntent();
        category_id = intent.getStringExtra("cid");
        String categoryName = intent.getStringExtra("category_name");
        String image = intent.getStringExtra("image");

        toolbar = findViewById(R.id.toolbar_sub_category);
        toolbar.setTitle(categoryName);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        linearLayout = findViewById(R.id.linearLayout_sub_category);

        if (Method.personalization_ad) {
            Method.showPersonalizedAds(linearLayout, SubCategory.this);
        } else {
            Method.showNonPersonalizedAds(linearLayout, SubCategory.this);
        }

        progressBar = findViewById(R.id.progressbar_sub_category);
        recyclerView = findViewById(R.id.recyclerView_sub_category);
        textView = findViewById(R.id.textView_sub_category);
        imageView = findViewById(R.id.imageView_sub_category);
        textView.setVisibility(View.GONE);

        if (image != null) {
            Picasso.get().load(image).into(imageView);
        }

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SubCategory.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setFocusable(false);
        recyclerView.setNestedScrollingEnabled(false);

        if (Method.isNetworkAvailable(SubCategory.this)) {
            SubCategoryDetail();
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }

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
                startActivity(new Intent(SubCategory.this, DietSearch.class)
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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void SubCategoryDetail() {

        progressBar.setVisibility(View.VISIBLE);

        Constant_Api.subCategoryLists.clear();

        String url = Constant_Api.sub_category + category_id;

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new AsyncHttpResponseHandler() {
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
                        String cid = object.getString("cat_id");
                        String diet_title = object.getString("diet_title");
                        String diet_info = object.getString("diet_info");
                        String diet_image = object.getString("diet_image");

                        Constant_Api.subCategoryLists.add(new SubCategoryList(id, cid, diet_title, diet_info, diet_image));

                    }
                    if (Constant_Api.subCategoryLists.size() == 0) {
                        textView.setVisibility(View.VISIBLE);
                    } else {
                        textView.setVisibility(View.GONE);
                        subCategoryAdapter = new SubCategoryAdapter(SubCategory.this, Constant_Api.subCategoryLists, adShow);
                        recyclerView.setAdapter(subCategoryAdapter);
                    }

                    progressBar.setVisibility(View.GONE);

                } catch (JSONException e) {
                    e.printStackTrace();
                    textView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

}
