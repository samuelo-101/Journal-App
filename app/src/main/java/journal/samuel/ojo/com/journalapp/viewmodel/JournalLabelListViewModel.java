package journal.samuel.ojo.com.journalapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.entity.Journal;
import journal.samuel.ojo.com.journalapp.entity.JournalLabel;

public class JournalLabelListViewModel extends ViewModel {

    private JournalDatabase journalDatabase;

    private String userId;
    private LiveData<List<JournalLabel>> journals;

    public JournalLabelListViewModel(JournalDatabase journalDatabase, String userId) {
        this.journalDatabase = journalDatabase;
        this.userId = userId;
        initialize( null);
    }

    public JournalLabelListViewModel(JournalDatabase journalDatabase, String userId, Integer excludeId) {
        this.journalDatabase = journalDatabase;
        this.userId = userId;
        initialize(excludeId);
    }

    public LiveData<List<JournalLabel>> getJournals() {
        return journals;
    }

    private void initialize(Integer excludeId) {
        if(excludeId == null)
            this.journals = journalDatabase.getJournalLabelDao().findAllForUser(this.userId);
        else
            this.journals = journalDatabase.getJournalLabelDao().findForUserWhereIdNotEqualTo(userId, excludeId);
    }

}
