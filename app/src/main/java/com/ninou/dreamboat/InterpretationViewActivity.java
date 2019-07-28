package com.ninou.dreamboat;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interpretation_list);


        Bundle extrasBundle = getIntent().getExtras();
        String term = extrasBundle.getString("WORD");
        String meaning = extrasBundle.getString("MEANING");
        ArrayList list = getIntent().getParcelableArrayListExtra("ADDL_HITS");

        Log.d(TAG, "requestCompleted: " + list);

        final ArrayAdapter arrayAdapter = new ArrayAdapter<String>(InterpretationViewActivity.this, android.R.layout.simple_expandable_list_item_1, list);


        ImageView mainImage = findViewById(R.id.term_imageView);
        TextView titleText = findViewById(R.id.top_hit_word_textView);
        TextView meaningText = findViewById(R.id.top_hit_meaning_textView);
        ListView addlHitsListView = findViewById(R.id.addtl_hits_listView);

        titleText.setText(term);
        meaningText.setText(meaning);
        addlHitsListView.setAdapter(arrayAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
