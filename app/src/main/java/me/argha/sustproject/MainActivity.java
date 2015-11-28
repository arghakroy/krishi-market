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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_voice:
                promptSpeechInput();
                break;
            case R.id.menu_search:
                try {
                    showSearchDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Util.printDebug("Json err",e.getMessage());
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void showSearchDialog() throws JSONException {
        final ArrayList<Category> allCategories=new ArrayList<>();
        JSONObject object=new JSONObject(getString(R.string.categories));
        JSONArray categoriesArray=object.getJSONArray("data");
        for (int i=0;i<categoriesArray.length();i++){
            JSONObject categoryObject=categoriesArray.getJSONObject(i);
            Category mainCat=new Category();
            mainCat.setBnName(categoryObject.getString("bn_name"));
            mainCat.setEnName(categoryObject.getString("eng_name"));
            ArrayList<Category> subCatList=new ArrayList<Category>();
            JSONArray subCatArray=categoryObject.getJSONArray("sub_cat");
            for (int j=0;j<subCatArray.length();j++){
                JSONObject subCatObj=subCatArray.getJSONObject(j);
                Category subCat=new Category();
                subCat.setBnName(subCatObj.getString("bn_name"));
                subCat.setEnName(subCatObj.getString("eng_name"));
                subCatList.add(subCat);
            }
            mainCat.setSubCategory(subCatList);
            allCategories.add(mainCat);
        }

        View searchLayout=getLayoutInflater().inflate(R.layout.search_dialog_layout,null);
        Spinner mainCatSpinner= (Spinner) searchLayout.findViewById(R.id.searchMainCatSpinner);
        final Spinner subCatSpinner= (Spinner) searchLayout.findViewById(R.id.searchSubCatSpinner);
        mainCatSpinner.setAdapter(new ArrayAdapter<Category>(this,android.R.layout.simple_spinner_item,allCategories));
        mainCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subCatSpinner.setAdapter(new ArrayAdapter<Category>(MainActivity.this,android.R.layout.simple_spinner_item,allCategories.get(i).getSubCategory()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        AlertDialog dialog=new AlertDialog.Builder(this)
            .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            })
            .create();

        dialog.setView(searchLayout);
        dialog.show();

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

    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Search Items");
        try {
            startActivityForResult(intent, AppConst.REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                "Speech is not supported",
                Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppConst.REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Util.showToast(MainActivity.this,result.get(0));
                }
                break;
            }

        }
    }
}
