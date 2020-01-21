package life.ppgoal.healthplanner.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import life.ppgoal.healthplanner.Adapter.ViewpagerAdapter;
import life.ppgoal.healthplanner.R;
import life.ppgoal.healthplanner.Util.Constant_Api;
import life.ppgoal.healthplanner.Util.Method;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SubCategoryDetail extends AppCompatActivity {

    private Method method;
    public Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int position;
    private int size;
    private String strings, type;
    private LinearLayout linearLayout;
    final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 101;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_detail);

        Method.forceRTLIfSupported(getWindow(), SubCategoryDetail.this);

        method = new Method(SubCategoryDetail.this);
        method.setStatusBarGradiant();

        Intent intent = getIntent();
        size = intent.getIntExtra("size", 0);
        position = intent.getIntExtra("position", 0);
        strings = intent.getStringExtra("day");
        type = intent.getStringExtra("type");

        String[] day = strings.split(",,/");

        toolbar = findViewById(R.id.toolbar_sub_category_detail);
        tabLayout = findViewById(R.id.tablayout_sub_category_detail);
        viewPager = findViewById(R.id.viewPager_sub_category_detail);

        switch (type) {
            case "notification":
                if (Constant_Api.notificationSCL != null) {
                    toolbar.setTitle(Constant_Api.notificationSCL.get(position).getDiet_title());
                }
                break;
            case "search":
                toolbar.setTitle(Constant_Api.searchList.get(position).getDiet_title());
                break;
            default:
                toolbar.setTitle(Constant_Api.subCategoryLists.get(position).getDiet_title());
                break;
        }
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        linearLayout = findViewById(R.id.linearLayout_sub_category);

        if (Method.personalization_ad) {
            Method.showPersonalizedAds(linearLayout, SubCategoryDetail.this);
        } else {
            Method.showNonPersonalizedAds(linearLayout, SubCategoryDetail.this);
        }

        for (int i = 0; i < size; i++) {
            tabLayout.addTab(tabLayout.newTab().setText(day[i]));
        }
        LinearLayout linearLayout = (LinearLayout) tabLayout.getChildAt(0);
        linearLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(getResources().getColor(R.color.tabLayoutDivider));
        drawable.setSize(1, 1);
        linearLayout.setDividerPadding(20);
        linearLayout.setDividerDrawable(drawable);
        if (size <= 2) {
            tabLayout.setTabMode(TabLayout.MODE_FIXED);
        } else {
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        }

        //change selected tab when viewpager changed page
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //create and set ViewPager adapter
        ViewpagerAdapter viewpagerAdapter = new ViewpagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), SubCategoryDetail.this, position, type);
        viewPager.setAdapter(viewpagerAdapter);
        viewPager.setCurrentItem(position);


        //change viewpager page when tab selected
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (type) {
                    case "notification":
                        if (Constant_Api.notificationSCL != null) {
                            toolbar.setTitle(Constant_Api.notificationSCL.get(position).getDiet_title());
                        }
                        break;
                    case "search":
                        toolbar.setTitle(Constant_Api.searchList.get(tab.getPosition()).getDiet_title());
                        break;
                    default:
                        toolbar.setTitle(Constant_Api.subCategoryLists.get(tab.getPosition()).getDiet_title());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.ic_share:
                if (Method.permissionStorage) {
                    Method.share = true;
                    switch (type) {
                        case "notification":
                            if (Constant_Api.notificationSCL != null) {
                                method.share_save(Constant_Api.notificationSCL.get(viewPager.getCurrentItem()).getDiet_image(),
                                        Constant_Api.notificationSCL.get(viewPager.getCurrentItem()).getDiet_title(),
                                        Constant_Api.notificationSCL.get(viewPager.getCurrentItem()).getDiet_info());
                            }
                            break;
                        case "search":
                            method.share_save(Constant_Api.searchList.get(viewPager.getCurrentItem()).getDiet_image(),
                                    Constant_Api.searchList.get(viewPager.getCurrentItem()).getDiet_title(),
                                    Constant_Api.searchList.get(viewPager.getCurrentItem()).getDiet_info());

                            break;
                        default:
                            method.share_save(Constant_Api.subCategoryLists.get(viewPager.getCurrentItem()).getDiet_image(),
                                    Constant_Api.subCategoryLists.get(viewPager.getCurrentItem()).getDiet_title(),
                                    Constant_Api.subCategoryLists.get(viewPager.getCurrentItem()).getDiet_info());
                            break;
                    }
                    Toast.makeText(SubCategoryDetail.this, getResources().getString(R.string.share), Toast.LENGTH_SHORT).show();
                } else {
                    checkPer();
                }
                break;

            case android.R.id.home:
                onBackPressed();
                break;

            default:
                break;
        }

        return true;
    }

    public void checkPer() {
        if ((ContextCompat.checkSelfPermission(SubCategoryDetail.this, "android.permission.WRITE_EXTERNAL_STORAGE" + "android.permission.WRITE_INTERNAL_STORAGE") != PackageManager.PERMISSION_GRANTED)) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{"android.permission.WRITE_EXTERNAL_STORAGE", "android.permission.WRITE_INTERNAL_STORAGE"},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else {
                Method.permissionStorage = true;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        boolean canUseExternalStorage = false;

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    canUseExternalStorage = true;
                    Method.permissionStorage = true;
                }
                if (!canUseExternalStorage) {
                    Method.permissionStorage = false;
                    Toast.makeText(SubCategoryDetail.this, getResources().getString(R.string.cannot_use_save_permission), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        Method.isOnBackPress();
        super.onBackPressed();
    }
}
