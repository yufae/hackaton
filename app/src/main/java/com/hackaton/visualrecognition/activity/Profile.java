package com.hackaton.visualrecognition.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hackaton.cloudant.nosql.CloudantConnector;
import com.hackaton.visualrecognition.R;
import com.hackaton.visualrecognition.data.AlargenView;

import java.util.ArrayList;

public class Profile extends AppCompatActivity {


    private ArrayList<AlargenView> mAlergenList;
    private ArrayAdapter<AlargenView> mAlergenAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    private void intialize(){
        displayListView();
    }


    private void displayListView() {

        CloudantConnector connector = new CloudantConnector();

        mAlergenList = new ArrayList<AlargenView>();
        for(String allergenName : connector.getAllergens()){
            mAlergenList.add(new AlargenView(allergenName, allergenName));
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



    //AllergenViewAdapter
    private class AllergenViewAdapter extends ArrayAdapter<AlargenView> {

        private ArrayList<AlargenView> mAllergenViewList;

        public AllergenViewAdapter(Context context, int textViewResourceId,
                               ArrayList<AlargenView> allergenViewList) {
            super(context, textViewResourceId, allergenViewList);
            this.mAllergenViewList = new ArrayList<AlargenView>();
            this.mAllergenViewList.addAll(allergenViewList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
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
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        AlargenView allergenView = (AlargenView) cb.getTag();
                        Toast.makeText(getApplicationContext(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        allergenView.setIsSelected(cb.isSelected());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            AlargenView alargenView = mAllergenViewList.get(position);
            holder.code.setText(" (" +  alargenView.getId() + ")");
            holder.name.setText(alargenView.getText());
            holder.name.setChecked(alargenView.isSelected());
            holder.name.setTag(alargenView);

            return convertView;

        }

    }

}
