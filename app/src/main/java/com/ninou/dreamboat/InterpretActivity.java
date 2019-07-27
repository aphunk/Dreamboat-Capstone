package com.ninou.dreamboat;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.algolia.search.saas.android.BuildConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InterpretActivity extends AppCompatActivity {
    private static final String TAG = "InterpretTAG";
    private String entryBody;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Terms");

    private String term;
    private String meaning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_interpret);

        EditText searchTextView = findViewById(R.id.term_title_text);
        TextView meaningTextView = findViewById(R.id.meaning_text);
        final ListView listView = findViewById(R.id.list_view);


//        Bundle extrasBundle = getIntent().getExtras();
//        if (extrasBundle != null) {
//            entryBody = extrasBundle.getString("ENTRY_TEXT");
//        }

        final String API_KEY = BuildConfig.ALGOLIA_API_KEY;


        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> list = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                list.add(document.getString("word"));
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(InterpretActivity.this, android.R.layout.simple_list_item_1, list);
                            listView.setAdapter(arrayAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }

                });


        Client client = new Client("TKKSUFNV4X", API_KEY);
        final Index index = client.getIndex("terms");

        Query query = new Query("license plate")
                .setAttributesToRetrieve("word", "meaning")
                .setHitsPerPage(50);
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                String TAG = "INTERPRET";

                Log.d(TAG, "requestCompleted: " + content);
            }
        });

//        searchTextView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                Query query = new Query(editable.toString())
//                        .setAttributesToRetrieve("word")
//                        .setHitsPerPage(50);
//                index.searchAsync(query, new CompletionHandler() {
//                    @Override
//                    public void requestCompleted(JSONObject content, AlgoliaException error) {
//                        try {
//                            JSONArray hits = content.getJSONArray("hits");
//                            List<String> list = new ArrayList<>();
//                            for (int i = 0; i < hits.length(); i++) {
//                                JSONObject jsonObject = hits.getJSONObject(i);
//                                String productName = jsonObject.getString("productName");
//                                list.add(productName);
//                            }
//                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(InterpretActivity.this, android.R.layout.simple_list_item_1, list);
//                            listView.setAdapter(arrayAdapter);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//        });

    }
}
