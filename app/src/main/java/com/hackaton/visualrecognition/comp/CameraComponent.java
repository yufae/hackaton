package com.hackaton.visualrecognition.comp;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.hackaton.visualrecognition.R;
import com.hackaton.visualrecognition.view.CameraView;

import static android.content.ContentValues.TAG;

/**
 * Created by fatih.erol on 18.10.2017.
 */

public class CameraComponent {

    private CameraComponentCallBack mCallBack;
    private Activity mActivity;
    private Camera mCamera;
    private CameraView mCameraView;


    private Button mImageCancelButton;
    private Button mImageCaptureButton;
    private Button mCameraCaptureButton;

    private Bitmap mImageCapture;

    private View.OnClickListener mImageCancelOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pauseContinueCamera(false);
        }
    };

    private View.OnClickListener mImageCaptureOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            pauseContinueCamera(false);
            mCallBack.takePicturCallBack(mImageCapture);
        }
    };

    private View.OnClickListener mCameraCaptureOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    pauseContinueCamera(true);
                    onPictureTakenOut(data, camera);
                }
            });

        }
    };

    public CameraComponent(CameraComponentCallBack callBack, Activity activity){
        mCallBack = callBack;
        mActivity = activity;
        create();
    }

    public void create(){

        mActivity.setContentView(R.layout.camera_comp);
        if(checkCameraHardware(mActivity)){
            mCamera = getCameraInstance();
            if(mCamera != null){
                // Create our Preview view and set it as the content of our activity.
                mCameraView = new CameraView(mActivity, mCamera);
                FrameLayout preview = (FrameLayout) mActivity.findViewById(R.id.camera_preview);
                preview.addView(mCameraView);
            }
        }else {
            Log.d(TAG, "camera does not exist");
        }

        mCameraCaptureButton = (Button) mActivity.findViewById(R.id.camera_capture);
        mCameraCaptureButton.setOnClickListener(mCameraCaptureOnClick);

        mImageCaptureButton = (Button) mActivity.findViewById(R.id.image_capture);
        mImageCaptureButton.setOnClickListener(mImageCaptureOnClick);

        mImageCancelButton = (Button) mActivity.findViewById(R.id.image_cancel);
        mImageCancelButton.setOnClickListener(mImageCancelOnClick);

    }

    private void pauseContinueCamera(boolean continuePause){
        mImageCancelButton.setVisibility(continuePause?View.VISIBLE:View.INVISIBLE);
        mImageCaptureButton.setVisibility(continuePause?View.VISIBLE:View.INVISIBLE);
        mCameraCaptureButton.setVisibility(continuePause?View.INVISIBLE:View.VISIBLE);
        if(continuePause)
            mCamera.stopPreview();
        else
            mCamera.startPreview();
    }

    private void onPictureTakenOut(byte[] data, Camera camera) {

        if(mImageCapture != null)
            mImageCapture.recycle();

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        mImageCapture = BitmapFactory.decodeByteArray(data, 0,data.length );

        options.inJustDecodeBounds = false;
    }


    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /** A safe way to get an instance of the Camera object. */
    private  Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            Log.d(TAG, "camera does not exist", e);
        }
        return c; // returns null if camera is unavailable
    }
}
