package com.pierfrancescosoffritti.slidingdrawer_sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * Created by Dell on 20-08-2016.
 */
public class ContactsFragment extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contacts, null);
        ListView listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new MyListAdapter(getContext(), "contacts"));
        return view;
    }
}
