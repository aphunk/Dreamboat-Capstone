package model;

import com.google.firebase.Timestamp;

public class Journal {
    private String title;
    private String entry;
    private String userId;
    private String userName;

private String date;


    public Journal() { //must for Firestore to work

    }

    public Journal(String title, String entry, String userId, String date) {
        this.title = title;
        this.entry = entry;
        this.userId = userId;
        this.userName = userName;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEntry() {
        return entry;
    }

    public void setEntry(String entry) {
        this.entry = entry;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}



