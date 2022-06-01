package com.andriod.smartparking1.Classes;

public class BookedPark {
    String booker_id ;
    String ownerid_;
    String Spotid_;
    String time_from;
    String time_to ;
    String day;
    String full_price;
    String statusbooked;
    String platenumber;
    public BookedPark(String booker_id , String ownerid_ , String spotid_ , String time_from ,
                      String time_to , String day , String full_price ,  String statusbooked
   , String platenumber ) {
        this.booker_id = booker_id;
        this.Spotid_ = spotid_;
        this.ownerid_ = ownerid_;
        this.time_from = time_from;
        this.time_to = time_to;
        this.day = day ;
        this.full_price = full_price;
        this.statusbooked = statusbooked;
        this.platenumber = platenumber;
    }

    public String getPlatenumber() {
        return platenumber;
    }

    public void setPlatenumber(String platenumber) {
        this.platenumber = platenumber;
    }

    public String getStatusbooked() {
        return statusbooked;
    }

    public void setStatusbooked(String statusbooked) {
        this.statusbooked = statusbooked;
    }

    public String getFull_price() {
        return full_price;
    }

    public void setFull_price(String full_price) {
        this.full_price = full_price;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime_to() {
        return time_to;
    }

    public void setTime_to(String time_to) {
        this.time_to = time_to;
    }

    public String getTime_from() {
        return time_from;
    }

    public void setTime_from(String time_from) {
        this.time_from = time_from;
    }

    public String getBooker_id() {
        return booker_id;
    }

    public String getSpotid_() {
        return Spotid_;
    }

    public void setSpotid_(String spot_id) {
        Spotid_ = spot_id;
    }

    public String getOwnerid_() {
        return ownerid_;
    }

    public void setOwnerid_(String owner_id) {
        this.ownerid_ = owner_id;
    }

    public void setBooker_id(String booker_id) {
        this.booker_id = booker_id;
    }
}//end of class
