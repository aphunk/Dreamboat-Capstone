package com.ninou.dreamboat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.ninou.dreamboat.util.JournalApi;

import org.w3c.dom.Text;

import java.util.Date;

import model.Journal;

public class PostJournalActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PostJournalActivity";
    private Button saveButton;
    private Button shareButton;
    private Button interpretButton;
    private ProgressBar progressBar;
    private EditText titleEditText;
    private EditText entryEditText;


    private String currentUserId;
    private String currentUserName;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    //Connection to Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private CollectionReference collectionReference = db.collection("Journal");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_journal);

        firebaseAuth.getInstance();
        progressBar = findViewById(R.id.save_entry_progress);
        titleEditText = findViewById(R.id.dream_title_text);
        entryEditText = findViewById(R.id.dream_entry_text);

        saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);
        interpretButton = findViewById(R.id.interpret_button);
//        interpretButton = setOnClickListener(this);
        shareButton = findViewById(R.id.share_button);
//        shareButton = setOnClickListener(this);

        progressBar.setVisibility(View.INVISIBLE);

        if (JournalApi.getInstance() != null) {
            currentUserId = JournalApi.getInstance().getUserId();
            currentUserName = JournalApi.getInstance().getUsername();

        }

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if(user != null) {

                }else {

                }
            }
        };
    }

    @Override
    public void onClick(View view) {
       switch (view.getId()) {
           case R.id.save_button:
           //saveJournal
               saveJournal();
               break;

               
       }
    }

    private void saveJournal() {
        String title = titleEditText.getText().toString().trim();
        String entry = entryEditText.getText().toString().trim();


        if (!TextUtils.isEmpty(title) &&
                !TextUtils.isEmpty(entry)) {

            Journal journal = new Journal();
            journal.setTitle(title);
            journal.setEntry(entry);
            journal.setTimeAdded(new Timestamp(new Date()));
            journal.setUserId(currentUserId);

            collectionReference.add(journal)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressBar.setVisibility(View.INVISIBLE);
                            startActivity(new Intent(PostJournalActivity.this,
                                    JournalListActivity.class));
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d(TAG, "onFailure: " + e.getMessage());
                        }
                    });

        }else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
