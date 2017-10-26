package com.hackaton.visualrecognition.activity;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackaton.cloudant.nosql.CloudantConnector;
import com.hackaton.cloudant.nosql.Food;
import com.hackaton.cloudant.nosql.Ingredient;
import com.hackaton.visualrecognition.R;
import com.hackaton.visualrecognition.data.Class;

import java.util.ArrayList;
import java.util.List;

public class Result extends BaseActivity {

    private static final float MAX_SCORE_TRESHOLD = 0.3f ;
    ArrayAdapter<String> mArrayAdapter;
    Class mFoodClass;
    Food mFood;
    String[] mArrayLabels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Parcelable[] parcelables = getIntent().getParcelableArrayExtra("result");

        mFoodClass =  getFoodClass(parcelables);
        if(mFoodClass != null /*|| parcelables.length > 0*/){
            goToFoodFound();
        }else{
            goToFoodNotFound();
        }

        /*if(parcelables != null){
            mArrayLabels = new String [parcelables.length];
            for(int i = 0; i < parcelables.length; i++){
                mArrayLabels[i] = ((Class)parcelables[i]).getClassName() + " " + ((Class)parcelables[i]).getScore();
            }

            mArrayAdapter = new ArrayAdapter<String>(this,
                    R.layout.activity_result,R.id.textView_score, mArrayLabels);

            ListView listView = (ListView) findViewById(R.id.listView_scores);
            listView.setAdapter(mArrayAdapter);
        }*/
    }


    private Class getFoodClass(Parcelable[] parcelables){
        Class foundClass = null;

        if(parcelables.length == 0 ){
            return foundClass;
        }

        Class maxClass = (Class) parcelables[0];
        int i = 1;
        for(i = 1; i < parcelables.length; i++){
            if( maxClass.getScore() < ((Class)parcelables[i]).getScore()){
                maxClass = (Class)parcelables[i];
            }
        }

        if(((Class)parcelables[i-1]).getScore() > MAX_SCORE_TRESHOLD){
            foundClass = maxClass;
        }
        return foundClass;
    }


    private void goToFoodFound(){

        List<Ingredient> allergenMatchedList  = getMatchIngredientList();
        ImageView resultImage = (ImageView) findViewById(R.id.image_ok);
        TextView foodName = (TextView) findViewById(R.id.text_food);
        TextView allergensName = (TextView) findViewById(R.id.text_allergens);

        foodName.setText(mFood.getFood_name());
        if(allergenMatchedList.size() > 0){
            resultImage.setImageResource(R.drawable.cross_img);
            allergensName.setText(getAllergenNamesCommaFormat(allergenMatchedList));
        }else{
            resultImage.setImageResource(R.drawable.check_img);
            allergensName.setText("Bon Appetit :)");
        }
    }

    private String getAllergenNamesCommaFormat(List<Ingredient> ingredientList){
        StringBuffer sb = new StringBuffer("");
        for(Ingredient i : ingredientList){
            if(!sb.toString().equalsIgnoreCase("")){
                sb.append(",");
            }
            sb.append(i.getName());
        }

        return sb.toString();
    }

    /*

     */
    private List<Ingredient> getMatchIngredientList(){

        List<Ingredient> allergenMatchedList  = new ArrayList<Ingredient>();

        CloudantConnector cc = new CloudantConnector();
        mFood  = cc.getIngridents( mFoodClass.getClassName());
        List<Ingredient> ingredientsList = mFood.getIngredient();
        List<String> allergensList = getUserAllergens();

        for(Ingredient ingredient: ingredientsList) {
            for(String allergen : allergensList){
                if( ingredient.getId().equalsIgnoreCase(allergen)) {
                    allergenMatchedList.add(ingredient);
                }
            }
        }

        return allergenMatchedList;
    }

    private void goToFoodNotFound(){

        finish();
    }
}
