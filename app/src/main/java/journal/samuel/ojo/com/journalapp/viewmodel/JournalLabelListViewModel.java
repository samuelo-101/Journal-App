package journal.samuel.ojo.com.journalapp.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import java.util.List;

import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.db.entity.JournalLabel;

public class JournalLabelListViewModel extends ViewModel {

    private JournalDatabase journalDatabase;
    private String userId;
    private LiveData<List<JournalLabel>> journalLabels;

    public JournalLabelListViewModel(JournalDatabase journalDatabase, String userId) {
        this.journalDatabase = journalDatabase;
        this.userId = userId;
        initialize();
    }

    public LiveData<List<JournalLabel>> getJournalLabels() {
        return journalLabels;
    }

    private void initialize() {
        this.journalLabels = journalDatabase.getJournalLabelDao().findAllForUser(this.userId);
    }

}
