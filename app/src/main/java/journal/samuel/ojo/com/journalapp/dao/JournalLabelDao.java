package journal.samuel.ojo.com.journalapp.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import journal.samuel.ojo.com.journalapp.db.entity.JournalLabel;

@Dao
public interface JournalLabelDao {

    @Query("SELECT * FROM journal_label WHERE user_id = :userId ORDER BY label ASC")
    public LiveData<List<JournalLabel>> findAllForUser(String userId);

    @Query("SELECT * FROM journal_label WHERE id = :id")
    public JournalLabel findById(Integer id);

    @Query("SELECT * FROM journal_label WHERE user_id = :userId AND id <> :id")
    public LiveData<List<JournalLabel>> findForUserWhereIdNotEqualTo(String userId, Integer id);

    @Query("SELECT * FROM journal_label WHERE user_id = :userId AND id IS NOT NULL")
    public List<JournalLabel> findForUserWhereIdNotNull(String userId);

    @Insert
    public void save(JournalLabel journalLabel);

    @Update
    public void update(JournalLabel journalLabel);

    @Delete
    public void delete(JournalLabel journalLabel);

    @Query("DELETE FROM journal_label")
    public void permanentlyDeleteAll();

}
