package com.hackaton.visualrecognition.comp;

/**
 * Created by fatih.erol on 26.10.2017.
 */

public interface  ISelectable {

    public boolean isSelected();

    public void setIsSelected(boolean check);

    public String getCode();

    public void setCode(String id);

    public String getText();

    public void setText(String text);
}
