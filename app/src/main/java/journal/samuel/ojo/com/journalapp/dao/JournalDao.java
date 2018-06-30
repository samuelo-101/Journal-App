package journal.samuel.ojo.com.journalapp.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import journal.samuel.ojo.com.journalapp.entity.Journal;

@Dao
public interface JournalDao {

    @Query("SELECT * FROM journal WHERE user_id = :userId ORDER BY created_on DESC")
    public LiveData<List<Journal>> findAllForUser(String userId);

    @Query("SELECT * FROM journal WHERE id = :id")
    public Journal findById(int id);

    @Insert
    public void save(Journal journal);

    @Insert
    public void saveAll(Journal... journals);

    @Update
    public void update(Journal journal);

    @Delete
    public void delete(Journal journal);

    @Query("DELETE FROM journal WHERE id = :id")
    public void delete(int id);
}
