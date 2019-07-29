package com.ninou.dreamboat;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import model.Journal;
import util.AppController;

import static com.google.firebase.auth.FirebaseAuth.AuthStateListener;
import static com.google.firebase.auth.FirebaseAuth.getInstance;

public class PostJournalActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ALGOLIA";
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
    private Button saveButton;

    private ImageButton speakButton;
    private ProgressBar progressBar;
    private EditText titleEditText;
    private EditText entryEditText;
    private TextView dateTimeDisplay;


    private String currentUserId;
    private String currentUserName;

    private String date;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    private FirebaseAuth firebaseAuth;
    private AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CollectionReference collectionReference = db.collection("Journal");


    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_journal);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setIcon(R.drawable.dreamboat_logo);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        supportActionBar.show();

        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_my_dreamboat:
                        if (currentUser != null && firebaseAuth != null) {
                            Intent a = new Intent(PostJournalActivity.this, JournalListActivity.class);
                            startActivity(a);
                            finish();
                        }
                        break;
                    case R.id.action_add:
                        if (currentUser != null && firebaseAuth != null) {
                            Intent b = new Intent(PostJournalActivity.this, PostJournalActivity.class);
                            startActivity(b);
                            finish();
                        }
                        break;
                    case R.id.action_signout:
                        if (currentUser != null && firebaseAuth != null) {
                            firebaseAuth.signOut();

                            Intent c = new Intent(PostJournalActivity.this,
                                    LoginActivity.class);
                            startActivity(c);
                            finish();
                        }
                        break;

                }
                return false;
            }
        });


        firebaseAuth = getInstance();
        progressBar = findViewById(R.id.save_entry_progress);
        titleEditText = findViewById(R.id.dream_title_text);
        entryEditText = findViewById(R.id.dream_entry_text);
        dateTimeDisplay = findViewById(R.id.entry_date_text);

        calendar = Calendar.getInstance();

        dateFormat = new SimpleDateFormat("EEE, MMM d, YYYY"  );
        date = dateFormat.format(calendar.getTime());
        dateTimeDisplay.setText(date);

        saveButton = findViewById(R.id.save_button);
        speakButton = findViewById(R.id.speak_button);
        saveButton.setOnClickListener(this);
        speakButton.setOnClickListener(this);
        progressBar.setVisibility(View.INVISIBLE);



        if (getInstance() == null) {
            startActivity(new Intent(PostJournalActivity.this, LoginActivity.class));
        }else {
            currentUserId = getInstance().getCurrentUser().getUid();
            currentUserName = AppController.getInstance().getUsername();
        }

        authStateListener = new AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if(currentUser != null) {

                }else {

                }
            }
        };


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_button:
                //get text & perform search
//                String entryBodyText = entryEditText.getText().toString();
//
//                performSearch(entryBodyText);
                //saveJournal
                saveJournal();
                break;
            case R.id.speak_button:
                //initialize speech to text
                promptSpeechInput();

        }
    }


    private void promptSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speech_prompt));
        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    getString(R.string.speech_not_supported),
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    entryEditText.setText(result.get(0));
                }
                break;
            }
        }
    }



    private void saveJournal() {
        final String title = titleEditText.getText().toString().trim();
        final String entry = entryEditText.getText().toString().trim();
        final String date = dateTimeDisplay.getText().toString();


        progressBar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(title) &&
                !TextUtils.isEmpty(entry)) {

            final Journal journal = new Journal();
            journal.setTitle(title);
            journal.setEntry(entry);
            journal.setDate(date);
            journal.setUserId(currentUserId);

            collectionReference.add(journal)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            progressBar.setVisibility(View.INVISIBLE);


                            Intent intent = new Intent(PostJournalActivity.this,
                                    ViewEntryActivity.class);
                                    intent.putExtra("CURRENT_USER", currentUserId);
                                    intent.putExtra("TITLE", title);
                                    intent.putExtra("ENTRY_TEXT", entry);
                                    intent.putExtra("DATE", date);
                            startActivity(intent);

                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PostJournalActivity.this, "Not Workingggg", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onFailure: " + e.getMessage());
                        }
                    });

        }else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    // Menu & State stuff

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_add:
                //Take users to add Journal
                if (currentUser != null && firebaseAuth != null) {
                    startActivity(new Intent(PostJournalActivity.this,
                            PostJournalActivity.class));
//                    finish();
                }
                break;
            case R.id.action_signout:
                //sign user out
                if (currentUser != null && firebaseAuth != null) {
                    firebaseAuth.signOut();

                    startActivity(new Intent(PostJournalActivity.this,
                            MainActivity.class));
//                    finish();
                }
                break;
            case R.id.action_my_dreamboat:
                if (currentUser != null && firebaseAuth != null) {
                    startActivity(new Intent(PostJournalActivity.this,
                            MainActivity.class));
                }
        }
        return super.onOptionsItemSelected(item);
    }
}

