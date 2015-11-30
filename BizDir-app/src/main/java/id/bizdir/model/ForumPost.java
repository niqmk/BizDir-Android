package id.bizdir.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import id.bizdir.util.Const;

/**
 * Created by Hendry on 28/07/2015.
 */

@DatabaseTable(tableName = "ForumPost")
public class ForumPost {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField(index = true)
    private int threadId;

    @DatabaseField
    private String threadTitle;

    @DatabaseField
    private String content;

    @DatabaseField
    private String user;

    @DatabaseField(index = true, dataType = DataType.DATE_STRING,
            format = Const.DATETIME_FORMAT)
    private Date createDate;

    @DatabaseField(index = true)
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getThreadId() {
        return threadId;
    }

    public void setThreadId(int threadId) {
        this.threadId = threadId;
    }

    public String getThreadTitle() {
        return threadTitle;
    }

    public void setThreadTitle(String threadTitle) {
        this.threadTitle = threadTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
