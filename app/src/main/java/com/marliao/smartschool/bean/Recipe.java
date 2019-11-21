package com.marliao.smartschool.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Recipe")
public class Recipe {
    @DatabaseField(columnName = "_id", generatedId = true)
    private int _id;
    @DatabaseField(columnName = "breakfast")
    private String breakfast;
    @DatabaseField(columnName = "launch")
    private String launch;
    @DatabaseField(columnName = "dinner")
    private String dinner;
    @DatabaseField(columnName = "week")
    private int week;

    public Recipe() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getBreakfast() {
        return breakfast;
    }

    public void setBreakfast(String breakfast) {
        this.breakfast = breakfast;
    }

    public String getLaunch() {
        return launch;
    }

    public void setLaunch(String launch) {
        this.launch = launch;
    }

    public String getDinner() {
        return dinner;
    }

    public void setDinner(String dinner) {
        this.dinner = dinner;
    }

    public int getWeek() {
        return week;
    }

    public void setWeek(int week) {
        this.week = week;
    }
}
