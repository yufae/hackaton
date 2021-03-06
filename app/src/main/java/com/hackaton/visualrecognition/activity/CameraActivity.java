package com.hackaton.visualrecognition.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hackaton.visualrecognition.comp.CameraComponent;
import com.hackaton.visualrecognition.comp.CameraComponentCallBack;
import com.hackaton.visualrecognition.data.Class;
import com.ibm.watson.developer_cloud.visual_recognition.v3.VisualRecognition;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifierResult;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CameraActivity extends AppCompatActivity implements CameraComponentCallBack {

    private CameraComponent mCameraComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCameraComponent = new CameraComponent(this, this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    @Override
    public void takePicturCallBack(Bitmap picture) {

        if( picture != null) {
            InputStream ioStream = convertBitmapToInputstream(picture);

            VisualRecognition service = new VisualRecognition(
                    VisualRecognition.VERSION_DATE_2016_05_20
            );
            service.setApiKey("721bc58130a2e8f4b010394a3476f74aa30b79b1");
            ClassifyOptions classifyOptions = new ClassifyOptions.Builder()
                    .imagesFile(ioStream)
                    .imagesFilename("food.jpeg")
                    .parameters("{\"classifier_ids\": [\"Food_DB_1509108873\"]}")
                    .build();

            ClassifiedImages result = null;
            try{
                result = service.classify(classifyOptions).execute();
            }catch (Exception e){
                Log.d(TAG, e.getMessage(), e);
            }

            //result.getImages().get(0).getClassifiers().get(0).getClasses().get(0);
            System.out.println(result);

            try {
                ioStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            List<Class> listClass = parseImageClassData(result);
            Parcelable[] arrClass = new Parcelable[listClass.size()];
            listClass.toArray(arrClass);

            Intent resultIntent = new Intent();
            resultIntent.putExtra("result", arrClass);

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        }
    }

    private List<Class> parseImageClassData(ClassifiedImages data){
        List<Class > listClass = new ArrayList<Class>();
        if(data != null){
            if(data.getImages().size() > 0){
                for (ClassifierResult classifier :data.getImages().get(0).getClassifiers()){
                    for(ClassResult clas: classifier.getClasses()){
                        listClass.add(new Class(clas.getClassName(), clas.getScore()));
                    }
                }
            }
        }
        return listClass;
    }

    private InputStream convertBitmapToInputstream(Bitmap bitmap){
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }
}
