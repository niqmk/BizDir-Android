package id.bizdir.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Hendry on 08/08/2015.
 */
@DatabaseTable(tableName = "Interest")
public class Interest {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField(foreign=true, foreignAutoCreate=true, foreignAutoRefresh=true)
    private MemberBizdir memberBizdir;

    @DatabaseField
    private String name;

    @DatabaseField
    private int status;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public MemberBizdir getMemberBizdir() {
        return memberBizdir;
    }

    public void setMemberBizdir(MemberBizdir memberBizdir) {
        this.memberBizdir = memberBizdir;
    }
}
