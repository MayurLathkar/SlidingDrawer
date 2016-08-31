package com.pierfrancescosoffritti.slidingdrawer_sample;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ToggleButton;
import com.github.lzyzsd.circleprogress.DonutProgress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Timer;

public class UserProfileActivity extends Activity implements SurfaceHolder.Callback{
    private final String VIDEO_PATH_NAME = "/mnt/sdcard/VGA_30fps_512vbrate.mp4";
    private MediaRecorder mMediaRecorder;
    private MediaPlayer mMediaPlayer;
    private VideoViewCustom videoView;
    private Uri mVideoUri;
    private boolean cacelled = false, using = true;
    private Camera mCamera;
    private SurfaceView mSurfaceView;
    private SurfaceHolder mHolder;
    private View mToggleButton;
    private ToggleButton save, discard;
    private boolean mInitSuccesful;
    private Timer timer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getWindow().setBackgroundDrawable(null);
        // we shall take the video in landscape orientation

        mSurfaceView = (SurfaceView) findViewById(R.id.surfaceView);
        mHolder = mSurfaceView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        videoView = (VideoViewCustom) findViewById(R.id.videoView);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        imageView = (GifImageView) findViewById(R.id.imageView);
        videoView.setDimensions(displayMetrics.widthPixels, displayMetrics.heightPixels);
        final DonutProgress donutProgress = (DonutProgress) findViewById(R.id.donut_progress);
        mToggleButton = (ToggleButton) findViewById(R.id.toggleRecordingButton);
        save = (ToggleButton) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap = null;
                try {
                    bitmap = new CreateGitTask().execute().get();
                }catch (Exception e){

                }
                //if (bitmap != null)
               //     imageView.setImageBitmap(bitmap);

            }
        });
        discard = (ToggleButton) findViewById(R.id.discard);
        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // toggle video recording
            public void onClick(View v) {
                if (((ToggleButton) v).isChecked()) {
                    mMediaRecorder.start();
                    mToggleButton.setEnabled(false);

                    try {
                        Thread.sleep(6 * 1000);
                        // This will recode for 10 seconds, if you don't want then just remove it.
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mMediaRecorder.stop();
                    mMediaRecorder.reset();
                    playRecordedVideo();
                    mToggleButton.setVisibility(View.GONE);
                    save.setVisibility(View.VISIBLE);
                    discard.setVisibility(View.VISIBLE);
                } else {
                    mMediaRecorder.stop();
                    mMediaRecorder.reset();
                    try {
                        initRecorder(mHolder.getSurface());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setBackgroundDrawable(null);
                videoView.setVisibility(View.GONE);
                mSurfaceView.setVisibility(View.VISIBLE);
            }
        });
    }

    private void playRecordedVideo(){
        mSurfaceView.setVisibility(View.GONE);
        videoView.setVisibility(View.VISIBLE);
        videoView.setVideoPath("/mnt/sdcard/VGA_30fps_512vbrate.mp4");
        videoView.start();
    }


    private class CreateGitTask extends AsyncTask<Void, Void, Bitmap>{
        @Override
        protected Bitmap doInBackground(Void... voids) {
            if (isCancelled() || !isUsing()) {
                return null;
            }
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            Bitmap bitmap = null;
            ArrayList<Bitmap> bitmaps = new ArrayList<>();
            byte [] byteArray = null;
            try {
                retriever.setDataSource(VIDEO_PATH_NAME);
                byte[] data = retriever.getEmbeddedPicture();
                if (!isCancelled() && isUsing()) {
                    if (data != null) {
                        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    }
                    if (bitmap == null) {
                        for (int i = 1000; i< 9000; i = i+1000){
                            bitmaps.add(retriever.getFrameAtTime(i));
                        }
                        bitmap = combineImageIntoBitmap(bitmaps);
                    }
                }
            } catch (IllegalArgumentException e) {
                Log.e("TAG=>", "MediaMetadataRetriever.setDataSource() fail:"
                        + e.getMessage());
            }
            retriever.release();
            return bitmap;
        }
    }

    private Bitmap combineImageIntoBitmap(ArrayList<Bitmap> bitmap){
        int w = 0, h = 0;
        for (int i = 0; i < bitmap.size(); i++) {
            if (i < bitmap.size() - 1) {
                w = bitmap.get(i).getWidth() > bitmap.get(i + 1).getWidth() ? bitmap.get(i).getWidth() : bitmap.get(i + 1).getWidth();
            }
            h += bitmap.get(i).getHeight();
        }

        Bitmap temp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(temp);
        int top = 0;
        for (int i = 0; i < bitmap.size(); i++) {
            Log.d("HTML", "Combine: "+i+"/"+bitmap.size()+1);

            top = (i == 0 ? 0 : top+bitmap.get(i).getHeight());
            canvas.drawBitmap(bitmap.get(i), 0f, top, null);
        }
        return temp;
    }

    private boolean isCancelled(){
        return cacelled;
    }

    private boolean isUsing(){
        return using;
    }

    @Override
    protected void onResume() {
        if (mInitSuccesful){
            mHolder = mSurfaceView.getHolder();
            mHolder.addCallback(UserProfileActivity.this);
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
            try {
                initRecorder(mHolder.getSurface());
            }catch (Exception e){}
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mVideoUri = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }


    /* Init the MediaRecorder, the order the methods are called is vital to
     * its correct functioning */
    private void initRecorder(Surface surface) throws IOException {
        // It is very important to unlock the camera before doing setCamera
        // or it will results in a black preview
        if (mCamera == null) {
            mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            mCamera.setDisplayOrientation(90);
            mCamera.unlock();
        }

        if (mMediaRecorder == null) mMediaRecorder = new MediaRecorder();
        mMediaRecorder.setPreviewDisplay(surface);
        mMediaRecorder.setCamera(mCamera);

        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
        //       mMediaRecorder.setOutputFormat(8);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mMediaRecorder.setVideoEncodingBitRate(512 * 1000);
        mMediaRecorder.setVideoFrameRate(30);
        mMediaRecorder.setVideoSize(640, 480);
        mMediaRecorder.setOutputFile(VIDEO_PATH_NAME);

        try {
            mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            // This is thrown if the previous calls are not called with the
            // proper order
            e.printStackTrace();
        }

        mInitSuccesful = true;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            if(!mInitSuccesful)
                initRecorder(mHolder.getSurface());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        shutdown();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {}

    private void shutdown() {
        // Release MediaRecorder and especially the Camera as it's a shared
        // object that can be used by other applications
        if (mMediaPlayer != null){
            mMediaRecorder.reset();
            mMediaRecorder.release();
            mCamera.release();

            // once the objects have been released they can't be reused
            mMediaRecorder = null;
            mCamera = null;
        }

}}