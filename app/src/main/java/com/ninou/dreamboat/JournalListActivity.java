package com.ninou.dreamboat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import model.Journal;
import ui.JournalRecyclerAdapter;

import static ui.JournalRecyclerAdapter.OnJournalListener;

public class JournalListActivity extends AppCompatActivity implements OnJournalListener {
    private static final String TAG = "JournalListActivity";
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<Journal> journalList;
    private RecyclerView recyclerView;
    private JournalRecyclerAdapter journalRecyclerAdapter;

    private CollectionReference collectionReference = db.collection("Journal");
    private TextView noJournalEntry;
    private Button noEntriesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_journal_list);

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
                            Intent a = new Intent(JournalListActivity.this,
                                    PostJournalActivity.class);
                            startActivity(a);
                            finish();
                        break;

                    case R.id.action_my_dreamboat:
                            Intent c = new Intent(JournalListActivity.this,
                                    JournalListActivity.class);
                            startActivity(c);
                            finish();
                            break;
                    case R.id.action_search:
                        Intent d = new Intent(JournalListActivity.this, SearchActivity.class);
                        startActivity(d);
                        finish();
                        break;
                }
                return false;
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        noJournalEntry = findViewById(R.id.list_no_entries);
        noEntriesButton = findViewById(R.id.list_no_entries_button);

        noEntriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JournalListActivity.this,
                        PostJournalActivity.class));
//                finish();
            }
        });

        journalList = new ArrayList<>();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    @Override
    protected void onStart() {
        super.onStart(); //get journals from firestore

        collectionReference.whereEqualTo("userId", FirebaseAuth.getInstance()
                .getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            journalList.clear();
                            for (QueryDocumentSnapshot journals : queryDocumentSnapshots) {
                                Journal journal = journals.toObject(Journal.class);
                                journalList.add(journal);
                            }

                            //Invoke recyclerview
                            journalRecyclerAdapter = new JournalRecyclerAdapter(JournalListActivity.this,
                                    journalList, JournalListActivity.this);
                            recyclerView.setAdapter(journalRecyclerAdapter);
                            journalRecyclerAdapter.notifyDataSetChanged();

                        }
                        else {
                            noJournalEntry.setVisibility(View.VISIBLE);
                            noEntriesButton.setVisibility(View.VISIBLE);

                            noEntriesButton.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(new Intent(JournalListActivity.this, PostJournalActivity.class));
                                    finish();
                                }
                            });

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                    startActivity(new Intent(JournalListActivity.this,
                            PostJournalActivity.class));
                    finish();

                break;
            case R.id.action_signout:
                //sign user out
                    firebaseAuth.signOut();

                    startActivity(new Intent(JournalListActivity.this,
                            MainActivity.class));
                    finish();

                break;
            case R.id.action_my_dreamboat:
                    startActivity(new Intent(JournalListActivity.this,
                            JournalListActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onJournalClick(int position) {
        Intent intent = new Intent(JournalListActivity.this, ViewEntryActivity.class);
        intent.putExtra("CURRENT_USER", currentUser.getUid());
        intent.putExtra("TITLE", journalList.get(position).getTitle());
        intent.putExtra("ENTRY_TEXT", journalList.get(position).getEntry());
        intent.putExtra("DATE", journalList.get(position).getDate());
        startActivity(intent);
    }
}
