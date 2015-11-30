package id.bizdir.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Hendry on 28/07/2015.
 */

@DatabaseTable(tableName = "AnggotaSubCategoryAssignment")
public class AnggotaSubCategoryAssignment {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private int anggotaId;

    @DatabaseField(index = true)
    private int subCategoryId;

    @DatabaseField(index = true)
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnggotaId() {
        return anggotaId;
    }

    public void setAnggotaId(int anggotaId) {
        this.anggotaId = anggotaId;
    }

    public int getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
