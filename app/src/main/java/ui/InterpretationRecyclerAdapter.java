package ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ninou.dreamboat.R;

import java.util.List;

import model.Meaning;

public class InterpretationRecyclerAdapter extends RecyclerView.Adapter<InterpretationRecyclerAdapter.ViewHolder> {
    private Context context;
    private List<Meaning> meaningList;

    public InterpretationRecyclerAdapter(Context context, List<Meaning> meaningList) {
        this.context = context;
        this.meaningList = meaningList;
    }

    @NonNull
    @Override
    public InterpretationRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.interpretation_row, viewGroup, false);

       return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull InterpretationRecyclerAdapter.ViewHolder viewHolder, int position) {

        Meaning meaning = meaningList.get(position);

        viewHolder.word.setText(meaning.getTerm());
        viewHolder.meaning.setText(meaning.getInterpretation());
    }

    @Override
    public int getItemCount() {
        return meaningList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView
            word,
            meaning;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            word = itemView.findViewById(R.id.search_textView);
            meaning = itemView.findViewById(R.id.entry_body_textView);

        }
    }
}
