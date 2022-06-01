package com.andriod.smartparking1.Classes;

public class Picmark {

    double lat66 , long66;
    String spot_id66;
    String pic_urlll;
    String status;
    String priceperhour;

    public Picmark(String spot_id66,double lat66, double long66 , String pic_urlll , String status , String priceperhour) {
        this.lat66 = lat66;
        this.long66 = long66;
        this.spot_id66 = spot_id66;
        this.pic_urlll =pic_urlll;
        this.status = status;
        this.priceperhour = priceperhour;
    }

    public String getPriceperhour() {
        return priceperhour;
    }

    public void setPriceperhour(String priceperhour) {
        this.priceperhour = priceperhour;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPic_urlll() {
        return pic_urlll;
    }

    public void setPic_urlll(String pic_urlll) {
        this.pic_urlll = pic_urlll;
    }

    public double getLat66() {
        return lat66;
    }

    public double getLong66() {
        return long66;
    }

    public String getSpot_id66() {
        return spot_id66;
    }

    public void setLat66(double lat66) {
        this.lat66 = lat66;
    }

    public void setLong66(double long66) {
        this.long66 = long66;
    }

    public void setSpot_id66(String spot_id66) {
        this.spot_id66 = spot_id66;
    }
}//end of class
