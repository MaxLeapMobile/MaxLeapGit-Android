package com.maxleapmobile.gitmaster.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.maxleap.MLUser;
import com.maxleapmobile.gitmaster.R;
import com.maxleapmobile.gitmaster.ui.fragment.MineFragment;
import com.maxleapmobile.gitmaster.ui.fragment.TimelineFragment;
import com.maxleapmobile.gitmaster.util.Const;
import com.maxleapmobile.gitmaster.util.PreferenceUtil;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int REQUEST_OAUTH = 1;

    private TextView titleView;
    private FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        initToolBar();
        checkAccessToken();
        initUI();
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
        if (null == PreferenceUtil.getString(this, Const.ACCESS_TOKEN_KEY, null)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, REQUEST_OAUTH);
        } else {
            initUI();
        }
    }

    private void initUI() {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, new TimelineFragment());
        transaction.commitAllowingStateLoss();
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
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_timeline) {

        } else if (id == R.id.nav_recommend) {

        } else if (id == R.id.nav_mine) {
            Bundle bundle = new Bundle();
            bundle.putString(Const.USERNAME, MLUser.getCurrentUser().getUserName());
            MineFragment mineFragment = new MineFragment();
            mineFragment.setArguments(bundle);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_main, mineFragment);
            transaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void itemSelect(int position) {

    }
}
