package journal.samuel.ojo.com.journalapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.Calendar;

import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.entity.Journal;
import journal.samuel.ojo.com.journalapp.factory.JournalServiceFactory;

public class ManageJournalActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button btnSave;
    private EditText edtTitle;
    private EditText edtJournalText;

    private JournalDatabase journalDatabase;
    private JournalServiceFactory journalServiceFactory;

    private int journalId;
    private Journal journal;

    public static String JOURNAL_ID_PARAM = "journalId";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_journal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final Bundle extras = getIntent().getExtras();
        final boolean isEditOperation = extras != null && extras.containsKey(JOURNAL_ID_PARAM);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        btnSave = findViewById(R.id.btnSave);
        edtTitle = findViewById(R.id.edtTitle);
        edtJournalText = findViewById(R.id.edtJournalText);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validateInputs()) {
                    setFormState(true);
                    journal.setTitle(edtTitle.getText().toString());
                    journal.setJournalText(edtJournalText.getText().toString());

                    if (!isEditOperation) {
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

        initializeDatabase();

        if(isEditOperation) {
            this.journal = journalServiceFactory.getById(extras.getInt(JOURNAL_ID_PARAM));
            edtTitle.setText(journal.getTitle());
            edtJournalText.setText(journal.getJournalText());
        } else {
            this.journal = new Journal();
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

        return true;
    }

    private void initializeDatabase() {
        journalDatabase = JournalDatabase.getInstance(ManageJournalActivity.this);
        journalServiceFactory = new JournalServiceFactory(journalDatabase);
    }

    private void setFormState(boolean isEnabled) {
        edtTitle.setEnabled(isEnabled);
        edtJournalText.setEnabled(isEnabled);
        btnSave.setEnabled(isEnabled);
        progressBar.setVisibility(isEnabled ? View.GONE : View.VISIBLE);
    }

}
