package com.example.firestore.Model;

public class User {

    private String first_name;
    private String last_name;
    private String contact_number;
    private String alternate_contact_number;
    private String email;
    private String blood_group;
    private String religion;
    private String present_address;
    private String weight;
    private String dob;

    private String password;
    private String user_type;

    public User() {
    }

    public User(String first_name, String last_name, String contact_number,
                String alternate_contact_number, String email, String blood_group, String religion,
                String present_address, String weight, String dob,
                String password, String user_type) {

        this.first_name = first_name;
        this.last_name = last_name;
        this.contact_number = contact_number;
        this.alternate_contact_number = alternate_contact_number;
        this.email = email;
        this.blood_group = blood_group;
        this.religion = religion;
        this.present_address = present_address;
        this.weight = weight;
        this.dob = dob;

        this.password = password;
        this.user_type = user_type;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getContact_number() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = contact_number;
    }

    public String getAlternate_contact_number() {
        return alternate_contact_number;
    }

    public void setAlternate_contact_number(String alternate_contact_number) {
        this.alternate_contact_number = alternate_contact_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBlood_group() {
        return blood_group;
    }

    public void setBlood_group(String blood_group) {
        this.blood_group = blood_group;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getPresent_address() {
        return present_address;
    }

    public void setPresent_address(String present_address) {
        this.present_address = present_address;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
    }
}
