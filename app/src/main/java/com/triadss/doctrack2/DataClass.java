package com.triadss.doctrack2;

public class DataClass {
    private String datastudentId;
    private String dataname;
    private String datagender;

    private String datacourse;
    private String datayear;
    private String datasection;
    private String dataroom;
    private String dataImage;
    private String datadevice;
    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setDataName(String dataname) {
        this.dataname = dataname;
    }

    public void setDataCourse(String datacourse) {
        this.datacourse = datacourse;
    }

    public void setDataRoom(String dataroom) {
        this.dataroom = dataroom;
    }

    public void setDataDevice(String datadevice) {
        this.datadevice = datadevice;
    }

    public String getDataStudentId() {
        return datastudentId;
    }

    public String getDataName() {
        return dataname;
    }

    public String getDataGender() {
        return datagender;
    }

    public String getDataCourse() {
        return datacourse;
    }

    public String getDataYear() {
        return datayear;
    }

    public String getDataSection() {
        return datasection;
    }

    public String getDataRoom() {
        return dataroom;
    }

    public String getDataImage() {
        return dataImage;
    }

    public String getDataDevice() {
        return datadevice;
    }

    public DataClass(String datastudentId, String dataname, String datagender, String datacourse, String datayear, String datasection, String dataroom, String datadevice, String dataImage) {
        this.datastudentId = datastudentId;
        this.dataname = dataname;
        this.datagender = datagender;
        this.datacourse = datacourse;
        this.datayear = datayear;
        this.datasection = datasection;
        this.dataroom = dataroom;
        this.datadevice = datadevice;
        this.dataImage = dataImage;
    }

    public DataClass() {
    }
}
