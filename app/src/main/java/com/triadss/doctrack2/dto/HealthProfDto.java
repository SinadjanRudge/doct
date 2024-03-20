package com.triadss.doctrack2.dto;

public class HealthProfDto {

    private String HWN;
    private String position;
    private String userName;
    private String password;
    private String healthProfId;
    private String appointmentId;
    private String gender;



    private String email;

    public HealthProfDto()
    {

    }
    public HealthProfDto(String HWN, String position, String userName, String password, String appointmentId,
                         String gender) {
        this.HWN = HWN;
        this.position = position;
        this.userName = userName;
        this.password = password;
        this.appointmentId = appointmentId;
        this.gender = gender;
    }
    public void setHealthProfid(String healthProfId) {
        this.healthProfId = healthProfId;
    }
    public String setFullName(String HWN) { return HWN;}
    public String setPosition(String position) { return position;}
    public String setUserName(String userName) { return userName;}
    public String setPassword(String password) { return password;}
    public String setAppointmentId(String appointmentId) { return appointmentId;}
    public String setGender(String gender) { return gender;}

    public String getFullName() {return HWN;}
    public String getHealthProfid() {return healthProfId;}
    public String getPosition() {return position;}
    public String getUserName() {return userName;}
    public String getPassword() {return password;}
    public String getAppointmentId() {return appointmentId;}
    public String getGender() {return gender;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
