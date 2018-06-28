package journal.samuel.ojo.com.journalapp.factory;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import journal.samuel.ojo.com.journalapp.db.JournalDatabase;
import journal.samuel.ojo.com.journalapp.viewmodel.JournalLabelListViewModel;
import journal.samuel.ojo.com.journalapp.viewmodel.JournalListViewModel;

public class JournalLabelViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private JournalDatabase journalDatabase;

    public JournalLabelViewModelFactory(JournalDatabase journalDatabase) {
        this.journalDatabase = journalDatabase;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new JournalLabelListViewModel(this.journalDatabase);
    }
}
