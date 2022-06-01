package com.andriod.smartparking1.Classes;

public class Parkingspot {

   String owner_id , price , spot_type ;
   double latitude,longitude; // for the location
  // String timefrom , timeTo ;
   String linkpic;
   String Spot_id;
   String  status;
   String parcodeurl;

    public String getParcodeurl() {
        return parcodeurl;
    }

    public void setParcodeurl(String parcodeurl) {
        this.parcodeurl = parcodeurl;
    }

    public Parkingspot(){}

   // constructer without pic
   public Parkingspot(String Spot_id , String owner_id , String spot_type , String status , String price ,
    double latitude , double longitude , String parcodeurl) {
       this.Spot_id = Spot_id;
      this.owner_id = owner_id;
      this.spot_type = spot_type ;
      this.status = "avaliable";
      this.price = price ;
      this.latitude = latitude ;
      this.longitude = longitude;
      this.parcodeurl = parcodeurl;
      //this.timefrom =timefrom;
      //this.timeTo = timeTo;
   } // end of constructer



   // constructer with pic
   public Parkingspot(String Spot_id, String owner_id , String spot_type , String status , String price ,
                      double latitude , double longitude
   , String linkpic , String parcodeurl) {
      this.Spot_id = Spot_id;
      this.owner_id = owner_id;
      this.spot_type = spot_type ;
      this.status = "avaliable";
      this.price = price ;
      this.latitude = latitude ;
      this.longitude = longitude;
      this.parcodeurl = parcodeurl;
    //  this.timefrom =timefrom;
     // this.timeTo = timeTo;
      this.linkpic =linkpic;
   } // end of constructer



   public void setLinkpic(String linkpic) {
      this.linkpic = linkpic;
   }

   public String getLinkpic() {
      return linkpic;
   }

   public String getOwner_id() {
      return owner_id;
   }

   public String getStatus() {
      return status;
   }

   public String getPrice() {
      return price;
   }

   public String getSpot_type() {
      return spot_type;
   }

   public double getLatitude() {
      return latitude;
   }

   public double getLongitude() {
      return longitude;
   }

   ///public String getTimefrom() {
     /// return timefrom;
  /// }

  /// public String getTimeTo() {
   ///   return timeTo;
  /// }

   public void setOwner_id(String owner_id) {
      this.owner_id = owner_id;
   }

   public void setStatus(String status) {
      this.status = status;
   }

   public void setPrice(String price) {
      this.price = price;
   }

   public void setSpot_type(String spot_type) {
      this.spot_type = spot_type;
   }

   public void setLatitude(double latitude) {
      this.latitude = latitude;
   }

   public void setLongitude(double longitude) {
      this.longitude = longitude;
   }

  /// public void setTimefrom(String timefrom) {
   ///   this.timefrom = timefrom;
  /// }

   ////public void setTimeTo(String timeTo) {
     /// this.timeTo = timeTo;
  /// }

   public String getSpot_id() {
      return Spot_id;
   }

   public void setSpot_id(String spot_id) {
      Spot_id = spot_id;
   }
}// end of class
