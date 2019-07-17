package ui;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.ninou.dreamboat.R;

import java.util.List;

import model.Journal;

public class JournalRecyclerAdapter extends RecyclerView.Adapter<JournalRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Journal> journalList;

    public JournalRecyclerAdapter(Context context, List<Journal> journalList) {
        this.context = context;
        this.journalList = journalList;
    }

    @NonNull
    @Override
    public JournalRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.journal_row, viewGroup, false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalRecyclerAdapter.ViewHolder viewHolder, int position) {

        Journal journal = journalList.get(position);

        viewHolder.title.setText(journal.getTitle());
        viewHolder.entries.setText(journal.getEntry());
//        viewHolder.name.setText(journal.getUserName());
//        String timeAgo = (String) DateUtils.getRelativeTimeSpanString(journal
//                .getTimeAdded()
//                .getSeconds() * 1000);
//        viewHolder.dateAdded.setText(timeAgo);
//        Timestamp timestamp = journal.getDate();
//        String entryDate = timestamp.toDate().toString();
        viewHolder.date.setText(journal.getDate());
    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView
                title,
                entries,
                date,
                name;
        public ImageButton shareButton;
        String userId;
        String userName;


        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            title = itemView.findViewById(R.id.journal_title_list);
            entries = itemView.findViewById(R.id.journal_entry_list);
            date = itemView.findViewById(R.id.journal_timestamp_list);
//            name = itemView.findViewById(R.id.journal_row_username);

//            shareButton = itemView.findViewById(R.id.journal_row_share_button);
//            shareButton.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View view) {
////                    context.startActivity();
//                }
//            });
        }
    }
}
