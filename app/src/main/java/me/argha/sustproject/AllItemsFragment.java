package me.argha.sustproject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import me.argha.sustproject.helpers.HTTPHelper;
import me.argha.sustproject.models.AllItemViewHolder;
import me.argha.sustproject.models.Category;
import me.argha.sustproject.models.Item;
import me.argha.sustproject.utils.AppConst;
import me.argha.sustproject.utils.AppURL;
import me.argha.sustproject.utils.Util;

/**
 * Author: ARGHA K ROY
 * Date: 11/27/2015.
 */
public class AllItemsFragment extends Fragment {

    ArrayList<Item> itemList;
    RecyclerView itemListView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root=inflater.inflate(R.layout.item_list_layout,container,false);
        itemListView = (RecyclerView) root.findViewById(R.id.itemList);
        itemListView.setHasFixedSize(true);
        GridLayoutManager llm = new GridLayoutManager(getActivity(),2);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        itemListView.setLayoutManager(llm);

        getAllItems();
        setHasOptionsMenu(true);
        return root;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
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

        View searchLayout=getActivity().getLayoutInflater().inflate(R.layout.search_dialog_layout, null);
        final Spinner districtSpinner= (Spinner) searchLayout.findViewById(R.id.searchDistrictSpinner);
        final Spinner mainCatSpinner= (Spinner) searchLayout.findViewById(R.id.searchMainCatSpinner);
        final Spinner subCatSpinner= (Spinner) searchLayout.findViewById(R.id.searchSubCatSpinner);
        mainCatSpinner.setAdapter(new ArrayAdapter<Category>(getActivity(),android.R.layout.simple_spinner_item,allCategories));
        mainCatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                subCatSpinner.setAdapter(new ArrayAdapter<Category>(getActivity(),android.R.layout.simple_spinner_item,allCategories.get(i).getSubCategory()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        AlertDialog dialog=new AlertDialog.Builder(getActivity())
            .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int pos) {
                    String text="";
                    if(districtSpinner.getSelectedItemPosition()!=0){
                        text=getResources().getStringArray(R.array.districtsEnglish)[districtSpinner.getSelectedItemPosition()];
                    }else if(mainCatSpinner.getSelectedItemPosition()!=0){
                        text=((Category)mainCatSpinner.getSelectedItem()).getEnName();
                    }else if(subCatSpinner.getSelectedItemPosition()!=0){
                        text=((Category)subCatSpinner.getSelectedItem()).getEnName();
                    }
                    ArrayList<Item> filteredList=new ArrayList<>();
                    for (int i=0;i<itemList.size();i++){
                        if(itemList.get(i).getName().contains(text) ||
                            itemList.get(i).getDescription().contains(text) ||
                            itemList.get(i).getMain_category().contains(text) ||
                            itemList.get(i).getSub_category().contains(text)){
                            filteredList.add(itemList.get(i));
                        }
                    }
                    itemListView.setAdapter(new ItemAdapter(filteredList));
                    Util.showToast(getActivity(),"Filtering "+filteredList.size());
                }
            })
            .create();

        dialog.setView(searchLayout);
        dialog.show();

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
            Toast.makeText(getActivity(),
                "Speech is not supported",
                Toast.LENGTH_SHORT).show();
        }
    }

    public void getAllItems() {
        AsyncHttpClient httpClient= HTTPHelper.getHTTPClient();
        final ProgressDialog dialog= Util.getProgressDialog(getActivity(),"Loading Items. Please wait...");
        httpClient.get(AppURL.ALL_ITEMS,null,new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                super.onStart();
                dialog.show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Util.printDebug("Item List response",response.toString());
                try {
                    if(response.getBoolean("success")){
                        itemList=new ArrayList<Item>();
                        JSONArray itemArray=response.getJSONArray("data");
                        for (int i=0;i<itemArray.length();i++){
                            JSONObject itemObject=itemArray.getJSONObject(i);
                            Item item=new Item();
                            item.setItemId(itemObject.getString("item_id"));
                            item.setName(itemObject.getString("name"));
                            item.setDescription(itemObject.getString("description"));
                            item.setMain_category(itemObject.getString("main_category"));
                            item.setSub_category(itemObject.getString("sub_category"));
                            item.setQuantity(itemObject.getString("quantity"));
                            item.setUnit(itemObject.getString("unit"));
                            item.setRange_max(itemObject.getString("range_max"));
                            item.setRange_min(itemObject.getString("range_min"));
                            item.setExpire_date(itemObject.getString("expire_date"));
                            item.setRating(itemObject.getString("rating"));
                            item.setPhoto(itemObject.getString("photo"));
                            itemList.add(item);
                        }
                        itemListView.setAdapter(new ItemAdapter(itemList));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Util.printDebug("Json except",e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                Util.printDebug("Item response fail", responseString);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                dialog.dismiss();
            }
        });
    }

    class ItemAdapter extends RecyclerView.Adapter<AllItemViewHolder> implements View.OnClickListener{
        ArrayList<Item> itemList;
        public ItemAdapter(ArrayList<Item> list){
            this.itemList=list;
        }

        @Override
        public AllItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.single_list_item, parent, false);
            itemView.setOnClickListener(this);
            return new AllItemViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(AllItemViewHolder holder, int position) {
            holder.itemName.setText(itemList.get(position).getName());
            holder.itemCategory.setText(itemList.get(position).getSub_category());
            holder.itemRating.setText("Rating: "+itemList.get(position).getRating());
            holder.itemPriceRange.setText("Price Range: " + itemList.get(position).getRange_min() + " - " + itemList.get(position).getRange_max());
            Picasso.with(getActivity()).load(AppURL.ASSETS+itemList.get(position).getPhoto()).into(holder.itemImage);
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }

        @Override
        public void onClick(View view) {
            int itemPosition = itemListView.getChildPosition(view);
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_fragment, ItemDetailsFragment.newInstance(itemList.get(itemPosition)));
            ft.addToBackStack(null);
            ft.commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case AppConst.REQ_CODE_SPEECH_INPUT: {
                if (resultCode == Activity.RESULT_OK && null != data) {
                    ArrayList<String> result = data
                        .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    Util.showToast(getActivity(),result.get(0));
                    String text=result.get(0);
                    ArrayList<Item> filteredList=new ArrayList<>();
                    for (int i=0;i<itemList.size();i++){
                        if(itemList.get(i).getName().contains(text) ||
                            itemList.get(i).getDescription().contains(text) ||
                            itemList.get(i).getMain_category().contains(text) ||
                            itemList.get(i).getSub_category().contains(text)){
                            filteredList.add(itemList.get(i));
                        }
                    }
                    itemListView.setAdapter(new ItemAdapter(filteredList));
                }
                break;
            }

        }
    }
}
