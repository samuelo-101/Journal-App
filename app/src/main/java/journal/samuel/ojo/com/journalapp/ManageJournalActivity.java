package journal.samuel.ojo.com.journalapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import journal.samuel.ojo.com.journalapp.adapter.RecyclerViewAddLabelToJournalAdapter;
import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.db.entity.Journal;
import journal.samuel.ojo.com.journalapp.db.entity.JournalLabel;
import journal.samuel.ojo.com.journalapp.factory.JournalLabelServiceFactory;
import journal.samuel.ojo.com.journalapp.factory.JournalLabelViewModelFactory;
import journal.samuel.ojo.com.journalapp.factory.JournalServiceFactory;
import journal.samuel.ojo.com.journalapp.util.SharedPreferencesUtil;
import journal.samuel.ojo.com.journalapp.viewmodel.JournalLabelListViewModel;

public class ManageJournalActivity extends AppCompatActivity implements RecyclerViewAddLabelToJournalAdapter.OnJournalLabelItemClick {

    private ProgressBar progressBar;
    private Button btnSave;
    private EditText edtTitle;
    private EditText edtJournalText;
    private RecyclerView rvAddJournalLabels;
    private TextView tvLabel;
    private TextView tvAddLabel;
    private View bottomSheetBehaviorView;
    private BottomSheetBehavior bottomSheetBehavior;

    private JournalDatabase journalDatabase;
    private JournalServiceFactory journalServiceFactory;
    private JournalLabelServiceFactory journalLabelServiceFactory;
    private JournalLabelViewModelFactory journalLabelViewModelFactory;
    private JournalLabelListViewModel journalLabelListViewModel;

    private RecyclerViewAddLabelToJournalAdapter adapter;

    private String userId;
    private Journal journal;
    private JournalLabel journalLabel;

    private boolean isEditOperation;

    public static String JOURNAL_ID_PARAM = "journalId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_journal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Bundle extras = getIntent().getExtras();
        isEditOperation = extras != null && extras.containsKey(JOURNAL_ID_PARAM);
        userId = SharedPreferencesUtil.getString(this, getString(R.string.g_id));

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        btnSave = findViewById(R.id.btnSave);
        edtTitle = findViewById(R.id.edtTitle);
        edtJournalText = findViewById(R.id.edtJournalText);
        tvLabel = findViewById(R.id.tvLabel);
        tvAddLabel = findViewById(R.id.tvAddLabel);
        rvAddJournalLabels = findViewById(R.id.rvAddJournalLabels);
        bottomSheetBehaviorView = findViewById(R.id.design_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetBehaviorView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvAddJournalLabels.setLayoutManager(layoutManager);
        rvAddJournalLabels.setItemAnimator(new DefaultItemAnimator());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvAddJournalLabels.getContext(),
                layoutManager.getOrientation());
        rvAddJournalLabels.addItemDecoration(dividerItemDecoration);

        initializeDatabase();

        if(isEditOperation) {
            this.journal = journalServiceFactory.getById(extras.getInt(JOURNAL_ID_PARAM));
            updateUIForEditOperation();
        } else {
            this.journal = new Journal();
            tvLabel.setVisibility(View.GONE);
        }

        adapter = new RecyclerViewAddLabelToJournalAdapter(this, isEditOperation ? journal.getJournalLabelId() : null);
        rvAddJournalLabels.setAdapter(adapter);

        setupViewModelAndFactory();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateInputs()) {
                    setFormState(true);
                    journal.setTitle(edtTitle.getText().toString());
                    journal.setJournalText(edtJournalText.getText().toString());

                    if (!isEditOperation) {
                        journal.setUserId(userId);
                        journal.setCreatedOn(Calendar.getInstance().getTimeInMillis());
                        journalServiceFactory.save(journal);
                    } else {
                        journal.setUpdatedOn(Calendar.getInstance().getTimeInMillis());
                        journalServiceFactory.update(journal);
                    }

                    finish();
                }
            }
        });

        tvAddLabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if(!isEditOperation)
            journalLabelListViewModel.getJournalLabels().removeObservers(this);
    }

    private void updateUIForEditOperation() {
        edtTitle.setText(journal.getTitle());
        edtJournalText.setText(journal.getJournalText());
        journalLabel = journalLabelServiceFactory.findById(journal.getJournalLabelId());
        if(journalLabel != null) {
            tvLabel.setVisibility(View.VISIBLE);
            tvLabel.setText(journalLabel.getLabel());
        } else {
            tvLabel.setVisibility(View.GONE);
        }
    }

    private boolean validateInputs() {
        boolean isValid = true;
        if(TextUtils.isEmpty(edtTitle.getText())) {
            edtTitle.setError(getString(R.string.journal_title_required));
            isValid = false;
        }
        if(TextUtils.isEmpty(edtJournalText.getText())) {
            edtJournalText.setError(getString(R.string.journal_content_required));
            isValid = false;
        }

        return isValid;
    }

    private void initializeDatabase() {
        journalDatabase = JournalDatabase.getInstance(ManageJournalActivity.this);
        journalServiceFactory = new JournalServiceFactory(journalDatabase);
        journalLabelServiceFactory = new JournalLabelServiceFactory(journalDatabase);
    }

    private void setupViewModelAndFactory() {
        if(isEditOperation) {
            List<JournalLabel> journalLabels = journalLabelServiceFactory.findForUserWhereIdNotEqualTo(this.userId, journal.getJournalLabelId());
            adapter.setJournalLabels(journalLabels);
        } else {
            journalLabelViewModelFactory = new JournalLabelViewModelFactory(journalDatabase, userId);
            journalLabelListViewModel = ViewModelProviders.of(this, journalLabelViewModelFactory).get(JournalLabelListViewModel.class);
            journalLabelListViewModel.getJournalLabels().observe(this, new Observer<List<JournalLabel>>() {
                @Override
                public void onChanged(@Nullable List<JournalLabel> journalLabels) {
                    adapter.setJournalLabels(journalLabels);
                }
            });
        }
    }

    private void setFormState(boolean isEnabled) {
        edtTitle.setEnabled(isEnabled);
        edtJournalText.setEnabled(isEnabled);
        btnSave.setEnabled(isEnabled);
        progressBar.setVisibility(isEnabled ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onJournalLabelItemClick(int journalLabelId) {
        Integer currentJournalLabelId = journal.getJournalLabelId();

        journal.setJournalLabelId(journalLabelId);
        journalLabel = journalLabelServiceFactory.findById(journalLabelId);
        tvLabel.setVisibility(View.VISIBLE);
        tvLabel.setText(journalLabel.getLabel());

        List<JournalLabel> journalLabels = adapter.getJournalLabels();
        List<JournalLabel> newJournalLabels = new ArrayList<>();

        if(currentJournalLabelId != null)
            newJournalLabels.add(journalLabelServiceFactory.findById(currentJournalLabelId));

        for (JournalLabel label : journalLabels) {
            if(!label.getId().equals(journalLabelId))
                newJournalLabels.add(label);
        }

        Collections.sort(newJournalLabels, new Comparator<JournalLabel>() {
            @Override
            public int compare(JournalLabel journalLabelOne, JournalLabel journalLabelTwo) {
                return journalLabelOne.getLabel().compareTo(journalLabelTwo.getLabel());
            }
        });

        adapter.setJournalLabels(newJournalLabels);

        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }
}
