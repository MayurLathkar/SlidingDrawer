package com.pierfrancescosoffritti.slidingdrawer_sample;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pierfrancescosoffritti.slidingdrawer_sample.adapters.ViewPagerAdapter;
import com.pierfrancescosoffritti.utils.FragmentsUtils;

public class RootFragment extends Fragment {

    private final static String TAG_1 = "TAG_1";
    private final static String TAG_2 = "TAG_2";
    private final static String TAG_3 = "TAG_3";
    private ViewPagerAdapter viewPagerAdapter;
    private TabLayout tabLayout;

    public RootFragment() {
    }

    public static RootFragment newInstance() {
        return new RootFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_root, container, false);

        tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);

        Fragment listFragment1;
        Fragment listFragment2;
        Fragment listFragment3;

        if(savedInstanceState == null) {
            listFragment1 = FragmentsUtils.findFragment(getChildFragmentManager(), new NowFragment(), null);
            listFragment2 = FragmentsUtils.findFragment(getChildFragmentManager(), new RecentFragment(), null);
            listFragment3 = FragmentsUtils.findFragment(getChildFragmentManager(), new ContactsFragment(), null);
        } else {
            String tag0 = savedInstanceState.getString(TAG_1);
            String tag1 = savedInstanceState.getString(TAG_2);
            String tag2 = savedInstanceState.getString(TAG_3);

            listFragment1 = FragmentsUtils.findFragment(getChildFragmentManager(), new NowFragment(), tag0);
            listFragment2 = FragmentsUtils.findFragment(getChildFragmentManager(), new RecentFragment(), tag1);
            listFragment3 = FragmentsUtils.findFragment(getChildFragmentManager(), new ContactsFragment(), tag2);
        }

        setupViewPager(view, tabLayout, new Pair<>(listFragment1, "Now"), new Pair<>(listFragment2, "Recent"), new Pair<>(listFragment3, "Contacts"));

        return view;
    }

    @Override
    public void onSaveInstanceState (Bundle outState) {
        super.onSaveInstanceState(outState);

        if(viewPagerAdapter != null) {
            outState.putString(TAG_1, viewPagerAdapter.getItem(0).getTag());
            outState.putString(TAG_2, viewPagerAdapter.getItem(1).getTag());
            outState.putString(TAG_3, viewPagerAdapter.getItem(2).getTag());
        } else {
            outState.putString(TAG_1, TAG_1);
            outState.putString(TAG_2, TAG_2);
            outState.putString(TAG_3, TAG_3);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof SlidingDrawerContainer) {
//            slidingDrawerContainer = (SlidingDrawerContainer) context;
//        } else {
//            throw new RuntimeException(context.getClass().getSimpleName() +" must implement " +SlidingDrawerContainer.class.getSimpleName());
//        }
//    }

    @SafeVarargs
    private final void setupViewPager(View view, TabLayout tabs, Pair<Fragment, String>... fragments) {
        viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragments);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(viewPagerAdapter);

        tabs.setupWithViewPager(viewPager);
    }


}
