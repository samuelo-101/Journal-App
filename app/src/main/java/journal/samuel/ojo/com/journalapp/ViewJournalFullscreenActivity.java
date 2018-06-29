package journal.samuel.ojo.com.journalapp;

import android.annotation.SuppressLint;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.entity.Journal;
import journal.samuel.ojo.com.journalapp.factory.JournalLabelServiceFactory;
import journal.samuel.ojo.com.journalapp.factory.JournalServiceFactory;
import journal.samuel.ojo.com.journalapp.util.AppUtil;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ViewJournalFullscreenActivity extends AppCompatActivity {

    private ImageButton btnClose;
    private TextView tvCreatedOn;
    private TextView tvTitle;
    private TextView tvJournalText;
    private TextView tvLabel;

    private JournalDatabase journalDatabase;
    private JournalServiceFactory journalServiceFactory;
    private JournalLabelServiceFactory journalLabelServiceFactory;

    public static final String JOURNAL_ID_PARAM = "journalId";
    private int journalId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_view_journal_fullscreen);

        tvCreatedOn = findViewById(R.id.tvCreatedOn);
        tvTitle = findViewById(R.id.tvTitle);
        tvLabel = findViewById(R.id.tvLabel);
        tvJournalText = findViewById(R.id.tvJournalText);
        btnClose = findViewById(R.id.btnClose);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey(JOURNAL_ID_PARAM)) {
            journalId = extras.getInt(JOURNAL_ID_PARAM);
        } else  {
            finish();
        }

        initializeDatabase();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Journal journal = journalServiceFactory.getById(journalId);
        Long createdOn = journal.getCreatedOn();
        tvCreatedOn.setText(AppUtil.getFormattedDate(createdOn));
        tvCreatedOn.append(" ");
        tvCreatedOn.append(AppUtil.getFormattedTime(createdOn));

        tvTitle.setText(journal.getTitle());
        tvJournalText.setText(journal.getJournalText());

        Integer journalLabelId = journal.getJournalLabelId();
        if(journalLabelId != null) {
            tvLabel.setVisibility(View.VISIBLE);
            tvLabel.setText(journalLabelServiceFactory.findById(journalLabelId).getLabel());
        } else {
            tvLabel.setVisibility(View.GONE);
        }
    }

    private void initializeDatabase() {
        journalDatabase = JournalDatabase.getInstance(this);
        journalServiceFactory = new JournalServiceFactory(journalDatabase);
        journalLabelServiceFactory = new JournalLabelServiceFactory(journalDatabase);
    }
}
