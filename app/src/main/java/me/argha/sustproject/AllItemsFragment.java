package me.argha.sustproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import me.argha.sustproject.helpers.HTTPHelper;
import me.argha.sustproject.models.AllItemViewHolder;
import me.argha.sustproject.models.Item;
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
                            item.setRating("5");
                            item.setPhoto(itemObject.getString("photo"));
                            itemList.add(item);
                        }
                        itemListView.setAdapter(new ItemAdapter());
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
            Picasso.with(getActivity()).load(itemList.get(position).getPhoto()).into(holder.itemImage);
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
}
