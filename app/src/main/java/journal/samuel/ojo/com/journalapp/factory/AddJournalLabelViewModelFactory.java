package journal.samuel.ojo.com.journalapp.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.viewmodel.JournalLabelListViewModel;

public class AddJournalLabelViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private Integer existingJournalLabelId;
    private String userId;
    private JournalDatabase journalDatabase;

    public AddJournalLabelViewModelFactory(JournalDatabase journalDatabase, String userId, Integer existingJournalLabelId) {
        this.existingJournalLabelId = existingJournalLabelId;
        this.userId = userId;
        this.journalDatabase = journalDatabase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new JournalLabelListViewModel(this.journalDatabase, this.userId, this.existingJournalLabelId);
    }
}
