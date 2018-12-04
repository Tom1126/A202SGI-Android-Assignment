package com.example.chqns022.androidassignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Locale;

import io.paperdb.Paper;

public class HomeMenu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    private Button mapBtn, favBtn, logoutBtn;
    private Fragment mFragment;
    private final CharSequence[] items = { "English / 英文", "Chinese / 中文"};
    private String selection;
    private static boolean isEnglishLanguage = true, isChineseLanguage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_menu);

        Paper.init(this);

        updateViewLanguage((String)Paper.book().read("language"));

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mapBtn = findViewById(R.id.map_btn);
        mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeMenu.this, MapsFragment.class);
                startActivity(intent);
                finish();
            }
        });

        favBtn = findViewById(R.id.favourites_btn);
        favBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeMenu.this, FavouriteListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //NotificationsControl.sendNotifications(this, "My Notifications", "Hello World");

        FirebaseDB db = new FirebaseDB();
        db.getFavouritesFromDB(FavouriteListFragment.getFavourites(), FirebaseDB.getCurrentUser().getEmail(), this);
        db.getMarkersFromDB(MapsFragment.getMarkerList(), this);
        //db.getMarkerFromDB(MarkerFragment.getCurMarker(), this);
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
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.change_language) {
            return true;
        }*/

        switch(id){
            case R.id.logout_settings:
                logOut();
                break;

            case R.id.change_language_settings:
                languageSetting();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void languageSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_AppCompat_Light_Dialog));

        builder.setTitle(R.string.change_language).setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i)
            {
                switch(i)
                {
                    case 0:
                        selection = (String) items[i];


                        Paper.book().write("language", "en");
                        updateViewLanguage((String)Paper.book().read("language"));

                        Intent intent = new Intent(HomeMenu.this, HomeMenu.class);
                        startActivity(intent);
                        finish();

                        break;

                    case 1:
                        selection = (String) items[i];

                        Paper.book().write("language", "zh");
                        updateViewLanguage((String)Paper.book().read("language"));

                        Intent intent2 = new Intent(HomeMenu.this, HomeMenu.class);
                        startActivity(intent2);
                        finish();

                        break;

                    default:
                        break;
                }



            }
        });


        AlertDialog alert = builder.create();
        alert.show();
    }

    private void checkLanguageSelection(){

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch(id){
            case R.id.logout_setting:
                logOut();
                break;

            case R.id.language_setting:
                languageSetting();
                break;

            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Method to log out
     */
    private void logOut() {
        FirebaseDB db = new FirebaseDB();
        db.userSignOut(this);
    }

    /**
     * Method to change app language
     * @param lang App language
     * output: void
     */
    private void updateViewLanguage(String lang){

        if(!lang.equals("zh") && !lang.equals("en")) lang = "en";
        Configuration configuration = new Configuration();
        configuration.locale = new Locale(lang);
        getBaseContext().getResources().updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());

    }


}
