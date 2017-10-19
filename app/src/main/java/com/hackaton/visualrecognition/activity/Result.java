package com.hackaton.visualrecognition.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hackaton.visualrecognition.R;
import com.hackaton.visualrecognition.data.Class;

public class Result extends AppCompatActivity {

    ArrayAdapter<String> mArrayAdapter;
    String[] mArrayLabels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Parcelable[] parcelables = getIntent().getParcelableArrayExtra("result");

        if(parcelables != null){
            mArrayLabels = new String [parcelables.length];
            for(int i = 0; i < parcelables.length; i++){
                mArrayLabels[i] = ((Class)parcelables[i]).getClassName() + " " + ((Class)parcelables[i]).getScore();
            }

            mArrayAdapter = new ArrayAdapter<String>(this,
                    R.layout.activity_result,R.id.textView_score, mArrayLabels);

            ListView listView = (ListView) findViewById(R.id.listView_scores);
            listView.setAdapter(mArrayAdapter);
        }
    }
}
