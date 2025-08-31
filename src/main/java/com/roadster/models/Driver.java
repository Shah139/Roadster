package com.roadster.models;

import javafx.beans.property.*;

public class Driver {
    private final StringProperty driverId;
    private final StringProperty name;
    private final StringProperty licenseNumber;
    private final StringProperty contactInfo;
    private final StringProperty email;
    private final StringProperty vehicleAssigned;
    private final BooleanProperty isAvailable;
    private final StringProperty status;
    private final StringProperty district;

    public Driver() {
        this.driverId = new SimpleStringProperty();
        this.name = new SimpleStringProperty();
        this.licenseNumber = new SimpleStringProperty();
        this.contactInfo = new SimpleStringProperty();
        this.email = new SimpleStringProperty();
        this.vehicleAssigned = new SimpleStringProperty();
        this.isAvailable = new SimpleBooleanProperty();
        this.status = new SimpleStringProperty();
        this.district = new SimpleStringProperty();
    }

    public Driver(String driverId, String name, String licenseNumber, String contactInfo, String email) {
        this.driverId = new SimpleStringProperty(driverId);
        this.name = new SimpleStringProperty(name);
        this.licenseNumber = new SimpleStringProperty(licenseNumber);
        this.contactInfo = new SimpleStringProperty(contactInfo);
        this.email = new SimpleStringProperty(email);
        this.vehicleAssigned = new SimpleStringProperty("");
        this.isAvailable = new SimpleBooleanProperty(true);
        this.status = new SimpleStringProperty("Active");
        this.district = new SimpleStringProperty("");
    }

    // Constructor with district
    public Driver(String driverId, String name, String licenseNumber, String contactInfo, String email, String district) {
        this.driverId = new SimpleStringProperty(driverId);
        this.name = new SimpleStringProperty(name);
        this.licenseNumber = new SimpleStringProperty(licenseNumber);
        this.contactInfo = new SimpleStringProperty(contactInfo);
        this.email = new SimpleStringProperty(email);
        this.vehicleAssigned = new SimpleStringProperty("");
        this.isAvailable = new SimpleBooleanProperty(true);
        this.status = new SimpleStringProperty("Active");
        this.district = new SimpleStringProperty(district);
    }

    // driverId property
    public String getDriverId() { return driverId.get(); }
    public void setDriverId(String driverId) { this.driverId.set(driverId); }
    public StringProperty driverIdProperty() { return driverId; }

    // Name property
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return name; }

    // License number property
    public String getLicenseNumber() { return licenseNumber.get(); }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber.set(licenseNumber); }
    public StringProperty licenseNumberProperty() { return licenseNumber; }

    // Contact info property
    public String getContactInfo() { return contactInfo.get(); }
    public void setContactInfo(String contactInfo) { this.contactInfo.set(contactInfo); }
    public StringProperty contactInfoProperty() { return contactInfo; }

    // Email property
    public String getEmail() { return email.get(); }
    public void setEmail(String email) { this.email.set(email); }
    public StringProperty emailProperty() { return email; }

    // Vehicle assigned property
    public String getVehicleAssigned() { return vehicleAssigned.get(); }
    public void setVehicleAssigned(String vehicleAssigned) { this.vehicleAssigned.set(vehicleAssigned); }
    public StringProperty vehicleAssignedProperty() { return vehicleAssigned; }

    // Available property
    public boolean isAvailable() { return isAvailable.get(); }
    public void setAvailable(boolean available) { this.isAvailable.set(available); }
    public BooleanProperty availableProperty() { return isAvailable; }

    // Status property
    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }
    public StringProperty statusProperty() { return status; }

    // District property
    public String getDistrict() { return district.get(); }
    public void setDistrict(String district) { this.district.set(district); }
    public StringProperty districtProperty() { return district; }

    @Override
    public String toString() {
        return "Driver{" +
                "driverId='" + getDriverId() + '\'' +
                ", name='" + getName() + '\'' +
                ", licenseNumber='" + getLicenseNumber() + '\'' +
                ", contactInfo='" + getContactInfo() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", vehicleAssigned='" + getVehicleAssigned() + '\'' +
                ", isAvailable=" + isAvailable() +
                ", status='" + getStatus() + '\'' +
                ", district='" + getDistrict() + '\'' +
                '}';
    }
}