package com.ninou.dreamboat;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;

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
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(SearchActivity.this, android.R.layout.simple_list_item_1);

        final EditText editText = findViewById(R.id.search_textView);
        final ListView listView = findViewById(R.id.search_results_listView);

        arrayAdapter.clear();

//        final Query query = new Query();
//        query.setAttributesToRetrieve("word", "meaning");
//        query.setAttributesToHighlight("word");
//        query.setHitsPerPage(20);
//
//        query.setQuery(editText.getText());
//
//        index.searchAsync(query, new CompletionHandler() {
//                    @Override
//                    public void requestCompleted(JSONObject content, AlgoliaException error) {
//                        try {
//                            JSONArray hits = content.getJSONArray("hits");
//                            List<String> list = new ArrayList<>();
//                            for (int i = 0; i < hits.length(); i++) {
//                                JSONObject jsonObject = hits.getJSONObject(i);
//                                String productName = jsonObject.getString("word");
//                                list.add(productName);
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
                arrayAdapter.clear();
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
                            List<String> list = new ArrayList<>();
                            for (int i = 0; i < hits.length(); i++) {
                                JSONObject jsonObject = hits.getJSONObject(i);
                                final String searchTerm = jsonObject.getString("word");
                                final String returnedMeaning = jsonObject.getString("meaning");
                                list.add(searchTerm);
                                listView.setOnItemClickListener(
                                        new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                                arrayAdapter.clear();
                                                Intent intent = new Intent(SearchActivity.this, InterpretationViewActivity.class);
                                                intent.putExtra("WORD", searchTerm);
                                                intent.putExtra("MEANING", returnedMeaning);
                                                startActivity(intent);
                                            }
                                        }
                                );
                            }
                            arrayAdapter.addAll(list);
                            listView.setAdapter(arrayAdapter);
                            arrayAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });


    }

}
