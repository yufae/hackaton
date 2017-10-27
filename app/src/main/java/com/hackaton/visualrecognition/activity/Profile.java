package com.hackaton.visualrecognition.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hackaton.chatbot.activity.ChatActivity;
import com.hackaton.cloudant.nosql.Allergen;
import com.hackaton.cloudant.nosql.CloudantConnector;
import com.hackaton.visualrecognition.R;
import com.hackaton.visualrecognition.data.AlargenView;
import com.hackaton.visualrecognition.data.Class;

import java.util.ArrayList;
import java.util.List;

public class Profile extends BaseActivity {



    private ArrayList<AlargenView> mAlergenList;
    private ArrayAdapter<AlargenView> mAlergenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //internet permission
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //check camera
        if(!checkCameraRequested()){
            requestCamera();
        }else{
            //goToCameraActivity();
        }
        //initialize elements
        intialize();
    }

    public boolean checkCameraRequested(){
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestCamera(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE},
                MY_PERMISSIONS_REQUEST_READ_CONTACTS);

    }

    private void intialize(){

        //prepare save button
        initializeSaveButton();
        //prepare alergen list view
        displayListView();
        //populate alergens of the profile
        populateAllergens();
    }

    private void initializeSaveButton(){
        Button buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickSaveButton();
            }
        });
    }


    private void displayListView() {

        CloudantConnector connector = new CloudantConnector();

        mAlergenList = new ArrayList<AlargenView>();
        List<Allergen> allergenList = null;
        try{
            allergenList = connector.getAllergens();
        } catch(Exception e){
            Log.d(TAG, "hata var "  + e.getMessage(), e);
        }
        for(Allergen allergen : allergenList){
            mAlergenList.add(new AlargenView(allergen));
        }

        //create an ArrayAdaptar from the String Array
        mAlergenAdapter = new AllergenViewAdapter(this,
                R.layout.activity_profile, mAlergenList);
        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(mAlergenAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                AlargenView alargenView = (AlargenView) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + alargenView.getText(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }


    private void populateAllergens(){
        List<String> allergenList = getUserAllergens();
        for (String allergenName : allergenList){
            for(AlargenView allergenView : mAlergenList){
                if(allergenView.getCode().equalsIgnoreCase(allergenName)){
                    allergenView.setIsSelected(true);
                }
            }
        }
    }

    public void onClickSaveButton(){
        List<Allergen> selectedAllergenList = new ArrayList<Allergen>();
        for (AlargenView all : mAlergenList){
            if(all.isSelected()){
                selectedAllergenList.add(all.getAllergen());
            }
        }

        saveUserAllergens(selectedAllergenList);
        //goToForward();
        goToForwardWıthImageCapture();
    }

    private void goToForward(){
        Intent intent  = new Intent(this, CameraActivity.class);
        startActivityForResult(intent, REQUEST_ACTIVITY_CAMERA);
    }

    private void goToForwardWıthImageCapture(){
        Intent intent  = new Intent(this, ImageCaptureActivity.class);
        startActivityForResult(intent, REQUEST_ACTIVITY_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_ACTIVITY_CAMERA:{
                onReturnActivityCamera(resultCode, data);
            }
            case REQUEST_ACTIVITY_RESULT:{
                onReturnActivityResultScreen(resultCode, data);
            }
            case REQUEST_ACTIVITY_IMAGE_CAPTURE:{
                onReturnIMageCapture(resultCode, data);
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


        if(result.length > 0){
            Intent resultScreenIntent = new Intent(this, Result.class);
            resultScreenIntent.putExtra("result", result);
            startActivityForResult(resultScreenIntent, REQUEST_ACTIVITY_RESULT);
        }else{
            Intent chatBotIntent = new Intent(this, ChatActivity.class);
            startActivityForResult(chatBotIntent, REQUEST_ACTIVITY_CHATBOT);
        }
    }

    private void onReturnActivityResultScreen(int resultCode, Intent data){
        goToForwardWıthImageCapture();
    }

    private void onReturnIMageCapture(int resultCode, Intent data){
        //
        onReturnActivityCamera(resultCode, data);
    }



    //AllergenViewAdapter
    private class AllergenViewAdapter extends ArrayAdapter<AlargenView> {

        private ArrayList<AlargenView> mAllergenViewList;

        public AllergenViewAdapter(Context context, int textViewResourceId,
                               ArrayList<AlargenView> allergenViewList) {
            super(context, textViewResourceId, allergenViewList);
            //this.mAllergenViewList = new ArrayList<AlargenView>();
            //this.mAllergenViewList.addAll(allergenViewList);
            this.mAllergenViewList = allergenViewList;
        }

        private class ViewHolder {
            TextView code;
            Switch name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.list_alergens, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (Switch) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        Switch cb = (Switch) v ;
                        AlargenView allergenView = (AlargenView) cb.getTag();
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        allergenView.setIsSelected(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            AlargenView alargenView = mAllergenViewList.get(position);
            holder.code.setText(alargenView.getText());
            holder.name.setText("");
            holder.name.setChecked(alargenView.isSelected());
            holder.name.setTag(alargenView);

            return convertView;

        }

    }

}
