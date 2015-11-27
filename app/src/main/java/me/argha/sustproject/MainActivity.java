package me.argha.sustproject;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

    FloatingActionMenu fabMenu;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabMenu = (FloatingActionMenu) findViewById(R.id.fabMenu);
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
            fabMenu.showMenu(true);
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
                fabMenu.showMenu(true);
                break;
            case R.id.nav_profile:
                fabMenu.hideMenu(true);
                FragmentTransaction ft2 = getSupportFragmentManager().beginTransaction();
                ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft2.replace(R.id.main_fragment, new MyProfileFragment());
                ft2.addToBackStack(null);
                ft2.commit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
