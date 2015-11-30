package id.bizdir.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

import id.bizdir.util.Const;

/**
 * Created by Hendry on 29/07/2015.
 */

@DatabaseTable(tableName = "TableSynch")
public class TableSynch {

    @DatabaseField(id = true)
    private String table;

    @DatabaseField(dataType = DataType.DATE_STRING,
            format = Const.DATETIME_FORMAT)
    private Date lastSynch;

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public Date getLastSynch() {
        return lastSynch;
    }

    public void setLastSynch(Date lastSynch) {
        this.lastSynch = lastSynch;
    }
}
