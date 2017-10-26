package com.hackaton.visualrecognition.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.hackaton.cloudant.nosql.Allergen;
import com.hackaton.cloudant.nosql.CloudantConnector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by fatih.erol on 26.10.2017.
 */

public class BaseActivity extends AppCompatActivity{

    private static final String TAG = "Content Values";

    public List<String> getUserAllergens(){

        List<String> result = new ArrayList<String>();
        SharedPreferences sp = getSharedPreferences("UserProfile", 0);
        Set<String> allergensSet = sp.getStringSet("allergenSet", null);
        if(allergensSet != null) {
            Iterator<String> iterator = allergensSet.iterator();
            while (iterator.hasNext()) {
                result.add(iterator.next());
            }
        }
        return result;
    }



    public void saveUserAllergens(List<Allergen> allergenList){

        CloudantConnector cc = new CloudantConnector();
        List<String> allergenStringList = new ArrayList<String>();
        try {
            allergenStringList = cc.getCrossAllergens(allergenList);
        }catch(Exception e){
            Log.d(TAG, e.getMessage(), e);
        }

        if(allergenStringList != null){
            Set<String> allergensSet = new HashSet<String>();
            for(String allergenName : allergenStringList){
                allergensSet.add(allergenName);
            }

            SharedPreferences sp = getSharedPreferences("UserProfile", 0);
            SharedPreferences.Editor editor = sp.edit();
            editor.putStringSet("allergenSet",allergensSet);
            editor.commit();
        }

    }
}
