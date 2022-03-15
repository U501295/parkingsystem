package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ParkingSpotDaoIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private TicketDAO ticketDAO;
    private Ticket ticket;
    private ParkingSpot parkingSpot;
    private ParkingSpotDAO parkingSpotDAO;
    private static DataBasePrepareService dataBasePrepareService = new DataBasePrepareService();

    @BeforeEach
    private void setUpPerTest() {
        try {
            parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            ticket = new Ticket();
            ticketDAO = new TicketDAO();
            parkingSpotDAO = new ParkingSpotDAO();
            parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
            ticketDAO.dataBaseConfig = dataBaseTestConfig;
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("TEST");
            ticket.setPrice(2.0);
            ticket.setInTime(new Date(System.currentTimeMillis()));
            ticket.setOutTime(new Date(System.currentTimeMillis() + (24 * 60 * 60 * 1000)));
            dataBasePrepareService.clearDataBaseEntries();

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }

    /*@AfterAll
    private static void tearDown(){
        dataBasePrepareService.clearDataBaseEntries();
    }*/

    @Test
    public void getNextAvailableSlotFunctionnal() throws SQLException, ClassNotFoundException {
        assertEquals(parkingSpotDAO.getNextAvailableSlot(ticket.getParkingSpot().getParkingType()),1);

    }

    @Test
    public void updateParkingTrue() throws SQLException, ClassNotFoundException {
        assertTrue(parkingSpotDAO.updateParking(ticket.getParkingSpot()));

    }


    }



