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
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import journal.samuel.ojo.com.journalapp.adapter.RecyclerViewJournalAdapter;
import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.entity.Journal;
import journal.samuel.ojo.com.journalapp.factory.JournalServiceFactory;
import journal.samuel.ojo.com.journalapp.factory.JournalViewModelFactory;
import journal.samuel.ojo.com.journalapp.model.JournalItem;
import journal.samuel.ojo.com.journalapp.viewmodel.JournalListViewModel;

public class MainActivity extends AppCompatActivity implements RecyclerViewJournalAdapter.JournalItemClickListener {

    private LinearLayout llNoJournalsMessage;
    private RecyclerView rvJournalList;
    private RecyclerViewJournalAdapter adapter;

    private ProgressBar progressBar;

    private JournalDatabase journalDatabase;

    private JournalServiceFactory journalServiceFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = findViewById(R.id.progressBar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(MainActivity.this, ManageJournalActivity.class);
                startActivity(intent);
            }
        });

        llNoJournalsMessage = findViewById(R.id.llNoJournalsMessage);
        rvJournalList = findViewById(R.id.rvJournalList);
        rvJournalList.setItemAnimator(new DefaultItemAnimator());

        initializeDatabase();

        adapter = new RecyclerViewJournalAdapter(this, this);
        rvJournalList.setAdapter(adapter);

        JournalViewModelFactory journalViewModelFactory = new JournalViewModelFactory(this.journalDatabase);
        final JournalListViewModel journalListViewModel = ViewModelProviders.of(this, journalViewModelFactory).get(JournalListViewModel.class);
        journalListViewModel.getAllJournals().observe(this, new Observer<List<Journal>>() {
            @Override
            public void onChanged(@Nullable List<Journal> journals) {
                if(journals == null || journals.size() == 0) {
                    llNoJournalsMessage.setVisibility(View.VISIBLE);
                } else {
                    llNoJournalsMessage.setVisibility(View.GONE);
                }
                adapter.setJournalItems(journals);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initUI() {
        progressBar.setVisibility(View.VISIBLE);
        llNoJournalsMessage.setVisibility(View.GONE);
        //rvJournalList.setVisibility(View.GONE);
    }

    private void initializeDatabase() {
        journalDatabase = JournalDatabase.getInstance(this);
        journalServiceFactory = new JournalServiceFactory(journalDatabase);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClickListener(final int journalId) {
        Intent intent = new Intent(MainActivity.this, ViewJournalFullscreenActivity.class);
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
}
