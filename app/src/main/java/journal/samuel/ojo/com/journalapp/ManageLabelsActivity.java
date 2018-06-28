package journal.samuel.ojo.com.journalapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.List;

import journal.samuel.ojo.com.journalapp.adapter.RecyclerViewJournalLabelAdapter;
import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.entity.JournalLabel;
import journal.samuel.ojo.com.journalapp.factory.JournalLabelServiceFactory;
import journal.samuel.ojo.com.journalapp.factory.JournalLabelViewModelFactory;
import journal.samuel.ojo.com.journalapp.viewmodel.JournalLabelListViewModel;

public class ManageLabelsActivity extends AppCompatActivity implements RecyclerViewJournalLabelAdapter.OnJournalLabelItemClick {

    private EditText edtLabelName;
    private ImageButton btnAddLabel;
    private RecyclerView rvJournalLabels;
    private RecyclerViewJournalLabelAdapter adapter;

    private JournalDatabase journalDatabase;
    private JournalLabelViewModelFactory journalLabelViewModelFactory;
    private JournalLabelServiceFactory journalLabelServiceFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_labels);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtLabelName = findViewById(R.id.edtLabelName);
        btnAddLabel = findViewById(R.id.btnAddLabel);
        rvJournalLabels = findViewById(R.id.rvJournalLabels);
        rvJournalLabels.setItemAnimator(new DefaultItemAnimator());

        btnAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!TextUtils.isEmpty(edtLabelName.getText())) {
                    JournalLabel journalLabel = new JournalLabel();
                    journalLabel.setLabel(edtLabelName.getText().toString());
                    journalLabelServiceFactory.save(journalLabel);
                    edtLabelName.setText("");
                } else {
                    edtLabelName.setError(getString(R.string.label_name_required));
                }
            }
        });

        adapter = new RecyclerViewJournalLabelAdapter(this);
        rvJournalLabels.setAdapter(adapter);

        journalDatabase = JournalDatabase.getInstance(this);
        journalLabelViewModelFactory = new JournalLabelViewModelFactory(journalDatabase);
        journalLabelServiceFactory = new JournalLabelServiceFactory(journalDatabase);

        JournalLabelListViewModel journalLabelListViewModel = ViewModelProviders.of(this, journalLabelViewModelFactory).get(JournalLabelListViewModel.class);
        journalLabelListViewModel.getJournals().observe(this, new Observer<List<JournalLabel>>() {
            @Override
            public void onChanged(@Nullable List<JournalLabel> journalLabels) {
                adapter.setJournalLabels(journalLabels);
            }
        });
    }

    @Override
    public void onDeleteClick(int journalLabelId) {
        journalLabelServiceFactory.delete(journalLabelServiceFactory.findById(journalLabelId));
    }
}
