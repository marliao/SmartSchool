package com.marliao.smartschool.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "Grade")
public class Grade {
    @DatabaseField(columnName = "_id",generatedId = true)
    private int _id;
    @DatabaseField(columnName = "student_name")
    private String student_name;
    @DatabaseField(columnName = "student_id")
    private String student_id;
    @DatabaseField(columnName = "math")
    private int math;
    @DatabaseField(columnName = "english")
    private int english;
    @DatabaseField(columnName = "chinese")
    private int chinese;
    private int all_grade;

    public int getAll_grade() {
        return all_grade;
    }

    public void setAll_grade(int all_grade) {
        this.all_grade = all_grade;
    }

    public Grade() {
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public int getMath() {
        return math;
    }

    public void setMath(int math) {
        this.math = math;
    }

    public int getEnglish() {
        return english;
    }

    public void setEnglish(int english) {
        this.english = english;
    }

    public int getChinese() {
        return chinese;
    }

    public void setChinese(int chinese) {
        this.chinese = chinese;
    }
}
