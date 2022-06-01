package com.andriod.smartparking1.Classes;

public class Rate {

    float value ;
    String rater_user;
    String owner_spot;

    public Rate(String rater_user , String owner_spot , float  value) {
        this.rater_user = rater_user;
        this.owner_spot = owner_spot;
        this.value = value;
    }//end constr

    public String getOwner_spot() {
        return owner_spot;
    }

    public void setOwner_spot(String owner_spot) {
        this.owner_spot = owner_spot;
    }

    public String getRater_user() {
        return rater_user;
    }

    public void setRater_user(String rater_user) {
        this.rater_user = rater_user;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }
}//end class
