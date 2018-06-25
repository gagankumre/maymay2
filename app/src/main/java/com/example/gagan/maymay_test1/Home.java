package com.example.gagan.maymay_test1;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity implements Homefragment.OnFragmentInteractionListener,Notificationfragment.OnFragmentInteractionListener,Createfragment.OnFragmentInteractionListener,Profiefragment.OnFragmentInteractionListener, SwipeRefreshLayout.OnRefreshListener  {
    private DrawerLayout mDrawerLayout;
    private BottomNavigationView nav_view;
    private FrameLayout fr_layout;

    private ViewPager homepager;
    private PagerViewadapter mpagerViewadapter;


    private SwipeRefreshLayout swipeRefreshLayout;


    private Homefragment homefr;
    private Notificationfragment notifr;
    private Createfragment createfr;
    private Profiefragment profilefr;

    private FirebaseAuth auth;



    @Override
    protected void onStart(){
        super.onStart();

        FirebaseUser currentUser= auth.getCurrentUser();
        if (currentUser==null){
            sendtologin();
        }
    }

    private void sendtologin() {

        //       create intent
        Intent intent= new Intent(Home.this,Login.class);
        startActivity(intent);
        finish();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        auth=FirebaseAuth.getInstance();


        //navigationbottom view
        fr_layout = findViewById(R.id.framelayout);
        nav_view = findViewById(R.id.navigationbottomlayout);

        homepager= findViewById(R.id.homeviewpager);
        mpagerViewadapter = new PagerViewadapter(getSupportFragmentManager());
        homepager.setOffscreenPageLimit(4);

        homefr = new Homefragment();
        notifr = new Notificationfragment();
        createfr = new Createfragment();
        profilefr = new Profiefragment();


        homepager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                changeicon(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        homepager.setAdapter(mpagerViewadapter);


        nav_view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id= menuItem.getItemId();
                if(id==R.id.homebottom){
//                    setfragment(homefr);
                    homepager.setCurrentItem(0);
                }
                if (id==R.id.notibottom){
//                    setfragment(notifr);
                    homepager.setCurrentItem(1);
                }
                if (id==R.id.createbottom){
//                    setfragment(createfr);
                    homepager.setCurrentItem(2);
                }
                if (id==R.id.profilebottom){
//                    setfragment(profilefr);
                    homepager.setCurrentItem(3);

                }
                return true;
            }
        });


        // toolbar up
        Toolbar toolbar = findViewById(R.id.toolbar);

        this.setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        assert actionbar != null;
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);



        // navigation drawer
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView NV = findViewById(R.id.nav_view);

        NV.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        if(id==R.id.nav_home){
                            mDrawerLayout.closeDrawers();
                            nav_view.setSelectedItemId(R.id.homebottom);
                        }
                        if(id==R.id.nav_profile){
                            mDrawerLayout.closeDrawers();
                            nav_view.setSelectedItemId(R.id.profilebottom);
                        }
                        if (id==R.id.nav_signout){
                            finish();
                            auth.signOut();
                            sendtologin();
                            Toast.makeText(Home.this,"Logged out successfully", Toast.LENGTH_SHORT).show();
                        }
                        if(id==R.id.nav_settings){
                            mDrawerLayout.closeDrawers();
                            Intent intent =new Intent(Home.this,Profile.class);
                            startActivity(intent);
                        }

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here
                        return true;

                    }
                });

//      onbuttonclick();

    }

    private void changeicon(int position) {
        if(position==0){
            nav_view.setSelectedItemId(R.id.homebottom);
        }
        if(position==1){
            nav_view.setSelectedItemId(R.id.notibottom);
        }
        if (position==2){
            nav_view.setSelectedItemId(R.id.createbottom);
        }
        if (position==3) {
            nav_view.setSelectedItemId(R.id.profilebottom);
        }
    }



    //  set fragment method
    private void setfragment(android.support.v4.app.Fragment fragment) {
        FragmentTransaction frag_tr = getSupportFragmentManager().beginTransaction();
        frag_tr.replace(R.id.framelayout,fragment);
        frag_tr.commit();

    }


    // menu
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//    // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menubar, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // onclick

    private ImageButton profile;

    public void onbuttonclick(){
        profile = (ImageButton) findViewById(R.id.profilebtn);
        profile.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent second = new Intent("com.example.gagan.maymay_test1.Profile");
                        second.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(second);

                    }
                });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    @Override
    public void onRefresh(){
        swipeRefreshLayout.setRefreshing(true);
        refreshList();
    }
    public void refreshList(){
        //do processing to get new data and set your listview's adapter, maybe  reinitialise the loaders you may be using or so
        //when your data has finished loading, cset the refresh state of the view to false
        int g =homepager.getCurrentItem();
        homepager.setAdapter(mpagerViewadapter);
        mpagerViewadapter.notifyDataSetChanged();
        homepager.setCurrentItem(g);
        swipeRefreshLayout.setRefreshing(false);

    }

}

