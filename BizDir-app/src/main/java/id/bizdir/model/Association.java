package id.bizdir.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Hendry on 25/09/2015.
 */

@DatabaseTable(tableName = "Association")
public class Association {
    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String name;

    @DatabaseField
    private String abbr;

    @DatabaseField
    private int cityId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getAbbr() {
        return abbr;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbbr(String abbr) { this.abbr = abbr; }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }
}
