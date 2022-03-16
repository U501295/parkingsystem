package com.parkit.parkingsystem.model;

import java.util.Date;

public class Ticket {
    private int id;
    private ParkingSpot parkingSpot;
    private String vehicleRegNumber;
    private double price;
    private Date inTime = null;
    private Date outTime = null;
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
        if (this.inTime != null){
            Date inTime = (Date)this.inTime.clone();
            return inTime;
        }else{
            return null;
        }
    }

    public void setInTime(Date inTime) {
        if (inTime != null){
            if (this.inTime == null){
                this.inTime = (Date)inTime.clone();
            }else{
                this.inTime.setTime(inTime.getTime());
            }
        }else{
            this.inTime = null;
        }
    }

    public Date getOutTime() {
        if (this.outTime != null){
            Date outTime = (Date)this.outTime.clone();
            return outTime;
        }else{
            return null;
        }
    }

    public void setOutTime(Date outTime) {
        if (outTime == null){
            this.outTime = null;
        }else {
            if (this.outTime != null){
                this.outTime.setTime(outTime.getTime());
            }else{
                this.outTime = (Date)outTime.clone();
            }
        }
    }

    public boolean getIsRecurring() {
        return isRecurring;
    }

    public void setIsRecurring(boolean isRecurring) {
        this.isRecurring = isRecurring;
    }


}
