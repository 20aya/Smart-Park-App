package com.andriod.smartparking1.Classes;

public class Users_O {

    private String Namme, phonenum,passw,emaill,Uid ;
   String type; //attributes

    public Users_O() {
    }



    public Users_O(String Uid , String type , String Namme , String phonenum, String passw, String emaill) {
        this.Uid=Uid;
        this.Namme=Namme;
        this.phonenum = phonenum;
        this.passw = passw;
        this.emaill = emaill;
        this.type= type;
    }
    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getNamme() {
        return Namme;
    }

    public String getPhonenum() {
        return phonenum;
    }

    public String getPassw() {
        return passw;
    }

    public String getEmaill() {
        return emaill;
    }

    public void setNamme(String namme) {
        Namme = namme;
    }

    public void setPhonenum(String phonenum) {
        this.phonenum = phonenum;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }

    public void setEmaill(String emaill) {
        this.emaill = emaill;
    }


}// end of class
