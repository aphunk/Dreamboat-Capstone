package com.ninou.dreamboat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import io.opencensus.stats.Aggregation;
import model.Meaning;
import ui.InterpretationRecyclerAdapter;

public class InterpretationViewActivity extends AppCompatActivity {
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
        setContentView(R.layout.interpretation_row);


        Bundle extrasBundle = getIntent().getExtras();
        String term = extrasBundle.getString("TERM");
        String meaning = extrasBundle.getString("MEANING");


        ImageView mainImage = findViewById(R.id.term_imageView);
        TextView titleText = findViewById(R.id.term_title_text);
        TextView meaningText = findViewById(R.id.meaning_text);

        titleText.setText(term);
        meaningText.setText(meaning);


    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
