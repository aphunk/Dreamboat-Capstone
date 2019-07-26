package com.ninou.dreamboat;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.android.BuildConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

import util.AppController;

public class ViewEntryActivity extends AppCompatActivity {
    private static final String TAG = "viewEntryActivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Terms");
    private String title;
    private String entryBody;
    private String date;
    private String currentUserId;

    private TextView entryBodyText;

    private Button editButton;

    final String API_KEY = BuildConfig.ALGOLIA_API_KEY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);

        Client client = new Client("TKKSUFNV4X", API_KEY);
        Index index = client.getIndex("terms");

        editButton = findViewById(R.id.edit_button);
        TextView entryTitle = findViewById(R.id.entry_title);
        TextView entryDate = findViewById(R.id.entry_date);
        TextView userId = findViewById(R.id.user_id_text);
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

        String[] words = entryBody.split("\\W+");
        final SpannableString ssEntryBody = new SpannableString(entryBody);

        for (final String word : words) {
            int wordLength = word.length();
            final int startIndex = entryBody.indexOf(word);
            final int endIndex = startIndex + wordLength;

            collectionReference.whereEqualTo("word", word)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (final QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    ClickableSpan clickableSpan = new ClickableSpan() {
                                        @Override
                                        public void onClick(View view) {
                                            Log.d(TAG, "onClick: IT WAS CLICKED!" );
                                            Log.d(TAG, "onClick: " + document.getString("meaning"));
                                            Intent intent = new Intent(ViewEntryActivity.this, InterpretationViewActivity.class);
                                            intent.putExtra("TERM", document.getString("word"));
                                            intent.putExtra("MEANING", document.getString("meaning"));
                                            startActivity(intent);
                                        }
                                    };
                                    ssEntryBody.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    entryBodyText.setText(ssEntryBody);
                                    entryBodyText.setMovementMethod(LinkMovementMethod.getInstance());
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });

        }
        entryBodyText.setText(ssEntryBody);

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
