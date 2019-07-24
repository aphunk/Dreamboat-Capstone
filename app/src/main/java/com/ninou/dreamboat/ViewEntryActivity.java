package com.ninou.dreamboat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.Query;

import util.AppController;

public class ViewEntryActivity extends AppCompatActivity {
    private static final String TAG = "viewEntryActivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Terms");
    private DocumentSnapshot documentSnapshot;
    private String title;
    private String entryBody;
    private String date;
    private String currentUserId;
    private String[] words;
    private String[] foundTerms;

    private TextView entryTitle;
    private TextView entryDate;
    private TextView entryBodyText;
    private TextView userId;

    private Button editButton;




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


//        final ForegroundColorSpan fcsBlue = new ForegroundColorSpan(Color.CYAN);
        words = entryBody.split("\\W+");

//        references = [];

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
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d(TAG, document.getId() + " => " + document.getData());
                                    ssEntryBody.setSpan(new ForegroundColorSpan(Color.CYAN), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    entryBodyText.setText(ssEntryBody);
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });


        }

        entryBodyText.setText(ssEntryBody);






//        System.out.println(termsReference);

        // iterate over words in the Entry
//        for (int i = 0; i < words.length; i++) {
//            Log.d(TAG, "onCreate: " +termsReference.whereEqualTo("word", words[i]));
//
//        }


//        SpannableString ss = new SpannableString(entryBody);
//        ForegroundColorSpan fcsBlue = new ForegroundColorSpan(Color.CYAN);
//
//        ss.setSpan(fcsBlue, 7, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        entryBodyText.setText(ss);


        // Edit post button
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
