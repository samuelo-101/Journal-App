package journal.samuel.ojo.com.journalapp.factory;

import android.os.AsyncTask;

import java.util.concurrent.ExecutionException;

import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.entity.Journal;
import journal.samuel.ojo.com.journalapp.entity.JournalLabel;

public class JournalLabelServiceFactory {

    private JournalDatabase journalDatabase;

    public JournalLabelServiceFactory(JournalDatabase journalDatabase) {
        this.journalDatabase = journalDatabase;
    }

    public void save(JournalLabel journalLabel) {
        new JournalLabelSaveAsyncTask(journalDatabase).execute(journalLabel);
    }

    public void delete(JournalLabel journalLabel) {
        new JournalLabelDeleteAsyncTask(journalDatabase).execute(journalLabel);
    }

    public JournalLabel findById(int journalLabelId) {
        try {
            return new JournalLabelFindByIdAsyncTask(journalDatabase).execute(journalLabelId).get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private class JournalLabelSaveAsyncTask extends AsyncTask<JournalLabel, Void, Void> {

        private JournalDatabase journalDatabase;

        JournalLabelSaveAsyncTask(JournalDatabase journalDatabase) {
            this.journalDatabase = journalDatabase;
        }

        @Override
        protected Void doInBackground(JournalLabel... journalLabels) {
            this.journalDatabase.getJournalLabelDao().save(journalLabels[0]);
            return null;
        }
    }

    private class JournalLabelDeleteAsyncTask extends AsyncTask<JournalLabel, Void, Void> {

        private JournalDatabase journalDatabase;

        JournalLabelDeleteAsyncTask(JournalDatabase journalDatabase) {
            this.journalDatabase = journalDatabase;
        }

        @Override
        protected Void doInBackground(JournalLabel... journalLabels) {
            this.journalDatabase.getJournalLabelDao().delete(journalLabels[0]);
            return null;
        }
    }

    private class JournalLabelFindByIdAsyncTask extends AsyncTask<Integer, Void, JournalLabel> {

        private JournalDatabase journalDatabase;

        JournalLabelFindByIdAsyncTask(JournalDatabase journalDatabase) {
            this.journalDatabase = journalDatabase;
        }

        @Override
        protected JournalLabel doInBackground(Integer... integers) {
            return this.journalDatabase.getJournalLabelDao().findById(integers[0]);
        }
    }
}
