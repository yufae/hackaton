package com.hackaton.visualrecognition.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by fatih.erol on 19.10.2017.
 */

public class Class implements Parcelable {

    private String className;
    private float  score;

    public String getClassName(){
        return this.className;
    }

    public void setClassName(String className){
        this.className = className;
    }

    public float getScore(){
        return this.score;
    }

    public void setScore(float score){
        this.score = score;
    }
    public Class(String className, float score){
        this.className = className;
        this.score = score;
    }

    protected Class(Parcel in) {
        this.className = in.readString();
        this.score = in.readFloat();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(className);
        dest.writeFloat(score);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Class> CREATOR = new Creator<Class>() {
        @Override
        public Class createFromParcel(Parcel in) {
            return new Class(in);
        }

        @Override
        public Class[] newArray(int size) {
            return new Class[size];
        }
    };
}
