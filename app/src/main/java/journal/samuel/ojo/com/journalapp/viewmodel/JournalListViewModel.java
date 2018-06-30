package journal.samuel.ojo.com.journalapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.entity.Journal;

public class JournalListViewModel extends ViewModel {

    private JournalDatabase journalDatabase;
    private String userId;
    private LiveData<List<Journal>> journals;

    public JournalListViewModel(JournalDatabase journalDatabase, String userId) {
        this.journalDatabase = journalDatabase;
        this.userId = userId;
        initialize();
    }

    public LiveData<List<Journal>> getAllJournals() {
        return this.journals;
    }

    private void initialize() {
        this.journals = journalDatabase.getJournalDao().findAllForUser(this.userId);
    }

}
