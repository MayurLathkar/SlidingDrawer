package com.pierfrancescosoffritti.slidingdrawer_sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

import com.pierfrancescosoffritti.slidingdrawer_sample.adapters.CustomGridAdapter;

/**
 * Created by Dell on 21-08-2016.
 */
public class NowFragment extends android.support.v4.app.Fragment {

    private OnCustomEventListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_now, null);
        final GridView gridView = (GridView) view.findViewById(R.id.gridview);
        gridView.setAdapter(new CustomGridAdapter(getContext()));
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int i1, int i2) {
                if (firstVisibleItem == 1) {
                    // check if we reached the top or bottom of the list
                    View v = gridView.getChildAt(0);
                    int offset = (v == null) ? 1 : v.getTop();
                    if (offset == 1) {
                        mListener.Event("Drag");
                        return;
                    } else {
                        mListener.Event("NoDrag");
                    }
                }
            }
        });
        return view;
    }

    public interface OnCustomEventListener{
        public void Event(String canDrag);
    }

    public void setCustomEventListener(OnCustomEventListener eventListener) {
        mListener = eventListener;
    }
}
