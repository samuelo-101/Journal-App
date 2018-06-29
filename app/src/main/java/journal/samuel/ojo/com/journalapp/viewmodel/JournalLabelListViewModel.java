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
        initialize(journalDatabase, null);
    }

    public JournalLabelListViewModel(JournalDatabase journalDatabase, Integer excludeId) {
        initialize(journalDatabase, excludeId);
    }

    public LiveData<List<JournalLabel>> getJournals() {
        return journals;
    }

    private void initialize(JournalDatabase journalDatabase, Integer excludeId) {
        this.journalDatabase = journalDatabase;
        if(excludeId == null)
            this.journals = journalDatabase.getJournalLabelDao().findAll();
        else
            this.journals = journalDatabase.getJournalLabelDao().findWhereIdNotEqualTo(excludeId);
    }

}
