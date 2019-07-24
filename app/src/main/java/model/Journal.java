package model;


public class Journal {
    private String title;
    private String entry;
    private String userId;
    private String userName;
    private String entryId;

private String date;


    public Journal() { //must for Firestore to work

    }

    public Journal(String title, String entry, String userId, String date, String entryId) {
        this.title = title;
        this.entry = entry;
        this.userId = userId;
        this.date = date;
        this.entryId = entryId;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
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



