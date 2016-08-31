package com.pierfrancescosoffritti.slidingdrawer_sample.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.pierfrancescosoffritti.slidingdrawer_sample.ProfileActivity;
import com.pierfrancescosoffritti.slidingdrawer_sample.R;
import com.pierfrancescosoffritti.slidingdrawer_sample.UserProfileActivity;

import java.util.Random;

/**
 * Created by Dell on 20-08-2016.
 */
public class CustomGridAdapter extends BaseAdapter {

    private Context context;

    public CustomGridAdapter(Context context){
        this.context = context;
    }

    @Override
    public int getCount() {
        return 20;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View gridView ;
        if (i == 0){
            gridView = inflater.inflate(R.layout.grid_add_item, null);
            gridView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, UserProfileActivity.class);
                    context.startActivity(intent);
                }
            });
        }
        else {
            gridView = inflater.inflate(R.layout.grid_single_item, null);
            gridView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("ChatOrCall", "Chat");
                    context.startActivity(intent);
                    return true;
                }
            });
            gridView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ProfileActivity.class);
                    intent.putExtra("ChatOrCall", "Call");
                    context.startActivity(intent);
                }
            });
        }


        int[] androidColors = context.getResources().getIntArray(R.array.androidcolors);
        int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
        gridView.setBackgroundColor(randomAndroidColor);
        return gridView;
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
