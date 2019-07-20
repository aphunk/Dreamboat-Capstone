package com.ninou.dreamboat;

import androidx.appcompat.app.AppCompatActivity;

import android.app.job.JobInfo;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import model.Journal;
import util.JournalApi;

public class ViewEntryActivity extends AppCompatActivity {
    private String title;
    private String entryBody;
    private String date;

    private TextView entryTitle;
    private TextView entryDate;
    private TextView entryBodyText;

    private Button editButton;

    private Intent intentExtras;
    private Journal journalEntry;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_entry);

        editButton = findViewById(R.id.edit_button);
        entryTitle = findViewById(R.id.entry_title);
        entryDate = findViewById(R.id.entry_date);
        entryBodyText = findViewById(R.id.entry_body_text);


        Bundle extrasBundle = getIntent().getExtras();
        if (extrasBundle != null) {
            title = extrasBundle.getString("TITLE");
            entryBody = extrasBundle.getString("ENTRY_TEXT");
            date = extrasBundle.getString("DATE");
        }


        entryTitle.setText(title);
        entryBodyText.setText(entryBody);
        entryDate.setText(date);

//        editButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                JournalApi journalApi = JournalApi.getInstance(); //Global API
//                journalApi.setUserId(currentUserId);
//                Intent intent = new Intent(ViewEntryActivity.this,
//                        PostJournalActivity.class);
//                intent.putExtra("userId", currentUserId);
//                intent.putExtra()
//                startActivity(intent);
//            }
//        });
    }

}
