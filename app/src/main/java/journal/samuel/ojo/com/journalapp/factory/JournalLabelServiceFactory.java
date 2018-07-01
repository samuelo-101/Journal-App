package journal.samuel.ojo.com.journalapp.factory;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.db.entity.JournalLabel;

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

    public JournalLabel findById(Integer journalLabelId) {
        try {
            return new JournalLabelFindByIdAsyncTask(journalDatabase, journalLabelId).execute().get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public List<JournalLabel> findForUserWhereIdNotEqualTo(String userId, Integer journalLabelId) {
        try {
            return new JournalLabelFindWhereIdNotEqualToAsyncTask(journalDatabase, userId, journalLabelId).execute().get();
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

    private class JournalLabelFindByIdAsyncTask extends AsyncTask<Void, Void, JournalLabel> {

        private JournalDatabase journalDatabase;
        private Integer id;

        JournalLabelFindByIdAsyncTask(JournalDatabase journalDatabase, Integer id) {
            this.journalDatabase = journalDatabase;
            this.id = id;
        }

        @Override
        protected JournalLabel doInBackground(Void... voids) {
            return this.journalDatabase.getJournalLabelDao().findById(id);
        }

    }

    private class JournalLabelFindWhereIdNotEqualToAsyncTask extends AsyncTask<Void, Void, List<JournalLabel>> {

        private JournalDatabase journalDatabase;
        private String userId;
        private Integer excludingId;

        JournalLabelFindWhereIdNotEqualToAsyncTask(JournalDatabase journalDatabase, String userId, Integer excludingId) {
            this.journalDatabase = journalDatabase;
            this.userId = userId;
            this.excludingId = excludingId;
        }

        @Override
        protected List<JournalLabel> doInBackground(Void... voids) {
            List<JournalLabel> journalLabels = this.journalDatabase.getJournalLabelDao().findForUserWhereIdNotNull(this.userId);
            if(excludingId == null) {
                return journalLabels;
            } else {

                List<JournalLabel> newJournalLabels = new ArrayList<>();
                for (JournalLabel label : journalLabels) {
                    if(!label.getId().equals(excludingId))
                        newJournalLabels.add(label);
                }

                Collections.sort(newJournalLabels, new Comparator<JournalLabel>() {
                    @Override
                    public int compare(JournalLabel journalLabelOne, JournalLabel journalLabelTwo) {
                        return journalLabelOne.getLabel().compareTo(journalLabelTwo.getLabel());
                    }
                });
                return newJournalLabels;
            }
        }
    }
}
