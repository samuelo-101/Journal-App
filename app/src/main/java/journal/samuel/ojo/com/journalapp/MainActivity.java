package journal.samuel.ojo.com.journalapp;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.List;

import journal.samuel.ojo.com.journalapp.adapter.RecyclerViewJournalAdapter;
import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.db.entity.Journal;
import journal.samuel.ojo.com.journalapp.factory.JournalServiceFactory;
import journal.samuel.ojo.com.journalapp.factory.JournalViewModelFactory;
import journal.samuel.ojo.com.journalapp.util.AppUtil;
import journal.samuel.ojo.com.journalapp.util.SharedPreferencesUtil;
import journal.samuel.ojo.com.journalapp.viewmodel.JournalListViewModel;

public class MainActivity extends AppCompatActivity implements RecyclerViewJournalAdapter.JournalItemClickListener {

    private LinearLayout llNoJournalsMessage;
    private RecyclerView rvJournalList;
    private RecyclerViewJournalAdapter adapter;

    private ProgressBar progressBar;

    private JournalDatabase journalDatabase;
    private JournalServiceFactory journalServiceFactory;
    private JournalListViewModel journalListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        checkUserSignIn();

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        progressBar = findViewById(R.id.progressBar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ManageJournalActivity.class);
                startActivity(intent);
            }
        });

        llNoJournalsMessage = findViewById(R.id.llNoJournalsMessage);
        rvJournalList = findViewById(R.id.rvJournalList);
        rvJournalList.setItemAnimator(new DefaultItemAnimator());

        String userId = SharedPreferencesUtil.getString(this, getString(R.string.g_id));

        initializeDatabase();

        adapter = new RecyclerViewJournalAdapter(this, this, journalDatabase);
        rvJournalList.setAdapter(adapter);

        JournalViewModelFactory journalViewModelFactory = new JournalViewModelFactory(this.journalDatabase, userId);
        journalListViewModel = ViewModelProviders.of(this, journalViewModelFactory).get(JournalListViewModel.class);
        journalListViewModel.getAllJournals().observe(this, new Observer<List<Journal>>() {
            @Override
            public void onChanged(@Nullable List<Journal> journals) {
                if(journals == null || journals.size() == 0) {
                    llNoJournalsMessage.setVisibility(View.VISIBLE);
                    rvJournalList.setVisibility(View.GONE);
                } else {
                    llNoJournalsMessage.setVisibility(View.GONE);
                    rvJournalList.setVisibility(View.VISIBLE);
                }
                adapter.setJournalItems(journals);
                progressBar.setVisibility(View.GONE);
            }
        });


        generateWelcomeMessage();
    }

    private void checkUserSignIn() {
        String signedInUserId = SharedPreferencesUtil.getString(this, getString(R.string.g_id));
        if(TextUtils.isEmpty(signedInUserId)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        journalListViewModel.getAllJournals().removeObservers(this);
    }

    private void initializeDatabase() {
        journalDatabase = JournalDatabase.getInstance(this);
        journalServiceFactory = new JournalServiceFactory(journalDatabase);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_labels) {
            Intent intent = new Intent(this, ManageLabelsActivity.class);
            startActivity(intent);
            return true;
        } else if(id == R.id.action_signout) {
            AppUtil.signOut(this);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClickListener(final int journalId) {
        Intent intent = new Intent(MainActivity.this, ViewJournalFullscreenActivity.class);
        intent.putExtra(ViewJournalFullscreenActivity.JOURNAL_ID_PARAM, journalId);
        startActivity(intent);
    }

    @Override
    public void onEditClickListener(final int journalId) {
        Intent intent = new Intent(MainActivity.this, ManageJournalActivity.class);
        intent.putExtra(ManageJournalActivity.JOURNAL_ID_PARAM, journalId);
        startActivity(intent);
    }

    @Override
    public void onDeleteClickListener(final int journalId) {
        Snackbar.make(rvJournalList, "Are you sure you want to delete this journal?", Snackbar.LENGTH_LONG)
                .setAction("Delete", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        journalServiceFactory.deleteById(journalId);
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.colorAccent))
                .show();
    }

    @Override
    public void onShareClickListener(String textToShare) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, textToShare);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void generateWelcomeMessage() {
        String email = SharedPreferencesUtil.getString(this, getString(R.string.g_email));
        String firstName = SharedPreferencesUtil.getString(this, getString(R.string.g_firstName));
        String lastName = SharedPreferencesUtil.getString(this, getString(R.string.g_lastName));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getString(R.string.signed_in_message));
        stringBuilder.append(" ");
        stringBuilder.append(firstName);
        stringBuilder.append(" ");
        stringBuilder.append(lastName);
        stringBuilder.append(" (");
        stringBuilder.append(email);
        stringBuilder.append(")");
        Snackbar.make(rvJournalList,
                stringBuilder.toString() ,
                Snackbar.LENGTH_LONG)
                .show();
    }
}
