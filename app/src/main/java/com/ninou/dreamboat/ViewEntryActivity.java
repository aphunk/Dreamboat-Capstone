package com.ninou.dreamboat;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    String API_KEY = com.ninou.dreamboat.BuildConfig.ApiKey;

    Client client = new Client("TKKSUFNV4X", API_KEY);
    final Index index = client.getIndex("terms");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setIcon(R.drawable.ic_dreamboatlogopad);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        supportActionBar.show();

        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_my_dreamboat:
                            Intent a = new Intent(ViewEntryActivity.this, JournalListActivity.class);
                            startActivity(a);
                            finish();
                        break;
                    case R.id.action_add:
                            Intent b = new Intent(ViewEntryActivity.this, PostJournalActivity.class);
                            startActivity(b);

                        break;
                    case R.id.action_signout:
                        // TODO: 2019-07-29 Change this to use a search icon & change the id name 
                            Intent c = new Intent(ViewEntryActivity.this,
                                    SearchActivity.class);
                            startActivity(c);
                            break;
                }
                return false;
            }
        });


        editButton = findViewById(R.id.edit_button);
        TextView entryTitle = findViewById(R.id.entry_title);
        TextView entryDate = findViewById(R.id.entry_date);
        entryBodyText = findViewById(R.id.entry_body_text);


        Bundle extrasBundle = getIntent().getExtras();
        if (extrasBundle != null) {
            currentUserId = extrasBundle.getString("CURRENT_USER");
            title = extrasBundle.getString("TITLE");
            entryBody = extrasBundle.getString("ENTRY_TEXT");
            date = extrasBundle.getString("DATE");
        }

        String[] words = entryBody.split("\\W+");
        final SpannableString ssEntryBody = new SpannableString(entryBody);

        for (final String word : words) {
            int wordLength = word.length();
            final int startIndex = entryBody.indexOf(word);
            final int endIndex = startIndex + wordLength;

            Query query = new Query(word)
                    .setAttributesToRetrieve("word", "meaning")
                    .setHitsPerPage(3);
            index.searchAsync(query, new CompletionHandler() {
                @Override
                public void requestCompleted(JSONObject content, AlgoliaException error) {
                    try {
                        JSONArray hits = content.getJSONArray("hits");

                        if (hits.length() > 0) {
                            JSONObject jsonObject = hits.getJSONObject(0);
                            final String topHitWord = jsonObject.getString("word");
                            final String topHitMeaning = jsonObject.getString("meaning");


                            ClickableSpan clickableSpan = new ClickableSpan() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(ViewEntryActivity.this, InterpretationViewActivity.class);
                                    intent.putExtra("WORD", topHitWord);
                                    intent.putExtra("MEANING", topHitMeaning);
                                    startActivity(intent);
                                }
                            };
                            ssEntryBody.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            entryBodyText.setText(ssEntryBody);
                            entryBodyText.setMovementMethod(LinkMovementMethod.getInstance());
                        }
                        Log.d(TAG, "requestCompleted: " + hits.length());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


        }

        entryTitle.setText(title);
        entryBodyText.setText(entryBody);
        entryDate.setText(date);


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
// TODO: 2019-07-29 add Extras Receiver in Post Journal activity 
                AppController journalApi = AppController.getInstance(); //Global API
                journalApi.setUserId(currentUserId);
                Intent intent = new Intent(ViewEntryActivity.this,
                        PostJournalActivity.class);
                intent.putExtra("userId", currentUserId);
                intent.putExtra("ENTRY_BODY", entryBody);
                intent.putExtra("TITLE", title);
                intent.putExtra("DATE", date);
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
            FirebaseUser currentUser = firebaseAuth.getCurrentUser();
            firebaseAuth.addAuthStateListener(authStateListener);

        }
    }

}
