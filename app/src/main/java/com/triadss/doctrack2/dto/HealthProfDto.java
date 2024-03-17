package com.triadss.doctrack2.dto;

import com.google.firebase.Timestamp;

public class HealthProfDto {

    private String fullName;
    private String position;
    private String userName;
    private String password;
    private String healthProfId;
    private String gender;
    private String email;

    public HealthProfDto() {

    }

    public HealthProfDto(String fullName, String position, String userName, String email, String password,
            String gender) {
        this.fullName = fullName;
        this.position = position;
        this.userName = userName;
        this.password = password;
        this.gender = gender;
        this.email = email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }
    public void setHealthProfid(String healthProfId) {
        this.healthProfId = healthProfId;
    }

    public String setFullName(String HWN) {
        return HWN;
    }

    public String setPosition(String position) {
        return position;
    }

    public String setUserName(String userName) {
        return userName;
    }

    public String setPassword(String password) {
        return password;
    }

    public String setGender(String gender) {
        return gender;
    }

    public String getFullName() {
        return fullName;
    }

    public String getHealthProfid() {
        return healthProfId;
    }

    public String getPosition() {
        return position;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getGender() {
        return gender;
    }

}
