package com.pierfrancescosoffritti.slidingdrawer_sample;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.pierfrancescosoffritti.slidingdrawer_sample.adapters.ViewPagerAdapter;
import com.pierfrancescosoffritti.utils.FragmentsUtils;

public class MainActivity extends AppCompatActivity implements NowFragment.OnCustomEventListener{

    private CameraPreview mPreview;
    private BottomSheetBehavior bottomSheetBehavior;
    private TabLayout tabs;
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;
    private Camera camera;
    private final static String TAG_1 = "TAG_1";
    private final static String TAG_2 = "TAG_2";
    private final static String TAG_3 = "TAG_3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setBackgroundDrawable(null);
        tabs = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        View view = findViewById(R.id.bottom_sheet);
        View cameraFragment = findViewById(R.id.fragment);


        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        cameraFragment.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }

                else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
                Log.v("State", ""+bottomSheetBehavior.getState());
                return true;
            }
        });

        bottomSheetBehavior = BottomSheetBehavior.from(view);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                Log.v("state==>",""+newState);
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.v("Slide==>",""+slideOffset);
            }
        });
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setPeekHeight(450);
//        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
//            @Override
//            public void onStateChanged(@NonNull View bottomSheet, int newState) {
//
//            }
//
//            @Override
//            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
//                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED){
//                    getWindow().addFlags(BIND_ADJUST_WITH_ACTIVITY);
//                }
//            }
//        });
        safeCameraOpenInView();
        setupTabsLayout();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus){

        }
    }



    private boolean safeCameraOpenInView(){
        boolean qOpened = false;
        try {
            camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            camera.setDisplayOrientation(90);
            qOpened = (camera != null);
            mPreview = new CameraPreview(MainActivity.this, camera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.fragment);
            preview.addView(mPreview);
        } catch (Exception e){

        }
        return qOpened;
    }

    @Override
    protected void onPause() {
        try {
            camera.release();
        }catch (Exception e){}
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        safeCameraOpenInView();
    }

    private void setupTabsLayout() {
        Fragment listFragment1;
        Fragment listFragment2;
        Fragment listFragment3;

            listFragment1 = FragmentsUtils.findFragment(getSupportFragmentManager(), new NowFragment(), null);
            listFragment2 = FragmentsUtils.findFragment(getSupportFragmentManager(), new RecentFragment(), null);
            listFragment3 = FragmentsUtils.findFragment(getSupportFragmentManager(), new ContactsFragment(), null);

        setupViewPager(tabs, new Pair<>(listFragment1, "Now"), new Pair<>(listFragment2, "Recent"), new Pair<>(listFragment3, "Contacts"));
    }


    @SafeVarargs
    private final void setupViewPager( TabLayout tabs, Pair<Fragment, String>... fragments) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(viewPagerAdapter);

        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void Event(String canDrag){
        if (canDrag.equals("Drag"))
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_DRAGGING);
        else if(canDrag.equals("NoDrag")){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
    }

}
