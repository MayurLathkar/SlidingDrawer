package com.pierfrancescosoffritti.slidingdrawer_sample;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Random;

/**
 * Created by Dell on 20-08-2016.
 */
public class MyListAdapter extends BaseAdapter {

    private Context context;
    private String tag;

    public MyListAdapter(Context context, String tag){
        this.context = context;
        this.tag = tag;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView;
        if (tag.equals("contacts"))
            itemView = inflater.inflate(R.layout.list_item_contact, null);
        else
            itemView = inflater.inflate(R.layout.list_item_recent, null);
        View randomView = itemView.findViewById(R.id.frame);
        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        randomView.setBackgroundColor(randomAndroidColor);
        return itemView;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }
}
