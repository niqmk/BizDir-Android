package id.bizdir.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Hendry on 28/07/2015.
 */

@DatabaseTable(tableName = "AnggotaSubCategory")
public class AnggotaSubCategory {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String title;

    @DatabaseField(index = true)
    private int categoryId;

    @DatabaseField
    private String categoryTitle;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
