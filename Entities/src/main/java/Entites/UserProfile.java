package Entites;

import java.io.Serializable;

public class UserProfile implements Serializable {
    private int id;
    private String fullName;
    private int age;
    private Sex sex;

    public UserProfile() {
    }

    public UserProfile(int id, String fullName, int age, Sex sex) {
        this.id = id;
        this.fullName = fullName;
        this.age = age;
        this.sex = sex;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }
}
