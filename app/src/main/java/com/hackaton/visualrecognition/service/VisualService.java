package com.hackaton.visualrecognition.service;

import android.graphics.Bitmap;
import android.util.Log;

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

/**
 * Created by fatih.erol on 26.10.2017.
 */

public class VisualService {


    public static List<Class> executeBitmap(Bitmap picture) {

        List<Class> listClass = null;
        if (picture != null) {
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
            try {
                result = service.classify(classifyOptions).execute();
            } catch (Exception e) {
                Log.d(TAG, e.getMessage(), e);
            }

            //result.getImages().get(0).getClassifiers().get(0).getClasses().get(0);
            System.out.println(result);
            Log.i(TAG, result.toString());

            try {
                ioStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

             listClass = parseImageClassData(result);
        }
        return  listClass;
    }
    private static List<Class> parseImageClassData(ClassifiedImages data) {
        List<Class> listClass = new ArrayList<Class>();
        if (data != null) {
            if (data.getImages().size() > 0) {
                for (ClassifierResult classifier : data.getImages().get(0).getClassifiers()) {
                    for (ClassResult clas : classifier.getClasses()) {
                        listClass.add(new Class(clas.getClassName(), clas.getScore()));
                    }
                }
            }
        }
        return listClass;
    }

    private static InputStream convertBitmapToInputstream(Bitmap bitmap){
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
