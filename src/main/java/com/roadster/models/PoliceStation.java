package com.roadster.models;

public class PoliceStation {
    private int stationId;
    private String name;
    private String contactInfo;
    private String lmgrcCode;

    // Getters and setters
    public int getStationId() { return stationId; }
    public void setStationId(int stationId) { this.stationId = stationId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getContactInfo() { return contactInfo; }
    public void setContactInfo(String contactInfo) { this.contactInfo = contactInfo; }

    public String getLmgrcCode() { return lmgrcCode; }
    public void setLmgrcCode(String lmgrcCode) { this.lmgrcCode = lmgrcCode; }
}