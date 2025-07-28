package com.roadster.models;

import javafx.beans.property.*;

/**
 * Driver model class for driver management
 */
public class Driver {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty licenseNumber;
    private final StringProperty phoneNumber;
    private final StringProperty email;
    private final StringProperty vehicleAssigned;
    private final BooleanProperty isAvailable;
    private final StringProperty status;
    
    public Driver(String id, String name, String licenseNumber, String phoneNumber, String email) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.licenseNumber = new SimpleStringProperty(licenseNumber);
        this.phoneNumber = new SimpleStringProperty(phoneNumber);
        this.email = new SimpleStringProperty(email);
        this.vehicleAssigned = new SimpleStringProperty("");
        this.isAvailable = new SimpleBooleanProperty(true);
        this.status = new SimpleStringProperty("Active");
    }
    
    // ID property
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }
    public StringProperty idProperty() { return id; }
    
    // Name property
    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return name; }
    
    // License number property
    public String getLicenseNumber() { return licenseNumber.get(); }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber.set(licenseNumber); }
    public StringProperty licenseNumberProperty() { return licenseNumber; }
    
    // Phone number property
    public String getPhoneNumber() { return phoneNumber.get(); }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber.set(phoneNumber); }
    public StringProperty phoneNumberProperty() { return phoneNumber; }
    
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
    
    @Override
    public String toString() {
        return "Driver{" +
                "id='" + getId() + '\'' +
                ", name='" + getName() + '\'' +
                ", licenseNumber='" + getLicenseNumber() + '\'' +
                ", phoneNumber='" + getPhoneNumber() + '\'' +
                ", email='" + getEmail() + '\'' +
                ", vehicleAssigned='" + getVehicleAssigned() + '\'' +
                ", isAvailable=" + isAvailable() +
                ", status='" + getStatus() + '\'' +
                '}';
    }
} 