package com.app.dietplan.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.app.dietplan.Activity.DietSearch;
import com.app.dietplan.Activity.SubCategory;
import com.app.dietplan.Adapter.CategoryAdapter;
import com.app.dietplan.Interface.AdShow;
import com.app.dietplan.Item.CategoryList;
import com.app.dietplan.R;
import com.app.dietplan.Util.Constant_Api;
import com.app.dietplan.Util.Method;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CategoryFragment extends Fragment {

    private Method method;
    private AdShow adShow;
    private RecyclerView recyclerView;
    private TextView textView_noData;
    private List<CategoryList> categoryLists;
    private ProgressBar progressBar;
    private CategoryAdapter categoryAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.category_fragment, container, false);

        adShow = new AdShow() {
            @Override
            public void position(int position, String type, String size, String day, String id, String image) {
                startActivity(new Intent(getActivity(), SubCategory.class)
                        .putExtra("cid", id)
                        .putExtra("image", image)
                        .putExtra("category_name", type));//pass category name
            }
        };
        method = new Method(getActivity(), adShow);

        categoryLists = new ArrayList<>();

        progressBar = view.findViewById(R.id.progressbar_sub_category_detail);
        textView_noData = view.findViewById(R.id.textView_category_fragment);
        recyclerView = view.findViewById(R.id.recyclerView_category_fragment);

        textView_noData.setVisibility(View.GONE);

        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        if (Method.isNetworkAvailable(getActivity())) {
            Category();
        } else {
            method.alertBox(getResources().getString(R.string.internet_connection));
        }

        setHasOptionsMenu(true);
        return view;

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.ic_searchView);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener((new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startActivity(new Intent(getActivity(), DietSearch.class)
                        .putExtra("search", query));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        }));

        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Category() {

        progressBar.setVisibility(View.VISIBLE);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Constant_Api.category, null, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                if (getActivity() != null) {

                    Log.d("Response", new String(responseBody));
                    String res = new String(responseBody);

                    try {
                        JSONObject jsonObject = new JSONObject(res);

                        JSONArray jsonArray = jsonObject.getJSONArray("DIET_APP");

                        for (int i = 0; i < jsonArray.length(); i++) {

                            JSONObject object = jsonArray.getJSONObject(i);
                            String cid = object.getString("cid");
                            String category_name = object.getString("category_name");
                            String category_image = object.getString("category_image");
                            String category_image_thumb = object.getString("category_image_thumb");

                            categoryLists.add(new CategoryList(cid, category_name, category_image, category_image_thumb));

                        }

                        if (categoryLists.size() == 0) {
                            textView_noData.setVisibility(View.VISIBLE);
                        } else {
                            textView_noData.setVisibility(View.GONE);
                            categoryAdapter = new CategoryAdapter(getActivity(), categoryLists, adShow);
                            recyclerView.setAdapter(categoryAdapter);
                            progressBar.setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        textView_noData.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

}
