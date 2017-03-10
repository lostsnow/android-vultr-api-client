package com.ohmcoe.vultr;


import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String configFile = "config.ohmcoe";
    private String api_key;
    private Fragment accountFragment;
    private Fragment serverFragment;
    private Fragment setAPIKeyFragment;
    private Fragment serverListFragment;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getResources().getString(R.string.api_key);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        checkAPIKeyFile();
    }

    public void setBundle()
    {
        bundle.putString("API-Key", api_key);

        accountFragment = new AccountFragment();
        accountFragment.setArguments(bundle);
        serverFragment = new ServerFragment();
        serverFragment.setArguments(bundle);
        setAPIKeyFragment = new SetAPIKeyFragment();
        setAPIKeyFragment.setArguments(bundle);
        serverListFragment = new ServerListFragment();
        serverListFragment.setArguments(bundle);
    }

    public void checkAPIKeyFile() {
        bundle = new Bundle();

        //start up default fragment
        FragmentManager fragmentManager = getFragmentManager();

        //check api key
        File file = new File(getApplicationContext().getFilesDir(), configFile);

        if (file.exists()) {
            FileInputStream fis;
            try {
                fis = openFileInput(configFile);
                StringBuffer str = new StringBuffer("");
                byte[] buffer = new byte[1024];
                int n;

                while ((n = fis.read(buffer)) != -1)
                {
                    str.append(new String(buffer, 0, n));
                }

                this.api_key = str.toString();

                fis.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            setBundle();

            accountFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, accountFragment)
                    .commit();
        } else {
            setBundle();

            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, setAPIKeyFragment)
                    .addToBackStack(null)
                    .commit();
        }
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
        getMenuInflater().inflate(R.menu.navigation, menu);
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
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame,  setAPIKeyFragment)
                    .addToBackStack(null)
                    .commit();
            return false;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();

        if (id == R.id.account) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, accountFragment)
                    .commit();
        } else if (id == R.id.server) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, serverListFragment)
                    .commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
