package com.parkit.parkingsystem.model;

import java.util.Date;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime;
    private Date outTime;
    private boolean isRecurring;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public String getVehicleRegNumber() {
        return vehicleRegNumber;
    }

    public void setVehicleRegNumber(String vehicleRegNumber) {
        this.vehicleRegNumber = vehicleRegNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getInTime() {
        return inTime = new Date(inTime.getTime());
    }

    public void setInTime(Date inTime) {
        this.inTime = new Date(inTime.getTime());
    }

    public Date getOutTime() {
        return outTime = new Date(outTime.getTime());
    }

    public void setOutTime(Date outTime) {
        this.outTime = new Date(outTime.getTime());
    }

    public boolean getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(boolean isRecurring) {
        this.isRecurring = isRecurring;
    }


}
