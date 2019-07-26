package com.ninou.dreamboat;

import android.os.Bundle;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.algolia.search.saas.android.BuildConfig;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.Journal;
import ui.JournalRecyclerAdapter;

public class InterpretActivity extends AppCompatActivity {
    private String entryBody;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Terms");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_interpret);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        final String API_KEY = BuildConfig.ALGOLIA_API_KEY;

        Client client = new Client("TKKSUFNV4X", API_KEY);
        Index index = client.getIndex("terms");

        collectionReference.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> list = new ArrayList<>();
                            for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                list.add(documentSnapshot.getString("word"));
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, list);
                            listView.setAdapter(arrayAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                        }

                    }
                });



        Bundle extrasBundle = getIntent().getExtras();
        if (extrasBundle != null) {
            entryBody = extrasBundle.getString("ENTRY_TEXT");
        }

        //////////////////////////////////////////// ALGOLIA /////////////////////////////////////////////////


        // pass entry body as arg for a new query
        Query query = new Query(entryBody)
                .setAttributesToRetrieve("word", "meaning");

        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                String TAG = "INTERPRET";
                Log.d(TAG, "requestCompleted: " + content.toString());
            }
        });


        ////////////////////////////////////////////// ALGOLIA /////////////////////////////////////////////////

    }

}
