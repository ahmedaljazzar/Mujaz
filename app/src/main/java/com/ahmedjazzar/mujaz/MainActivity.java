package com.ahmedjazzar.mujaz;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private int[] tabIcons = {
            R.drawable.ic_desktop_windows,
            R.drawable.ic_directions_bike,
            R.drawable.ic_account_balance
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setupViewPager() {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // The following lines of adding fragments are hard coded until I got an official way to get
        // the number of the sections.
        // TODO: Make 'em dynamic based on an API, Fragments titles also
        String[] generalTitles = new String[] {
                "Donald Trump unveils plan to fight ISIS",
                "ESPN's emotional tribute to Saunders",
                "Judge's decision brings inmate to tears"
        };
        int[] generalViews = new int[]  {12123, 8091, 1121};
        String[] generalUrls = new String[]{
                "http://cnnios-f.akamaihd.net/i/cnn/big/politics/2016/08/15/donald-trump-isis-terrorism-speech-schneider-pkg-tsr.cnn_713913_ios_,150,440,650,840,1240,.mp4.csmil/master.m3u8?__b__=650",
                "http://cnnios-f.akamaihd.net/i/cnn/big/cnnmoney/2016/08/10/john-saunders-espn-dies.cnn_706742_ios_,150,440,650,840,1240,.mp4.csmil/master.m3u8?__b__=650",
                "http://cnnios-f.akamaihd.net/i/cnn/big/us/2016/08/09/judge-lets-inmate-see-newborn-baby-orig-vstan-dlewis.cnn_705007_ios_,150,440,650,840,1240,.mp4.csmil/master.m3u8?__b__=650"
        };
        int[] generalThumbnails = new int[]  {
                R.drawable.g1,
                R.drawable.g2,
                R.drawable.g3
        };

        adapter.addFragment(
                NewsFragment.newInstance(generalTitles, generalViews, generalUrls, generalThumbnails), "General"
        );

        String[] sportTitles = new String[] {
                "Katinka Hosszu: 'Amazing to share success with someone you love'",
                "US swimmer Lochte, Brazil police differ on robbery story"
        };
        int[] SportsThumbnails = new int[]  {
                R.drawable.s1,
                R.drawable.s2
        };
        String[] sportUrls = new String[] {
                "http://cnnios-f.akamaihd.net/i/cnn/big/sports/2016/08/19/rio-katinka-hosszu-riddell-pkg.cnn_718246_ios_,150,440,650,840,1240,.mp4.csmil/master.m3u8?__b__=650",
                "http://cnnios-f.akamaihd.net/i/cnn/big/world/2016/08/19/us-swimmers-rio-gas-station-raw-video-ekr-orig-vstan.cnn_718284_ios_,150,440,650,840,1240,.mp4.csmil/master.m3u8?__b__=650'"
        };
        adapter.addFragment(
                NewsFragment.newInstance(sportTitles, generalViews, sportUrls, SportsThumbnails), "Sport"
        );

        String[] businessTitles = new String[] {
                "What Clinton and Trump would do about Obamacare",
                "Auction alert: You can buy gear from the Rio Olympics",
                "Surprise! More $450K Ford GT supercars are on the way!"
        };
        String[] businessUrls = new String[]{
                "http://ht3.cdn.turner.com/money/big/news/economy/2016/08/16/aetna-obamacare.cnnmoney_1024x576.mp4",
                "http://ht3.cdn.turner.com/money/big/news/2016/06/30/olympics-commercials-visa-under-armour-nike-pg-michael-phelps.cnnmoney_1024x576.mp4",
                "http://ht3.cdn.turner.com/money/big/news/2015/03/04/ford-gt-most-expensive-ever.cnnmoney_1024x576.mp4"
        };
        int[] businessThumbnails = new int[]  {
                R.drawable.b1,
                R.drawable.b2,
                R.drawable.b3
        };
        adapter.addFragment(
                NewsFragment.newInstance(businessTitles, generalViews, businessUrls, businessThumbnails), "Business"
        );


        viewPager.setAdapter(adapter);
        // Implemented to reset the players for fragments on each page change
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < adapter.getCount(); i++)    {
                    ((NewsFragment) adapter.getItem(i)).resetMediaPlayer();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_channels) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_live) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
