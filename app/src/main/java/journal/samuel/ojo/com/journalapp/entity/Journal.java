package journal.samuel.ojo.com.journalapp.entity;


import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import static android.arch.persistence.room.ForeignKey.SET_NULL;

@Entity(tableName = "journal", foreignKeys = @ForeignKey(entity = JournalLabel.class,
        parentColumns = "id", childColumns = "journal_label_id", onDelete = SET_NULL),
        indices = {@Index(value = {"journal_label_id"})})
public class Journal {

    @PrimaryKey(autoGenerate = true)
    private Integer id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "journal_text")
    private String journalText;

    @ColumnInfo(name = "journal_label_id")
    private Integer journalLabelId;

    @ColumnInfo(name = "user_id")
    private String userId;

    @ColumnInfo(name = "created_on")

    private Long createdOn;

    @ColumnInfo(name = "updated_on")
    private Long updatedOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getJournalText() {
        return journalText;
    }

    public void setJournalText(String journalText) {
        this.journalText = journalText;
    }

    public Integer getJournalLabelId() {
        return journalLabelId;
    }

    public void setJournalLabelId(Integer journalLabelId) {
        this.journalLabelId = journalLabelId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Long createdOn) {
        this.createdOn = createdOn;
    }

    public Long getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Long updatedOn) {
        this.updatedOn = updatedOn;
    }
}
