package com.ninou.dreamboat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import model.Journal;
import util.AppController;

public class ViewEntryActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String title;
    private String entryBody;
    private String date;
    private String currentUserId;

    private TextView entryTitle;
    private TextView entryDate;
    private TextView entryBodyText;
    private TextView userId;

    private Button editButton;

    private Intent intentExtras;
    private Journal journalEntry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);

        editButton = findViewById(R.id.edit_button);
        entryTitle = findViewById(R.id.entry_title);
        entryDate = findViewById(R.id.entry_date);
        userId = findViewById(R.id.user_id_text);
        entryBodyText = findViewById(R.id.entry_body_text);


        Bundle extrasBundle = getIntent().getExtras();
        if (extrasBundle != null) {
            currentUserId = extrasBundle.getString("CURRENT_USER");
            title = extrasBundle.getString("TITLE");
            entryBody = extrasBundle.getString("ENTRY_TEXT");
            date = extrasBundle.getString("DATE");
        }


        entryTitle.setText(title);
        entryBodyText.setText(entryBody);
        entryDate.setText(date);
        userId.setText(currentUserId);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AppController journalApi = AppController.getInstance(); //Global API
                journalApi.setUserId(currentUserId);
                Intent intent = new Intent(ViewEntryActivity.this,
                        PostJournalActivity.class);
                intent.putExtra("userId", currentUserId);
                startActivity(intent);
            }
        });
    }




    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (firebaseAuth != null) {
            currentUser = firebaseAuth.getCurrentUser();
            firebaseAuth.addAuthStateListener(authStateListener);

        }
    }

}
