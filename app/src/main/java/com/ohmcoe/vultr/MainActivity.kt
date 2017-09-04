package com.ohmcoe.vultr

import android.app.Fragment
import android.content.Context
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity() : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        val PREFS_NAME:String = "VultrAPIClientFile"
        val API_KEY:String = "API_KEY"
    }

    private var api_key: String? = null
    private var accountFragment: Fragment? = null
    private var setAPIKeyFragment: SetAPIKeyFragment? = null
    private var serverListFragment: Fragment? = null
    private var snapshotFragment: Fragment? = null
    var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val drawer = drawer_layout
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = nav_view
        navigationView.setNavigationItemSelectedListener(this)

        checkAPIKeyFile(savedInstanceState)
    }

    fun setBundle(api_key: String?) {
        bundle!!.putString("API-Key", api_key)

        setAPIKeyFragment = SetAPIKeyFragment()
        setAPIKeyFragment!!.arguments = bundle
        accountFragment = AccountFragment()
        accountFragment!!.arguments = bundle
        serverListFragment = ServerListFragment()
        serverListFragment!!.arguments = bundle
        snapshotFragment = SnapshotFragment()
        snapshotFragment!!.arguments = bundle
    }

    fun checkAPIKeyFile(savedInstanceState: Bundle?) {
        bundle = Bundle()

        val sharedPreferences = getSharedPreferences(MainActivity.PREFS_NAME, Context.MODE_PRIVATE)
        api_key = sharedPreferences.getString(MainActivity.API_KEY, null)

        if (api_key != null) {
            setBundle(api_key)
            if (savedInstanceState == null) {
                showAccountFragment()
            }
        } else {
            setBundle(api_key)
            fragmentManager.beginTransaction()
                    .replace(content_frame.id, setAPIKeyFragment)
                    .addToBackStack(null)
                    .commit()
        }
    }

    override fun onBackPressed() {
        val drawer = drawer_layout
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        if (id == R.id.action_settings) {
            showSettingsFragment()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        when (id) {
            R.id.account -> showAccountFragment()
            R.id.server -> showServerListFragment()
            R.id.snapshot -> showSnapshotFragment()
        }

        val drawer = drawer_layout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    fun showAccountFragment()
    {
        fragmentManager.beginTransaction()
                .replace(content_frame.id, accountFragment)
                .commit()
    }

    fun showServerListFragment()
    {
        fragmentManager.beginTransaction()
                .replace(content_frame.id, serverListFragment)
                .commit()
    }

    fun showSnapshotFragment() {
        fragmentManager.beginTransaction()
                .replace(content_frame.id, snapshotFragment)
                .commit()
    }

    private fun showSettingsFragment() {
        fragmentManager.beginTransaction()
                .replace(content_frame.id, setAPIKeyFragment)
                .addToBackStack(null)
                .commit()
    }
}
