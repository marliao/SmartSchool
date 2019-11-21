package com.marliao.smartschool.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Market")
public class Market {
    @DatabaseField(columnName = "_id",generatedId = true)
    private int _id;
    @DatabaseField(columnName = "product_name")
    private String product_name;
    @DatabaseField(columnName = "discription")
    private String discription;
    @DatabaseField(columnName = "price")
    private int price;
    @DatabaseField(columnName = "phone_number")
    private String phone_number;
    @DatabaseField(columnName = "send_user")
    private String send_user;
    @DatabaseField(columnName = "time")
    private long time;

    public Market() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getSend_user() {
        return send_user;
    }

    public void setSend_user(String send_user) {
        this.send_user = send_user;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
