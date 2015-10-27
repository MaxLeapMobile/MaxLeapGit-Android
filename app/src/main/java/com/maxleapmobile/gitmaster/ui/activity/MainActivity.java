package com.maxleapmobile.gitmaster.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.api.ApiManager;
import com.maxleapmobile.gitmaster.calllback.ApiCallback;
import com.maxleapmobile.gitmaster.calllback.OperationCallback;
import com.maxleapmobile.gitmaster.manage.UserManager;
import com.maxleapmobile.gitmaster.model.User;
import com.maxleapmobile.gitmaster.ui.fragment.MineFragment;
import com.maxleapmobile.gitmaster.ui.fragment.RecommendFragment;
import com.maxleapmobile.gitmaster.ui.fragment.TimelineFragment;
import com.maxleapmobile.gitmaster.util.Const;
import com.maxleapmobile.gitmaster.util.Logger;
import com.maxleapmobile.gitmaster.util.PreferenceUtil;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_OAUTH = 1;

    private TextView titleView;
    private TimelineFragment timelineFragment;
    private MineFragment mineFragment;
    private RecommendFragment recommendFragment;
    private long mLastBackPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        initToolBar();
        checkAccessToken();
    }


    private void initToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        titleView = (TextView) toolbar.findViewById(R.id.title);
        titleView.setText(R.string.activity_main_item_timeline);

        ImageButton searchBtn = (ImageButton) toolbar.findViewById(R.id.search);
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void checkAccessToken() {
        timelineFragment = new TimelineFragment();
        if (TextUtils.isEmpty(PreferenceUtil.getString(this, Const.ACCESS_TOKEN_KEY, null))
                || TextUtils.isEmpty(PreferenceUtil.getString(this, Const.USERNAME, null))) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_OAUTH);
        } else {
            initUI();
        }
    }

    private void initUI() {
        getGithubUserInfo();
        getSupportFragmentManager().beginTransaction().
                replace(R.id.content_main, timelineFragment).commitAllowingStateLoss();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OAUTH && resultCode == RESULT_OK) {
            initUI();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (System.currentTimeMillis() - mLastBackPressedTime < 2000) {
                super.onBackPressed();
            } else {
                mLastBackPressedTime = System.currentTimeMillis();
                Logger.toast(this, R.string.exit);
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_timeline) {
            titleView.setText(R.string.activity_main_item_timeline);
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.content_main, timelineFragment).commit();
        } else if (id == R.id.nav_recommend) {
            titleView.setText(R.string.activity_main_item_recommend);
            if (recommendFragment == null) {
                recommendFragment = new RecommendFragment();
            }
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.content_main, recommendFragment).commit();

        } else if (id == R.id.nav_mine) {
            titleView.setText(R.string.activity_main_item_mine);
            if (mineFragment == null) {
                Bundle bundle = new Bundle();
                bundle.putString(Const.USERNAME, PreferenceUtil.getString(this, Const.USERNAME, null));
                mineFragment = new MineFragment();
                mineFragment.setArguments(bundle);
            }
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.content_main, mineFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getGithubUserInfo() {
        ApiManager.getInstance().getCurrentUser(new ApiCallback<User>() {
            @Override
            public void success(User user, Response response) {
                user.setAccessToken(PreferenceUtil.getString(MainActivity.this,
                        Const.ACCESS_TOKEN_KEY, null));
                UserManager.getInstance().SaveUserInfo(user, new OperationCallback() {
                    @Override
                    public void success() {
                    }

                    @Override
                    public void failed(String error) {
                        System.currentTimeMillis();
                    }
                });
            }

            @Override
            public void failure(RetrofitError error) {
                super.failure(error);
            }
        });
    }

}
