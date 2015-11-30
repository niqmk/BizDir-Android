package id.bizdir.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Hendry on 28/07/2015.
 * Selalu Online pada object ini
 */

@DatabaseTable(tableName = "NewsStock")
public class NewsStock {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField(index = true)
    private String stock_code;

    @DatabaseField
    private String stock_name;

    @DatabaseField
    private String company;

    @DatabaseField
    private String market_board;

    @DatabaseField
    private String last;

    @DatabaseField
    private String change;

    @DatabaseField
    private String percent_change;

    @DatabaseField
    private String close;

    @DatabaseField
    private String open;

    @DatabaseField
    private String high;

    @DatabaseField
    private String low;

    @DatabaseField
    private String volume;

    @DatabaseField
    private String value;

    @DatabaseField(index = true, canBeNull = false, defaultValue = "0")
    private int type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStock_code() {
        return stock_code;
    }

    public void setStock_code(String stock_code) {
        this.stock_code = stock_code;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getMarket_board() {
        return market_board;
    }

    public void setMarket_board(String market_board) {
        this.market_board = market_board;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getPercent_change() {
        return percent_change;
    }

    public void setPercent_change(String percent_change) {
        this.percent_change = percent_change;
    }

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(String open) {
        this.open = open;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
