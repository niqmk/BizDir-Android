package id.bizdir.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

/**
 * Created by Hendry on 28/07/2015.
 */

@DatabaseTable(tableName = "DownloadRoot")
public class DownloadRoot {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField(index = true)
    private String title;

    @DatabaseField(index = true)
    private int status;

    private List<DownloadSub> downloadSubList;

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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DownloadSub> getDownloadSubList() {
        return downloadSubList;
    }

    public void setDownloadSubList(List<DownloadSub> downloadSubList) {
        this.downloadSubList = downloadSubList;
    }
}
