package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;

public class FareCalculatorService {

    public void calculateFare(Ticket ticket){
        if( (ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime())) ){
            throw new IllegalArgumentException("Out time provided is incorrect:"+ticket.getOutTime().toString());
        }

        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();



        double duration;
        final int sec_in_millisec = 1000, min_in_sec=60,hours_in_min=60;
        duration = (outHour - inHour)/(sec_in_millisec*min_in_sec*hours_in_min);
        //TODO : show new feature
        if  (duration<0.5){
            ticket.setPrice(0);

        }else {

            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown Parking Type");
            }
        }
        if (ticket.getIsRecurring()){
            ticket.setPrice(ticket.getPrice()-(0.05*ticket.getPrice()));
        }

    }
}