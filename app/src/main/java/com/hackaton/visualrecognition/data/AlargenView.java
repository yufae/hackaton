package com.hackaton.visualrecognition.data;

import com.hackaton.cloudant.nosql.Allergens;
import com.hackaton.visualrecognition.comp.ISelectable;

/**
 * Created by fatih.erol on 26.10.2017.
 */

public class AlargenView extends Allergens implements ISelectable {


    public AlargenView (){

    }

    public AlargenView (String id, String name){
        this.setId(id);
        this.setText(name);
    }


    private boolean mIselected;

    @Override
    public boolean isSelected() {
        return mIselected;
    }

    @Override
    public void setIsSelected(boolean check) {
        mIselected = check;
    }


    @Override
    public String getId() {
        return this.get_id();
    }

    @Override
    public void setId(String id) {
        this.set_id(id);
    }



    @Override
    public String getText() {
        return this.getName().get(0);
    }

    @Override
    public void setText(String text) {
        this.getName().set(0, text);
    }

}
