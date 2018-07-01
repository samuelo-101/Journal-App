package journal.samuel.ojo.com.journalapp.factory;

import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.db.entity.Journal;

public class JournalServiceFactory {

    private JournalDatabase journalDatabase;

    public JournalServiceFactory(JournalDatabase journalDatabase) {
        this.journalDatabase = journalDatabase;
    }

    public void save(Journal journal) {
        new JournalSaveAsyncTask(journalDatabase).execute(journal);
    }

    public void update(Journal journal) {
        new JournalUpdateAsyncTask(journalDatabase).execute(journal);
    }

    public void deleteById(int journalId) {
        new JournalDeleteByIdAsyncTask(journalDatabase).execute(journalId);
    }

    public void delete(Journal journal) {
        new JournalDeleteAsyncTask(journalDatabase).execute(journal);
    }

    public Journal getById(int journalId) {
        try {
            return new JournalFindByIdAsyncTask(journalDatabase).execute(journalId).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private class JournalSaveAsyncTask extends AsyncTask<Journal, Void, Void> {

        private JournalDatabase journalDatabase;

        JournalSaveAsyncTask(JournalDatabase journalDatabase) {
            this.journalDatabase = journalDatabase;
        }

        @Override
        protected Void doInBackground(Journal... journals) {
            this.journalDatabase.getJournalDao().save(journals[0]);
            return null;
        }
    }

    private class JournalUpdateAsyncTask extends AsyncTask<Journal, Void, Void> {

        private JournalDatabase journalDatabase;

        JournalUpdateAsyncTask(JournalDatabase journalDatabase) {
            this.journalDatabase = journalDatabase;
        }

        @Override
        protected Void doInBackground(Journal... journals) {
            this.journalDatabase.getJournalDao().update(journals[0]);
            return null;
        }
    }

    private class JournalDeleteAsyncTask extends AsyncTask<Journal, Void, Void> {

        private JournalDatabase journalDatabase;

        JournalDeleteAsyncTask(JournalDatabase journalDatabase) {
            this.journalDatabase = journalDatabase;
        }

        @Override
        protected Void doInBackground(Journal... journals) {
            this.journalDatabase.getJournalDao().delete(journals[0]);
            return null;
        }
    }

    private class JournalDeleteByIdAsyncTask extends AsyncTask<Integer, Void, Void> {

        private JournalDatabase journalDatabase;

        JournalDeleteByIdAsyncTask(JournalDatabase journalDatabase) {
            this.journalDatabase = journalDatabase;
        }

        @Override
        protected Void doInBackground(Integer... ids) {
            this.journalDatabase.getJournalDao().delete(ids[0]);
            return null;
        }
    }

    private class JournalFindByIdAsyncTask extends AsyncTask<Integer, Void, Journal> {

        private JournalDatabase journalDatabase;

        JournalFindByIdAsyncTask(JournalDatabase journalDatabase) {
            this.journalDatabase = journalDatabase;
        }

        @Override
        protected Journal doInBackground(Integer... integers) {
            return this.journalDatabase.getJournalDao().findById(integers[0]);
        }
    }
}
