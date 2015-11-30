package id.bizdir.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import id.bizdir.util.Const;

/**
 * Created by Hendry on 28/07/2015.
 */

@DatabaseTable(tableName = "Opportunity")
public class Opportunity {

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

    @DatabaseField
    private String companyEmail;

    @DatabaseField(dataType = DataType.DATE_STRING,
            format = Const.DATETIME_FORMAT)
    private Date startDate;

    @DatabaseField(dataType = DataType.DATE_STRING,
            format = Const.DATETIME_FORMAT)
    private Date endDate;

    @DatabaseField
    private String file;

    @DatabaseField
    private int viewCount;

    @DatabaseField
    private int emailCount;

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

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getEmailCount() {
        return emailCount;
    }

    public void setEmailCount(int emailCount) {
        this.emailCount = emailCount;
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
