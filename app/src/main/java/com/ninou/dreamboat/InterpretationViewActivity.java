package com.ninou.dreamboat;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

import model.Meaning;
import ui.InterpretationRecyclerAdapter;

public class InterpretationViewActivity extends AppCompatActivity {
    private static final String TAG = "ADDITIONAL HITS =>";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Meaning> meaningList;
    private RecyclerView recyclerView;
    private InterpretationRecyclerAdapter interpretationRecyclerAdapter;

    private Image mainImage;


    private CollectionReference collectionReference = db.collection("Terms");

    String API_KEY = com.ninou.dreamboat.BuildConfig.ApiKey;

    Client client = new Client("TKKSUFNV4X", API_KEY);
    final Index index = client.getIndex("terms");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interpretation_list);

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
                            Intent a = new Intent(InterpretationViewActivity.this, JournalListActivity.class);
                            startActivity(a);
                            finish();
                        }
                        break;
                    case R.id.action_add:
                        if (currentUser != null && firebaseAuth != null) {
                            Intent b = new Intent(InterpretationViewActivity.this, PostJournalActivity.class);
                            startActivity(b);
                        }
                        break;
                    case R.id.action_signout:
                        if (currentUser != null && firebaseAuth != null) {
                            firebaseAuth.signOut();

                            Intent c = new Intent(InterpretationViewActivity.this,
                                    LoginActivity.class);
                            startActivity(c);
                            break;
                        }
                }
                return false;
            }
        });


        ImageView mainImage = findViewById(R.id.term_imageView);
        TextView titleText = findViewById(R.id.top_hit_word_textView);
        TextView meaningText = findViewById(R.id.top_hit_meaning_textView);
        final ListView addlHitsListView = findViewById(R.id.addtl_hits_listView);
        final View additionalHitsLayout = findViewById(R.id.additional_hits_layout);


        Bundle extrasBundle = getIntent().getExtras();
        String term = extrasBundle.getString("WORD");
        String meaning = extrasBundle.getString("MEANING");
//        ArrayList list = getIntent().getParcelableArrayListExtra("ADDL_HITS");

        titleText.setText(term);
        meaningText.setText(meaning);

        additionalHitsLayout.setVisibility(View.INVISIBLE);
        Query query = new Query(term)
                .setAttributesToRetrieve("word", "meaning")
                .setHitsPerPage(30);
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                try {
                    JSONArray hits = content.getJSONArray("hits");
                    if (hits.length() > 1) {
                        List<String> list = new ArrayList<>();
                        for (int i = 1; i < hits.length(); i++) {
                            JSONObject jsonObject = hits.getJSONObject(i);
                            String addlWord = jsonObject.getString("word");
                            list.add(addlWord);
                        }
                        ArrayAdapter arrayAdapter =
                                new ArrayAdapter<>(InterpretationViewActivity.this,
                                        android.R.layout.simple_list_item_1, list);
                        addlHitsListView.setAdapter(arrayAdapter);

                        additionalHitsLayout.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
