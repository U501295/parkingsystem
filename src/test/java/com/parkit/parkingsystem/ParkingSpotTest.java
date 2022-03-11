package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotTest {

    private ParkingSpotDAO parkingSpotDAO;
    private TicketDAO ticketDAO;
    private Ticket ticket;

    @BeforeEach
    private void setUpPerTest() {
        try {
            /*when(dataBaseConfig.getConnection()).thenReturn(any(Connection.class));*/
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            ticket = new Ticket();
            ticketDAO = new TicketDAO();
            parkingSpotDAO = new ParkingSpotDAO();
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("TEST");
            ticket.setPrice(2.0);
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setOutTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000) + (24 * 60 * 60 * 1000)));

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void getNextAvailableSpotFunctional() throws SQLException, ClassNotFoundException {
        assertEquals(parkingSpotDAO.getNextAvailableSlot(ticket.getParkingSpot().getParkingType()),1);

    }

    @Test
    public void getNextAvailableSpotWhenParkingIsFull() throws SQLException, ClassNotFoundException {

        assertEquals(parkingSpotDAO.getNextAvailableSlot(ticket.getParkingSpot().getParkingType()),-1);

    }
}