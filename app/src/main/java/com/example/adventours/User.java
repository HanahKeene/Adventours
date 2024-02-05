package com.example.adventours;

public class User {

    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String gender;
    private String age;
    private String birthday;
    private String city;
    private String phone;

    public User() {
    }

    public User(String username, String email, String firstName, String lastName, String gender, String age, String birthday, String city, String phone) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.age = age;
        this.birthday = birthday;
        this.city = city;
        this.phone = phone;
    }

    public User(String firstName, String lastName, String city, String number, String bday, String email, String username) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.city = city;
        this.phone = number;  // Assuming "number" corresponds to the "phone" field
        this.birthday = bday;
        this.email = email;
        this.username = username;
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
