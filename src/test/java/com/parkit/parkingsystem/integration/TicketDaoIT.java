package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.ParkingType;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TicketDaoIT {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private TicketDAO ticketDAO;
    private Ticket ticket;
    private static DataBasePrepareService dataBasePrepareService;


    @BeforeEach
    private void setUpPerTest() {
        try {
            ParkingSpot parkingSpot = new ParkingSpot(1, ParkingType.CAR, false);
            ticket = new Ticket();
            ticketDAO = new TicketDAO();
            ticketDAO.dataBaseConfig = dataBaseTestConfig;
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("TEST");
            ticket.setPrice(2.0);
            //ticket.setInTime(new Date());
            //ticket.setInTime(new Timestamp());
            //ticket.setInTime(new Date(2022,03,10, 21,50,40));
            ticket.setInTime(new Date(System.currentTimeMillis()));
            ticket.setOutTime(new Date(System.currentTimeMillis()  + (  24 * 60 * 60 * 1000)));
            dataBasePrepareService.clearDataBaseEntries();

        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @AfterAll
    private static void tearDown(){
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void saveTicketTrue(){
        // il n'est pas possible de mocker la base de donnée étant donné qu'elle est créée directement dans saveTicket
        assertTrue(ticketDAO.saveTicket(ticket));


    }

    @Test
    public void saveTicketFalse() throws SQLException, ClassNotFoundException {
        // il n'est pas possible de mocker la base de donnée étant donné qu'elle est créée directement dans saveTicket
        assertFalse(ticketDAO.saveTicket(null));

    }

    @Test
    public void getTicketVariables() throws SQLException, ClassNotFoundException {
        // il n'est pas possible de mocker la base de donnée étant donné qu'elle est créée directement dans saveTicket
        ticketDAO.saveTicket(ticket);
        Ticket test = ticketDAO.getTicket(ticket.getVehicleRegNumber());
        assertTrue(test instanceof Ticket);


    }


    @Test
    public void RecurringFalse() throws SQLException, ClassNotFoundException {
        ticket.setVehicleRegNumber("FALSE");
        assertFalse(ticketDAO.isTicketFromRecurrentUser(ticket.getVehicleRegNumber()));
        ticket.setVehicleRegNumber("TEST");

    }

    @Test
    public void RecurringTrue() throws SQLException, ClassNotFoundException {
        ticketDAO.saveTicket(ticket);
        assertTrue(ticketDAO.isTicketFromRecurrentUser(ticket.getVehicleRegNumber()));

    }

    @Test
    public void UpdateTicketTrue() throws SQLException, ClassNotFoundException {
        assertTrue(ticketDAO.updateTicket(ticket));

    }

    @Test
    public void UpdateTicketFalse() throws SQLException, ClassNotFoundException {
        assertFalse(ticketDAO.updateTicket(null));

    }






}
