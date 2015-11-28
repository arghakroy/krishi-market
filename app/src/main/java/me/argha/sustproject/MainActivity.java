package me.argha.sustproject;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import me.argha.sustproject.helpers.PrefHelper;
import me.argha.sustproject.models.Category;
import me.argha.sustproject.utils.AppConst;
import me.argha.sustproject.utils.Util;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionMenu fabMenu;
    NavigationView navigationView;

    TextView userFullNameTv;
    TextView userNameTv;

    PrefHelper prefHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        prefHelper=new PrefHelper(this);

        fabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
        fabMenu.setVisibility(View.GONE);
        FloatingActionButton searchFabBtn= (FloatingActionButton) fabMenu.findViewById(R.id.searchFabBtn);
        FloatingActionButton voiceBtn= (FloatingActionButton) findViewById(R.id.voiceFabBtn);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.main_fragment, new AllItemsFragment());
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            navigationView.setCheckedItem(0);
            lastMenuItem=null;
            //fabMenu.showMenu(true);
            super.onBackPressed();
        }
    }

    MenuItem lastMenuItem;
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if(item.isChecked())return false;
        // Handle navigation view item clicks here.
        FragmentManager fm =getSupportFragmentManager();
        for(int i = 0; i < fm.getBackStackEntryCount(); ++i) {
            fm.popBackStack();
        }

        int id = item.getItemId();
        switch (id){
            case R.id.nav_add_item:
                fabMenu.hideMenu(true);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.replace(R.id.main_fragment, new AddNewItemFragment());
                ft.addToBackStack(null);
                ft.commit();
                break;
            case R.id.nav_all_item:
                //fabMenu.showMenu(true);
                break;
            case R.id.nav_profile:
                fabMenu.hideMenu(true);
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft2.replace(R.id.main_fragment, new MyProfileFragment());
                ft2.addToBackStack(null);
                ft2.commit();
                break;
            case R.id.nav_logout:
                prefHelper.logOut();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.nav_heatmap:
                startActivity(new Intent(this,MapsActivity.class));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
