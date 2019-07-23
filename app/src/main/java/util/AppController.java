package util;

import android.app.Application;

public class AppController extends Application {
    private String username;
    private String userId;
    private String entryBody;
    private String date;
    private String entryId;
    private String[] dreamWords;

    private static AppController instance;

    public static AppController getInstance() {
        if (instance == null)
            instance = new AppController();
        return instance;
    }

    public AppController(){}

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


    public void setDreamWords(String[] dreamWords) {
        this.dreamWords = dreamWords;
    }

}
