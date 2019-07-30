package com.ninou.dreamboat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    String API_KEY = com.ninou.dreamboat.BuildConfig.ApiKey;
    Client client = new Client("TKKSUFNV4X", API_KEY);
    final Index index = client.getIndex("termsTraditionalSearch");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_interpret);


//        arrayAdapter.notifyDataSetChanged();
//        arrayAdapter.clear();

        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setIcon(R.drawable.ic_dreamboatlogopad1);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        supportActionBar.show();


        BottomNavigationView navigation = findViewById(R.id.nav_view);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_add:
                        //Take users to add Journal
                        Intent a = new Intent(SearchActivity.this,
                                PostJournalActivity.class);
                        startActivity(a);
                        finish();
                        break;
//                    case R.id.action_signout:
//                        //sign user out
//                            firebaseAuth.signOut();
//
//                            Intent b = new Intent(JournalListActivity.this,
//                                    MainActivity.class);
//                        startActivity(b);
//                            break;
                    case R.id.action_my_dreamboat:
                        Intent c = new Intent(SearchActivity.this,
                                JournalListActivity.class);
                        startActivity(c);
                        finish();
                        break;
                    case R.id.action_search:
                        Intent d = new Intent(SearchActivity.this, SearchActivity.class);
                        startActivity(d);
                        finish();
                        break;
                }
                return false;
            }
        });



        final EditText editText = findViewById(R.id.search_textView);
        final ListView listView = findViewById(R.id.search_results_listView);



//        final Query query = new Query();
//        query.setAttributesToRetrieve("word", "meaning");
//        query.setAttributesToHighlight("word");
//        query.setHitsPerPage(20);
//
//        query.setQuery(editText.getText());

//        index.searchAsync(query, new CompletionHandler() {
//                    @Override
//                    public void requestCompleted(JSONObject content, AlgoliaException error) {
//                        try {
//                            JSONArray hits = content.getJSONArray("hits");
//                            List<String> list = new ArrayList<>();
//                            for (int i = 0; i < hits.length(); i++) {
//                                JSONObject jsonObject = hits.getJSONObject(i);
//                                String term = jsonObject.getString("word");
//                                list.add(term);
//                            }
//                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_list_item_1, list);
//                            listView.setAdapter(arrayAdapter);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });



        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Query query = new Query(editable.toString())
                        .setAttributesToRetrieve("word", "meaning")
                        .setHitsPerPage(8);
                index.searchAsync(query, new CompletionHandler() {
                    @Override
                    public void requestCompleted(JSONObject content, AlgoliaException error) {
                        try {
                            JSONArray hits = content.getJSONArray("hits");
                            final List<String> list = new ArrayList<>();
                            for (int i = 0; i < hits.length(); i++) {
                                JSONObject jsonObject = hits.getJSONObject(i);
                                final String searchTerm = jsonObject.getString("word");
                                final String returnedMeaning = jsonObject.getString("meaning");
                                list.add(searchTerm);
                                listView.setOnItemClickListener(
                                        new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                list.clear();
                                                Intent intent = new Intent(SearchActivity.this, InterpretationViewActivity.class);
                                                intent.putExtra("WORD", searchTerm);
                                                intent.putExtra("MEANING", returnedMeaning);
                                                startActivity(intent);
                                            }
                                        }
                                );
                            }
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_list_item_1, list);
                            arrayAdapter.addAll(list);
                            listView.setAdapter(arrayAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


    }


}
