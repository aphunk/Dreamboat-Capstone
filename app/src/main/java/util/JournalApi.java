package util;

import android.app.Application;

public class JournalApi extends Application {
    private String username;
    private String userId;
    private String entryBody;
    private String date;
    private String entryId;

    private static JournalApi instance;

    public static JournalApi getInstance() {
        if (instance == null)
            instance = new JournalApi();
        return instance;
    }

    public JournalApi(){}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEntryId() {
        return entryId;
    }

    public void setEntryId(String entryId) {
        this.entryId = entryId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
