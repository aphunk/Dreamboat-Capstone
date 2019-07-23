package com.ninou.dreamboat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.core.Query;

import org.w3c.dom.Document;

import java.util.ArrayList;

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
    private DocumentReference docRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);

        final ArrayList<String> termsArray = new ArrayList<>();


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


        final SpannableString ss = new SpannableString(entryBody);
        final ForegroundColorSpan fcsBlue = new ForegroundColorSpan(Color.CYAN);
        words = entryBody.split(" ");

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

        entryTitle.setText(title);
        entryBodyText.setText(entryBody);
        entryDate.setText(date);
        userId.setText(currentUserId);

        for (int i = 0; i < words.length; i++) {
            int wordLength = words[i].length();
            final int startIndex = entryBody.indexOf(words[i]);
            final int endIndex = startIndex + wordLength;

// todo -- find a way to keep track of all terms that need to be underlined (set text outside of the loop)


            final ArrayList<String> termsArray = new ArrayList<>();



//            collectionReference.whereEqualTo("word", words[i])
//                    .get()
//                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            if (task.isSuccessful()) {
//                                ss.setSpan(fcsBlue, startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
//                                entryBodyText.setText(ss);
////                                String word = words.toString().substring(startIndex, endIndex);
////                                System.out.println("*********" + word);
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                }
//                            } else {
//                                Log.d(TAG, "Error getting documents: ", task.getException());
//                            }
//                        }
//                    });

            }
        }



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


//    public void addTermsArray() {
//        final ArrayList<String> termsArray = new ArrayList<>();
//        DocumentReference docRef = collectionReference.document("Terms");
//        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()) {
//                    DocumentSnapshot document = task.getResult();
//                    if (document != null) {
//                        termsArray.add(document.getString("word"));
//                        System.out.println("***********I'M A TERMS ARRAY!!" + termsArray);
//                    }else {
//                        Log.d("LOGGER", "get failed with ", task.getException());
//                    }
//                }
//            }
//        });
//    }


        @Override
        protected void onStart () {
            super.onStart();
        }

        @Override
        protected void onStop () {
            super.onStop();
            if (firebaseAuth != null) {
                firebaseAuth.removeAuthStateListener(authStateListener);
            }
        }

        @Override
        protected void onPause () {
            super.onPause();
            if (firebaseAuth != null) {
                firebaseAuth.removeAuthStateListener(authStateListener);
            }
        }

        @Override
        protected void onResume () {
            super.onResume();
            if (firebaseAuth != null) {
                currentUser = firebaseAuth.getCurrentUser();
                firebaseAuth.addAuthStateListener(authStateListener);

            }
        }

}
