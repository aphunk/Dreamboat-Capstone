package com.ninou.dreamboat;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AppCompatActivity;

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
        import android.widget.Toast;

        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.firebase.Timestamp;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.firestore.CollectionReference;
        import com.google.firebase.firestore.DocumentReference;
        import com.google.firebase.firestore.FirebaseFirestore;
//        import com.google.firebase.storage.FirebaseStorage;
//        import com.google.firebase.storage.StorageReference;
//

        import java.util.ArrayList;
        import java.util.Date;
        import java.util.Locale;

        import model.Journal;
        import util.JournalApi;

public class PostJournalActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PostJournalActivity";
    private static final int REQUEST_CODE_SPEECH_INPUT = 100;
    private Button saveButton;
//    private Button shareButton;
//    private Button interpretButton;
    private ImageButton speakButton;
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
//    private StorageReference storageReference;

    private CollectionReference collectionReference = db.collection("Journal");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_journal);


//        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.save_entry_progress);
        titleEditText = findViewById(R.id.dream_title_text);
        entryEditText = findViewById(R.id.dream_entry_text);


        saveButton = findViewById(R.id.save_button);
        speakButton = findViewById(R.id.speak_button);
//        interpretButton = findViewById(R.id.interpret_button);
//        interpretButton = setOnClickListener(this);
//        shareButton = findViewById(R.id.share_button);
//        shareButton = setOnClickListener(this);

        saveButton.setOnClickListener(this);
        speakButton.setOnClickListener(this);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//
//        switch (item.getItemId()) {
//            case R.id.action_add:
//                //Take users to add Journal
//                if (user != null && firebaseAuth != null) {
//                    startActivity(new Intent(PostJournalActivity.this,
//                            PostJournalActivity.class));
////                    finish();
//                }
//                break;
//            case R.id.action_signout:
//                //sign user out
//                if (user != null && firebaseAuth != null) {
//                    firebaseAuth.signOut();
//
//                    startActivity(new Intent(PostJournalActivity.this,
//                            MainActivity.class));
////                    finish();
//                }
//                break;
//            case R.id.action_home:
//                if (user != null && firebaseAuth != null) {
//                    startActivity(new Intent(PostJournalActivity.this,
//                            MainActivity.class));
//                }
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save_button:
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
        String title = titleEditText.getText().toString().trim();
        String entry = entryEditText.getText().toString().trim();

        progressBar.setVisibility(View.VISIBLE);

        if (!TextUtils.isEmpty(title) &&
                !TextUtils.isEmpty(entry)) {

            Journal journal = new Journal();
            journal.setTitle(title);
            journal.setEntry(entry);
            journal.setTimeAdded(new Timestamp(new Date()));
            journal.setUserId(currentUserId);
//            journal.setUserName(currentUserName);

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
                            Toast.makeText(PostJournalActivity.this, "Not Workingggg", Toast.LENGTH_SHORT).show();
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

