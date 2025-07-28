package com.roadster.models;

import javafx.beans.property.*;

/**
 * Model class representing a Car in the Roadster application
 */
public class Car {
    private final StringProperty brand;
    private final StringProperty model;
    private final IntegerProperty year;
    private final DoubleProperty price;
    private final StringProperty color;
    private final BooleanProperty available;

    public Car() {
        this.brand = new SimpleStringProperty("");
        this.model = new SimpleStringProperty("");
        this.year = new SimpleIntegerProperty(0);
        this.price = new SimpleDoubleProperty(0.0);
        this.color = new SimpleStringProperty("");
        this.available = new SimpleBooleanProperty(true);
    }

    public Car(String brand, String model, int year, double price, String color) {
        this.brand = new SimpleStringProperty(brand);
        this.model = new SimpleStringProperty(model);
        this.year = new SimpleIntegerProperty(year);
        this.price = new SimpleDoubleProperty(price);
        this.color = new SimpleStringProperty(color);
        this.available = new SimpleBooleanProperty(true);
    }

    // Brand property
    public String getBrand() { return brand.get(); }
    public void setBrand(String brand) { this.brand.set(brand); }
    public StringProperty brandProperty() { return brand; }

    // Model property
    public String getModel() { return model.get(); }
    public void setModel(String model) { this.model.set(model); }
    public StringProperty modelProperty() { return model; }

    // Year property
    public int getYear() { return year.get(); }
    public void setYear(int year) { this.year.set(year); }
    public IntegerProperty yearProperty() { return year; }

    // Price property
    public double getPrice() { return price.get(); }
    public void setPrice(double price) { this.price.set(price); }
    public DoubleProperty priceProperty() { return price; }

    // Color property
    public String getColor() { return color.get(); }
    public void setColor(String color) { this.color.set(color); }
    public StringProperty colorProperty() { return color; }

    // Available property
    public boolean isAvailable() { return available.get(); }
    public void setAvailable(boolean available) { this.available.set(available); }
    public BooleanProperty availableProperty() { return available; }

    @Override
    public String toString() {
        return String.format("%s %s (%d) - $%.2f", 
            getBrand(), getModel(), getYear(), getPrice());
    }
} 