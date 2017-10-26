package com.hackaton.visualrecognition.data;

import com.hackaton.cloudant.nosql.Allergen;
import com.hackaton.visualrecognition.comp.ISelectable;

/**
 * Created by fatih.erol on 26.10.2017.
 */

public class AlargenView  implements ISelectable {


    public AlargenView (){

    }

    public AlargenView (String id, String name){
        this.mAllergen = new Allergen();
        this.setCode(id);
        this.setText(name);
    }

    private Allergen mAllergen;

    private boolean mIselected;

    public AlargenView(Allergen allergen) {
        super();
        this.mAllergen = allergen;
    }

    @Override
    public boolean isSelected() {
        return mIselected;
    }

    @Override
    public void setIsSelected(boolean check) {
        mIselected = check;
    }

    @Override
    public String getCode() {
        return this.mAllergen.getId();
    }

    @Override
    public void setCode(String id) {
        this.mAllergen.setId(id);
    }

    @Override
    public String getText() {
        return this.mAllergen.getName();
    }

    @Override
    public void setText(String text) {
        this.mAllergen.setName(text);
    }


    public Allergen getAllergen(){
        return mAllergen;
    }


}
