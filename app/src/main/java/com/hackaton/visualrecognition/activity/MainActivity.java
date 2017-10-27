package com.hackaton.visualrecognition.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.hackaton.visualrecognition.R;
import com.hackaton.visualrecognition.data.Class;

import static android.content.ContentValues.TAG;


public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 100;

    private Button mStartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!checkCameraRequested()){
            requestCamera();
        }else{
            //goToCameraActivity();
        }

        mStartButton = (Button)findViewById(R.id.button_start);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToCameraActivity();
            }
        });
    }


    public boolean checkCameraRequested(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestCamera(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                                                             Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                                    MY_PERMISSIONS_REQUEST_READ_CONTACTS);

    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "requestCode is " + requestCode);
        for(String permission : permissions){
            Log.d(TAG, "permission is " + permission);
        }

        for(int grantResult : grantResults){
            Log.d(TAG, "grantResult is " + grantResult);
        }

        //goToCameraActivity();
    }

    private void goToCameraActivity(){
        Intent cameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(cameraIntent, 1000);
    }

    private void goToResultScreenActivity(){
        Intent cameraIntent = new Intent(this, CameraActivity.class);
        startActivityForResult(cameraIntent, 1001);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1000:{
                onReturnActivityCamera(resultCode, data);
            }
            case 1001:{
                onReturnActivityResultScreen(resultCode, data);
            }
        }
    }

    private void onReturnActivityCamera(int resultCode, Intent data){
        Parcelable[] result = data.getParcelableArrayExtra("result");
        if(result != null){
            for(Parcelable p : result){
                Log.d(TAG, ((Class)p).getClassName());
            }
        }


        Intent resultScreenIntent = new Intent(this, Result.class);
        resultScreenIntent.putExtra("result", result);
        startActivityForResult(resultScreenIntent, 1001);
    }

    private void onReturnActivityResultScreen(int resultCode, Intent data){

    }
}
