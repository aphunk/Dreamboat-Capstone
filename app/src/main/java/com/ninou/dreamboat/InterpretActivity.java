package com.ninou.dreamboat;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.util.Comparator;

public class InterpretActivity extends AppCompatActivity {
    private static final String TAG = "InterpretTAG";
    private String entryBody;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Terms");

    private String term;
    private String meaning;
//    String api_key = BuildConfig.ApiKey;
    String API_KEY = com.ninou.dreamboat.BuildConfig.ApiKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_interpret);

        EditText searchTextView = findViewById(R.id.term_title_text);
        TextView meaningTextView = findViewById(R.id.meaning_text);
        final ListView listView = findViewById(R.id.list_view);


        Bundle extrasBundle = getIntent().getExtras();
        if (extrasBundle != null) {
            entryBody = extrasBundle.getString("ENTRY_TEXT");
            Log.d(TAG, "onCreate: " + entryBody);
        }




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


        Client client = new Client("TKKSUFNV4X", API_KEY);
        final Index index = client.getIndex("terms");
        Query query = new Query("confiding")
                .setAttributesToRetrieve("word", "meaning")
                .setHitsPerPage(50);
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                String TAG = "INTERPRET";

                Log.d(TAG, "requestCompleted: " + content);
            }
        });

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
