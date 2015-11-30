package id.bizdir.model;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import id.bizdir.util.Const;

/**
 * Created by Hendry on 28/07/2015.
 */

@DatabaseTable(tableName = "MemberBizdir")
public class MemberBizdir {

    @DatabaseField(id = true)
    private int id;

    @DatabaseField
    private String email;

    @DatabaseField
    private String name;

    @DatabaseField
    private int gender;

    @DatabaseField(dataType = DataType.DATE_STRING,
            format = Const.DATETIME_FORMAT)
    private Date birthday;

    @DatabaseField
    private String nokta;

    @ForeignCollectionField
    private Collection<Interest> interest = new ArrayList<>();

    @DatabaseField
    private String bizdirid;

    @DatabaseField
    private String phone;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getNokta() {
        return nokta;
    }

    public void setNokta(String nokta) {
        this.nokta = nokta;
    }

    public Collection<Interest> getInterest() {
        return interest;
    }

    public void setInterest(Collection<Interest> interest) {
        this.interest = interest;
    }

    public String getBizdirid() {
        return bizdirid;
    }

    public void setBizdirid(String bizdirid) {
        this.bizdirid = bizdirid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
