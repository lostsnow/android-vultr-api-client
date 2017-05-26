package com.ohmcoe.vultr

import android.app.Fragment
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
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException

class MainActivity() : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    object Config {
        val configFile = "config.ohmcoe"
    }

    val configFile = "config.ohmcoe"
    private var api_key: String? = null
    private var accountFragment: Fragment? = null
    private var serverFragment: Fragment? = null
    private var setAPIKeyFragment: SetAPIKeyFragment? = null
    private var serverListFragment: Fragment? = null
    private var snapshotFragment:Fragment? = null
    var bundle: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val  drawer = drawer_layout
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = nav_view
        navigationView.setNavigationItemSelectedListener(this)

        checkAPIKeyFile()
    }

    fun setBundle() {
        this.bundle!!.putString("API-Key", api_key)

        setAPIKeyFragment = SetAPIKeyFragment()
        setAPIKeyFragment!!.arguments = bundle
        accountFragment = AccountFragment()
        accountFragment!!.arguments = bundle
        serverFragment = ServerFragment()
        serverFragment!!.arguments = bundle
        serverListFragment = ServerListFragment()
        serverListFragment!!.arguments = bundle
        snapshotFragment = SnapshotFragment()
        snapshotFragment!!.arguments = bundle
    }

    fun checkAPIKeyFile() {
        bundle = Bundle()

        val fragmentManager = fragmentManager

        val file = File(application.filesDir, configFile)

        if (file.exists()) {
            val fis: FileInputStream
            try {
                fis = openFileInput(configFile)
                val str = StringBuffer("")
                val buffer = ByteArray(1024)
                var n: Int

                while (true) {
                    n = fis.read(buffer)
                    if (n == -1)
                        break

                    str.append(String(buffer, 0, n))
                }

                this.api_key = str.toString()
                fis.close()
            } catch(e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            setBundle()
            accountFragment!!.arguments = bundle
            fragmentManager.beginTransaction()
                    .replace(content_frame.id, accountFragment)
                    .commit()
        } else {
            setBundle()
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
            val fragmentManager = fragmentManager
            fragmentManager.beginTransaction()
                    .replace(content_frame.id, setAPIKeyFragment)
                    .addToBackStack(null)
                    .commit()
            return false
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId
        val fragmentManager = fragmentManager


        when (id) {
            R.id.account ->  fragmentManager.beginTransaction()
                    .replace(content_frame.id, accountFragment)
                    .commit()
            R.id.server ->   fragmentManager.beginTransaction()
                    .replace(content_frame.id, serverListFragment)
                    .commit()
            R.id.snapshot -> fragmentManager.beginTransaction()
                    .replace(content_frame.id, snapshotFragment)
                    .commit()
        }

        val drawer = drawer_layout
        drawer.closeDrawer(GravityCompat.START)
        return true
    }
}
