package journal.samuel.ojo.com.journalapp.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import journal.samuel.ojo.com.journalapp.dao.JournalDao;
import journal.samuel.ojo.com.journalapp.dao.JournalLabelDao;
import journal.samuel.ojo.com.journalapp.entity.Journal;
import journal.samuel.ojo.com.journalapp.entity.JournalLabel;

@Database(entities = {Journal.class, JournalLabel.class}, version = 1)
public abstract class JournalDatabase extends RoomDatabase {

    private static JournalDatabase journalDatabase;
    private static final String DATABASE_NAME = "journal_app_db";

    public abstract JournalDao getJournalDao();
    public abstract JournalLabelDao getJournalLabelDao();

    public static JournalDatabase getInstance(Context context) {
        if(journalDatabase == null) {
            synchronized (JournalDatabase.class) {
                journalDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        JournalDatabase.class, DATABASE_NAME).build();
            }
        }
        return journalDatabase;
    }
}
