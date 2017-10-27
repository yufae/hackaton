
package com.hackaton.cloudant.nosql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Food implements Serializable {
    private String _id;
    private String _rev;
    private String food_name;
    private List<Ingredient> ingredient;
    private List<Ingredient> detectedAllergens = new ArrayList<Ingredient>();

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_rev() {
        return _rev;
    }

    public void set_rev(String _rev) {
        this._rev = _rev;
    }

    public List<Ingredient> getIngredient() {
        return ingredient;
    }

    public void setIngredient(List<Ingredient> ingredient) {
        this.ingredient = ingredient;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }


    public List<Ingredient> getDetectedAllergens() {
        return detectedAllergens;
    }

    public void setDetectedAllergens(List<Ingredient> detectedAllergens) {
        this.detectedAllergens = detectedAllergens;
    }
}
