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

import java.util.Date;
import java.util.List;

import model.Journal;

public class JournalRecyclerAdapter extends RecyclerView.Adapter<JournalRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Journal> journalList;
    private OnJournalListener onJournalListener;

    public JournalRecyclerAdapter(Context context, List<Journal> journalList, OnJournalListener onJournalListener) {
        this.context = context;
        this.journalList = journalList;
        this.onJournalListener = onJournalListener;
    }

    @NonNull
    @Override
    public JournalRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.journal_row, viewGroup, false);


        return new ViewHolder(view, context, onJournalListener);
    }

    @Override
    public void onBindViewHolder(@NonNull JournalRecyclerAdapter.ViewHolder viewHolder, int position) {

        Journal journal = journalList.get(position);

        viewHolder.title.setText(journal.getTitle());
        viewHolder.entries.setText(journal.getEntry());
//
        String noDate = "Mystery date";
//        Timestamp timestamp = journal.getDate();
//        String entryDate = timestamp != null ? timestamp.toDate().toString() : noDate;
        viewHolder.dateAdded.setText(journal.getDate());
    }

    @Override
    public int getItemCount() {
        return journalList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView
                title,
                entries,
                dateAdded,
                name;
        public ImageButton shareButton;
        String userId;
        String userName;

        OnJournalListener onJournalListener;

        public ViewHolder(@NonNull View itemView, Context ctx, OnJournalListener onJournalListener) {
            super(itemView);
            context = ctx;
            this.onJournalListener = onJournalListener;

            title = itemView.findViewById(R.id.journal_title_list);
            entries = itemView.findViewById(R.id.journal_entry_list);
            dateAdded = itemView.findViewById(R.id.journal_timestamp_list);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onJournalListener.onJournalClick(getAdapterPosition());
        }
    }
    public interface OnJournalListener{
        void onJournalClick(int position);
    }
}
