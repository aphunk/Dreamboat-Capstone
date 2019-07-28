package com.ninou.dreamboat;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

public class InterpretActivity extends AppCompatActivity {
    private static final String TAG = "InterpretTAG";
    private String entryBody;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Terms");

    private String term;
    private String meaning;
    String API_KEY = com.ninou.dreamboat.BuildConfig.ApiKey;

    Client client = new Client("TKKSUFNV4X", API_KEY);
    final Index index = client.getIndex("terms");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_interpret);

        EditText searchTextView = findViewById(R.id.term_title_text);
        final TextView entryBodyTextView = findViewById(R.id.entry_body_textView);
//        final ListView listView = findViewById(R.id.list_view);


        Bundle extrasBundle = getIntent().getExtras();
        if (extrasBundle != null) {
            entryBody = extrasBundle.getString("ENTRY_TEXT");
            Log.d(TAG, "onCreate: " + entryBody);
        }

        String[] words = entryBody.split("\\W+");
        final SpannableString ssEntryBody = new SpannableString(entryBody);
        final ArrayList<String> matchedWords = new ArrayList<>();

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

                            final ArrayList list = new ArrayList<>();
                            for(int i=1; i<hits.length(); i++) {
                                JSONObject hit = hits.getJSONObject(i);
                                String addlWord = jsonObject.getString("word");
                                list.add(addlWord);
                            }

                            ClickableSpan clickableSpan = new ClickableSpan() {
                                        @Override
                                        public void onClick(View view) {
                                            Intent intent = new Intent(InterpretActivity.this, InterpretationViewActivity.class);
                                            intent.putExtra("WORD", topHitWord);
                                            intent.putExtra("MEANING", topHitMeaning);
                                            intent.putParcelableArrayListExtra("ADDL_HITS", list);
                                            startActivity(intent);
                                        }
                                    };

                                    ssEntryBody.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                                    entryBodyTextView.setText(ssEntryBody);
                                    entryBodyTextView.setMovementMethod(LinkMovementMethod.getInstance());
                        }
                        Log.d(TAG, "requestCompleted: " + hits.length());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
//                    matchedWords.add(content.toString());
//                    Log.d(TAG, "onCreate: FOUND WORDS =>" + matchedWords.toArray()[0]);
//                    Log.d(TAG, "requestCompleted: " + content);
                }
            });

        }

        entryBodyTextView.setText(entryBody);




//        collectionReference.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "onComplete: GET REQUEST TO COLLECTION REF COMPLETED");
//                            List<String> list = new ArrayList<>();
//                            for (DocumentSnapshot document : task.getResult()) {
//                                list.add(document.getString("word"));
//                            }
//                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(InterpretActivity.this, android.R.layout.simple_list_item_1, list);
//                            listView.setAdapter(arrayAdapter);
//                        } else {
//                            Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//
//                });


    }

    private static Comparator<String> ALPHABETICAL_ORDER = new Comparator<String>() {
        public int compare(String str1, String str2) {
            int res = String.CASE_INSENSITIVE_ORDER.compare(str1, str2);
            if (res == 0) {
                res = str1.compareTo(str2);
            }
            return res;
        }
    };
}
