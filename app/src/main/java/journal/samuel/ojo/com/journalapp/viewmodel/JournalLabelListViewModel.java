package journal.samuel.ojo.com.journalapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.entity.Journal;
import journal.samuel.ojo.com.journalapp.entity.JournalLabel;

public class JournalLabelListViewModel extends ViewModel {

    private JournalDatabase journalDatabase;

    private LiveData<List<JournalLabel>> journals;

    public JournalLabelListViewModel(JournalDatabase journalDatabase) {
        initialize(journalDatabase);
    }

    public LiveData<List<JournalLabel>> getJournals() {
        return journals;
    }

    private void initialize(JournalDatabase journalDatabase) {
        this.journalDatabase = journalDatabase;
        this.journals = journalDatabase.getJournalLabelDao().findAll();
    }

}
