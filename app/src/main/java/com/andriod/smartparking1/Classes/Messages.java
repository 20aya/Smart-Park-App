package com.andriod.smartparking1.Classes;

public class Messages {

    String message , senderID ;
    long timestamp;
public Messages(){}
    public Messages(String message , String senderID ,long timestamp) {
        this.timestamp = timestamp;
        this.message = message;
        this.senderID = senderID;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}//end class
