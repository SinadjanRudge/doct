package com.triadss.doctrack2.dto;

import android.os.Parcel;
import android.os.Parcelable;

public class AddPatientDto implements Parcelable {

    public AddPatientDto(){
        //Required empty constructor
    }

    private String idNumber;

    private String email;

    private String fullName;

    private String address;

    private String phone;

    private int Age;

    private String course;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public void setIdNumber(String idNumber) {
        this.idNumber = idNumber;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public AddPatientDto(Parcel in) {
        // Read fields from Parcel
        idNumber = in.readString();
        fullName = in.readString();
        email = in.readString();
        address = in.readString();
        Age = in.readString();
        phone = in.readString();
        course = in.readString();
    }

    public static final Creator<AddPatientDto> CREATOR = new Creator<AddPatientDto>() {
        @Override
        public AddPatientDto createFromParcel(Parcel in) {
            return new AddPatientDto(in);
        }

        @Override
        public AddPatientDto[] newArray(int size) {
            return new AddPatientDto[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // Write fields to Parcel
        dest.writeString(idNumber);
        dest.writeString(fullName);
        dest.writeString(email);
        dest.writeString(address);
        dest.writeString(Age);
        dest.writeString(phone);
        dest.writeString(course);
    }
}
