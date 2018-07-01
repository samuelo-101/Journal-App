package journal.samuel.ojo.com.journalapp.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import journal.samuel.ojo.com.journalapp.R;
import journal.samuel.ojo.com.journalapp.db.entity.JournalLabel;

public class RecyclerViewJournalLabelAdapter extends RecyclerView.Adapter<RecyclerViewJournalLabelAdapter.ViewHolder> {

    private Context context;
    private List<JournalLabel> journalLabels;
    private OnJournalLabelItemClick onJournalLabelItemClick;

    public RecyclerViewJournalLabelAdapter(OnJournalLabelItemClick onJournalLabelItemClick) {
        this.onJournalLabelItemClick = onJournalLabelItemClick;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.journal_label_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final JournalLabel journalLabel = this.journalLabels.get(position);
        holder.tvJournalLabel.setText(journalLabel.getLabel());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onJournalLabelItemClick.onDeleteClick(journalLabel.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.journalLabels == null ? 0 : this.journalLabels.size();
    }

    public List<JournalLabel> getJournalLabels() {
        return journalLabels;
    }

    public void setJournalLabels(List<JournalLabel> journalLabels) {
        this.journalLabels = journalLabels;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView tvJournalLabel;
        ImageButton btnDelete;

        ViewHolder(View itemView) {
            super(itemView);

            view = itemView;
            tvJournalLabel = itemView.findViewById(R.id.tvJournalLabel);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    public interface OnJournalLabelItemClick {
        public void onDeleteClick(int journalLabelId);
    }

}
