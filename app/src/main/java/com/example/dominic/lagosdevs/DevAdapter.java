package com.example.dominic.lagosdevs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;

/**
 * Created by Dominic Idagu on 09/03/2017.
 */

public class DevAdapter extends ArrayAdapter {
    private Context context;
    private ArrayList<Dev> itemsList;

    public DevAdapter(Context context,ArrayList<Dev> items){
        super(context,android.R.layout.simple_list_item_1,items);
        this.context=context;
        this.itemsList=items;
    }

    public int getCount() {
        if(itemsList!=null){
            return itemsList.size();
        }
        return 0;
    }

    public Dev getItem(int position) {
        if(itemsList != null){
            return itemsList.get(position);
        }
        return null;
    }

    public long getItemId(int position) {
        if(itemsList != null){
            return itemsList.get(position).hashCode();
        }
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView=convertView;
        //get view from row item
        if(rowView == null){
            LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView=inflater.inflate(R.layout.list_item,null);
        }
        Dev dev=itemsList.get(position);
        ImageView photo=(ImageView) rowView.findViewById(R.id.items_list_image);
        String q=dev.getPhotoUrl();
        Log.i("log photo url",q);
        //Loading Image from URL
        Picasso.with(context)
                .load(dev.getPhotoUrl())
                //.placeholder(R.drawable.placeholder)   // optional
                .error(R.drawable.loading)      // optional
                .resize(120,120)                        // optional
                .into(photo);

        TextView username=(TextView) rowView.findViewById(R.id.items_list_username);
        username.setText(dev.getUsername());
        return rowView;
    }

    public ArrayList<Dev> getItemsList(){
        return itemsList;
    }

    public void setItemsList(ArrayList<Dev> itemsList){
        this.itemsList=itemsList;
    }
}
