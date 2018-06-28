package journal.samuel.ojo.com.journalapp.repo;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;
import java.util.concurrent.ExecutionException;

import journal.samuel.ojo.com.journalapp.dao.JournalDao;
import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.entity.Journal;

public class JournalRepository {

    private Context context;
    private JournalDao journalDao;
    private LiveData<List<Journal>> journalsLiveData;

    public JournalRepository(Context context) {
        JournalDatabase journalDatabase = JournalDatabase.getInstance(context);
        journalDao = journalDatabase.getJournalDao();
        journalsLiveData = journalDao.findAll();

    }

    public LiveData<List<Journal>> getJournals() {
        try {
            return new FetchAllJournalsAsyncTask(journalDao).execute().get();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private static class FetchAllJournalsAsyncTask  extends AsyncTask<Void, Void, LiveData<List<Journal>>> {

        private JournalDao journalDao;

        FetchAllJournalsAsyncTask(JournalDao journalDao) {
            this.journalDao = journalDao;
        }

        @Override
        protected LiveData<List<Journal>> doInBackground(Void... voids) {
            return this.journalDao.findAll();
        }
    }
}
