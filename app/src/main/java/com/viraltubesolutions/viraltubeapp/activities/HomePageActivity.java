package com.viraltubesolutions.viraltubeapp.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.viraltubesolutions.videoplayerlibrary.customview.JZVideoPlayer;
import com.viraltubesolutions.viraltubeapp.R;
import com.viraltubesolutions.viraltubeapp.adapters.MyPagerAdapter;
import com.viraltubesolutions.viraltubeapp.fragments.LogOutDialogFragment;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    Toolbar toolbar;
    MyPagerAdapter adapter;
    public TabLayout tabLayout;
    public boolean doubleBackToExitPressedOnce=false;
    public ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initializetoolBar();


    }

    private void initializetoolBar() {
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        tabLayout= (TabLayout) findViewById(R.id.tablayout);
        viewPager= (ViewPager) findViewById(R.id.viewpager);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);

        tabLayout.addTab(tabLayout.newTab().setText("Level1"));
        tabLayout.addTab(tabLayout.newTab().setText("Uploads"));
        tabLayout.addTab(tabLayout.newTab().setText("Level2"));
        tabLayout.addTab(tabLayout.newTab().setText("Contacts"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        adapter=new MyPagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.drawer_open, R.string.drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void changeTabToOne()
    {
        if(viewPager!=null)
        {
            viewPager.setCurrentItem(0);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        //menuInflater.inflate(R.menu.toolbar_items,menu);
        //MenuItem search = menu.findItem(R.id.action_search);
        //SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        //search(searchView);

        return true;
    }

       @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        /*if(id==R.id.action_search)
        {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home)
        {
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
        }
        else  if(id==R.id.nav_events)
        {
            Toast.makeText(this, "Events", Toast.LENGTH_SHORT).show();
        }
        else  if(id==R.id.nav_aboutus)
        {
            Toast.makeText(this, "Aboutus", Toast.LENGTH_SHORT).show();
        }
        else  if(id==R.id.nav_rateus)
        {
            Toast.makeText(this, "Rateus", Toast.LENGTH_SHORT).show();
        }
        else  if(id==R.id.nav_feedback)
        {
            Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
        }
        else  if(id==R.id.nav_callus)
        {
            Toast.makeText(this, "Callus", Toast.LENGTH_SHORT).show();
        }
        else  if(id==R.id.nav_logout)
        {
            callLogoutdialog();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    void callLogoutdialog()
    {
        LogOutDialogFragment logout=new LogOutDialogFragment();
        FragmentManager manager=getSupportFragmentManager();
        FragmentTransaction ft=manager.beginTransaction();
        ft.setCustomAnimations(R.anim.fade_in, R.anim.fade_in);
        logout.setCancelable(false);
        logout.show(ft,"logoutFragment");
    }
    @Override
    public void onBackPressed() {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }
       else if (doubleBackToExitPressedOnce) {
            this.finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Snackbar.make(toolbar, R.string.back_press_to_exit, Snackbar.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    protected void onStop() {
        super.onStop();
        JZVideoPlayer.releaseAllVideos();
    }

}
