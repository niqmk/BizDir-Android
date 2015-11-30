package id.bizdir.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Hendry on 28/07/2015.
 */

@DatabaseTable(tableName = "Anggota")
public class Anggota implements Parcelable {

    @DatabaseField(id = true)
    private int id;

    //@DatabaseField
    //private int memberType;

    //@DatabaseField
    //private String ktaNo;

    //@DatabaseField(dataType = DataType.DATE_STRING,
    //        format = Const.DATETIME_FORMAT)
    //@DatabaseField
    //private String regDate;

    @DatabaseField(index = true)
    private String name;

    @DatabaseField
    private String description;

    @DatabaseField(index = true)
    private String product;

    //@DatabaseField
    //private String kind;

    @DatabaseField(index = true)
    private int provinceId;

    //@DatabaseField
    //private String provinceName;

    @DatabaseField(index = true)
    private int cityId;

    //@DatabaseField
    //private String cityName;

    //@DatabaseField
    //private String subdistrict;

    //@DatabaseField
    //private String kelurahan;

    @DatabaseField(index = true)
    private String address;

    //@DatabaseField
    //private String posCode;

    @DatabaseField
    private String phone;

    @DatabaseField
    private String fax;

    @DatabaseField
    private String email;

    @DatabaseField
    private String web;

    //@DatabaseField
    //private String director;

    //@DatabaseField
    //private String contactPersonName;

    //@DatabaseField
    //private String contactPersonJabatan;

    //@DatabaseField
    //private String contactPersonPhone;

    //@DatabaseField
    //private String year;

    //@DatabaseField
    //private String buinessField;

    //@DatabaseField
    //private String siup;

    //@DatabaseField
    //private String npwp;

    //@DatabaseField
    //private int rating;

    @DatabaseField
    private int verification;

    @DatabaseField
    private String longitude;

    @DatabaseField
    private String latitude;

    @DatabaseField
    private String thumb;

    @DatabaseField
    private String picture;

    @DatabaseField(index = true)
    private int status;

    @DatabaseField(index = true, defaultValue = "0", canBeNull = false)
    private int favorite;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /*
    public int getMemberType() {
        return memberType;
    }

    public void setMemberType(int memberType) {
        this.memberType = memberType;
    }

    public String getKtaNo() {
        return ktaNo;
    }

    public void setKtaNo(String ktaNo) {
        this.ktaNo = ktaNo;
    }
    */

    /*
    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
    */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    /*
    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    */

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    /*
    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
    */

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    /*
    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
    */

    /*
    public String getSubdistrict() {
        return subdistrict;
    }

    public void setSubdistrict(String subdistrict) {
        this.subdistrict = subdistrict;
    }

    public String getKelurahan() {
        return kelurahan;
    }

    public void setKelurahan(String kelurahan) {
        this.kelurahan = kelurahan;
    }
    */

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /*
    public String getPosCode() {
        return posCode;
    }

    public void setPosCode(String posCode) {
        this.posCode = posCode;
    }
    */

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    /*
    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getContactPersonJabatan() {
        return contactPersonJabatan;
    }

    public void setContactPersonJabatan(String contactPersonJabatan) {
        this.contactPersonJabatan = contactPersonJabatan;
    }

    public String getContactPersonPhone() {
        return contactPersonPhone;
    }

    public void setContactPersonPhone(String contactPersonPhone) {
        this.contactPersonPhone = contactPersonPhone;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getBuinessField() {
        return buinessField;
    }

    public void setBuinessField(String buinessField) {
        this.buinessField = buinessField;
    }

    public String getSiup() {
        return siup;
    }

    public void setSiup(String siup) {
        this.siup = siup;
    }

    public String getNpwp() {
        return npwp;
    }

    public void setNpwp(String npwp) {
        this.npwp = npwp;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    */

    public int getVerification() {
        return verification;
    }

    public void setVerification(int verification) {
        this.verification = verification;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public Anggota() {}
    public Anggota(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        product = in.readString();
        provinceId = in.readInt();
        cityId = in.readInt();
        address = in.readString();
        phone = in.readString();
        fax = in.readString();
        email = in.readString();
        web = in.readString();
        verification = in.readInt();
        longitude = in.readString();
        latitude = in.readString();
        thumb = in.readString();
        picture = in.readString();
        status = in.readInt();
        favorite = in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel arg0, int arg1) {
        arg0.writeInt(id);
        arg0.writeString(name);
        arg0.writeString(description);
        arg0.writeString(product);
        arg0.writeInt(provinceId);
        arg0.writeInt(cityId);
        arg0.writeString(address);
        arg0.writeString(phone);
        arg0.writeString(fax);
        arg0.writeString(email);
        arg0.writeString(web);
        arg0.writeInt(verification);
        arg0.writeString(longitude);
        arg0.writeString(latitude);
        arg0.writeString(thumb);
        arg0.writeString(picture);
        arg0.writeInt(status);
        arg0.writeInt(favorite);
    }
    @SuppressWarnings("rawtypes")
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        @Override
        public Anggota createFromParcel(Parcel in) {
            return new Anggota(in);
        }
        @Override
        public Anggota[] newArray(int size) {
            return new Anggota[size];
        }
    };
}
