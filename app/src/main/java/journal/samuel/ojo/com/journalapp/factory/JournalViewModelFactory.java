package journal.samuel.ojo.com.journalapp.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.viewmodel.JournalListViewModel;

public class JournalViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private JournalDatabase journalDatabase;
    private String userId;

    public JournalViewModelFactory(JournalDatabase journalDatabase, String userId) {
        this.journalDatabase = journalDatabase;
        this.userId = userId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new JournalListViewModel(this.journalDatabase, this.userId);
    }
}
