package com.hackaton.visualrecognition.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.MediaStore;

import com.hackaton.visualrecognition.R;
import com.hackaton.visualrecognition.app.App;
import com.hackaton.visualrecognition.data.Class;
import com.hackaton.visualrecognition.helper.Helper;
import com.hackaton.visualrecognition.service.VisualService;

import java.io.File;
import java.util.Calendar;
import java.util.List;

public class ImageCaptureActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_capture);
        //internet permission
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        //create directory
        Helper.createDirectoryForPictures();
        goToForwardWıthImageCapture();
    }


    private void goToForwardWıthImageCapture(){
        Intent cameraIntent =  new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        App._file = new File(App._dir, String.format("myPhoto_.jpg", Calendar.getInstance().getTimeInMillis()));
        cameraIntent.putExtra (MediaStore.EXTRA_OUTPUT, Uri.fromFile (App._file));
        startActivityForResult(cameraIntent, REQUEST_ACTIVITY_IMAGE_CAPTURE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_ACTIVITY_IMAGE_CAPTURE:{
                onReturnIMageCapture(resultCode, data);
            }
        }
    }

    private void onReturnIMageCapture(int resultCode, Intent data) {

        Bitmap imageBitmap = BitmapFactory.decodeFile(App._file.getAbsolutePath());
        imageBitmap = Bitmap.createScaledBitmap(imageBitmap,  imageBitmap.getWidth()/2, imageBitmap.getHeight()/2, false);

        List<Class> listClass = VisualService.executeBitmap(imageBitmap);
        Parcelable[] arrClass = new Parcelable[listClass.size()];
        listClass.toArray(arrClass);

        Intent resultIntent = new Intent();
        resultIntent.putExtra("result", arrClass);

        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
