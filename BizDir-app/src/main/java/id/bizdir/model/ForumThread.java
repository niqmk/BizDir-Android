package id.bizdir.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import id.bizdir.util.Const;

/**
 * Created by Hendry on 28/07/2015.
 */

@DatabaseTable(tableName = "ForumThread")
public class ForumThread {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String title;

    @DatabaseField(index = true)
    private int categoryId;

    @DatabaseField
    private String categoryTitle;

    @DatabaseField
    private String description;

    @DatabaseField(index = true)
    private int replyCount;

    @DatabaseField(index = true)
    private int viewCount;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getReplyCount() {
        return replyCount;
    }

    public void setReplyCount(int replyCount) {
        this.replyCount = replyCount;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
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
