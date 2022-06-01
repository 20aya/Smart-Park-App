package com.andriod.smartparking1.Classes;

public class plate_vehicle {
    String userid_norm ;
    String platenum;
    String plate_id;

    public plate_vehicle(String userid_norm , String plate_id , String platenum) {
        this.userid_norm = userid_norm;
        this.platenum= platenum;
        this.plate_id = plate_id;
    }

    public void setPlate_id(String plate_id) {
        this.plate_id = plate_id;
    }

    public String getPlate_id() {
        return plate_id;
    }

    public void setPlatenum(String platenum) {
        this.platenum = platenum;
    }

    public void setUserid_norm(String userid_norm) {
        this.userid_norm = userid_norm;
    }

    public String getPlatenum() {
        return platenum;
    }

    public String getUserid_norm() {
        return userid_norm;
    }
}// end of class
