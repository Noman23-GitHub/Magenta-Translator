package ru.noman23.magentatranslator.ui.NavigationUI;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.noman23.magentatranslator.R;
import ru.noman23.magentatranslator.activities.HistoryActivity;
import ru.noman23.magentatranslator.activities.MainActivity;
import ru.noman23.magentatranslator.activities.SavedActivity;

public class NavigationUI {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.nav_view_start) NavigationView mStartNavigationView;

    private AppCompatActivity activity;

    public NavigationUI() {
    }

    public void setNavigation(AppCompatActivity activity) {
        ButterKnife.bind(this, activity);
        this.activity = activity;

        activity.setSupportActionBar(mToolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(activity, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        mDrawerToggle.setToolbarNavigationClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));

        mStartNavigationView.setNavigationItemSelectedListener(this::onNavigationItemSelect);
    }

    private boolean onNavigationItemSelect(MenuItem item) {
        mDrawerLayout.closeDrawer(GravityCompat.START);
        Intent intent = null;
        switch (item.getItemId()) {
            case R.id.navigate_main:
                intent = new Intent(activity, MainActivity.class);
                break;
            case R.id.navigate_history:
                intent = new Intent(activity, HistoryActivity.class);
                break;
            case R.id.navigate_saved:
                intent = new Intent(activity, SavedActivity.class);
                break;
        }
        if (intent != null) activity.startActivity(intent);
        return true;
    }

    public void setNavigationSelected(@IdRes int rid) {
        mStartNavigationView.setCheckedItem(rid);
    }
}
