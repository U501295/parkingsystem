package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class TicketDAOTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private TicketDAO ticketDAO;
    private Ticket ticket;


   /* public boolean equal(Ticket obj)
    {
        return  (obj instanceof Ticket)
                &&
                (((obj).getParkingSpot()).equals(ticket.getParkingSpot()))
                &&
                (obj).getVehicleRegNumber()== ticket.getVehicleRegNumber()
                &&
                (obj).getPrice()== ticket.getPrice()
                &&
                (obj).getInTime().toInstant().truncatedTo(ChronoUnit.SECONDS).equals(ticket.getInTime().toInstant().truncatedTo(ChronoUnit.SECONDS))
                &&
                (obj).getOutTime().toInstant().truncatedTo(ChronoUnit.SECONDS).equals(ticket.getOutTime().toInstant().truncatedTo(ChronoUnit.SECONDS));


    }*/

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

        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("Failed to set up test mock objects");
        }
    }

    @Test
    public void RecurringFalse() throws SQLException, ClassNotFoundException {
        ticket.setVehicleRegNumber("FALSE");
        assertFalse(ticketDAO.isTicketFromRecurrentUser(ticket.getVehicleRegNumber()));
        ticket.setVehicleRegNumber("TEST");

    }
    @Test
    public void saveTicketTrue() throws SQLException, ClassNotFoundException {
        // il n'est pas possible de mocker la base de donnée étant donné qu'elle est créée directement dans saveTicket
        assertTrue(ticketDAO.saveTicket(ticket));

        //verify(dataBaseConfig, Mockito.times(1)).getConnection();
        //verify(dataBaseConfig, Mockito.times(1)).closeConnection(any(Connection.class));

    }

    @Test
    public void saveTicketFalse() throws SQLException, ClassNotFoundException {
        // il n'est pas possible de mocker la base de donnée étant donné qu'elle est créée directement dans saveTicket
        assertFalse(ticketDAO.saveTicket(null));

        //verify(dataBaseConfig, Mockito.times(1)).getConnection();
        //verify(dataBaseConfig, Mockito.times(1)).closeConnection(any(Connection.class));

    }

    @Test
    public void getTicketVariables() throws SQLException, ClassNotFoundException {
        // il n'est pas possible de mocker la base de donnée étant donné qu'elle est créée directement dans saveTicket
        ticketDAO.saveTicket(ticket);
        Ticket test = ticketDAO.getTicket(ticket.getVehicleRegNumber());

        assertTrue(test instanceof Ticket);
       /* System.out.println(test.getInTime().toInstant().truncatedTo(ChronoUnit.SECONDS));
        System.out.println(test.getOutTime().toInstant().truncatedTo(ChronoUnit.SECONDS));
        //System.out.println(java.sql.Timestamp.valueOf(ticket.getInTime().toString()));
        System.out.println(ticket.getInTime().toInstant().truncatedTo(ChronoUnit.SECONDS));
        System.out.println(ticket.getOutTime().toInstant().truncatedTo(ChronoUnit.SECONDS));
        //System.out.println(ticket.getInTime().getTime());
        assertTrue ((((Ticket) test).getParkingSpot()).equals(ticket.getParkingSpot()));
        assertTrue (((Ticket)test).getVehicleRegNumber()== ticket.getVehicleRegNumber());
        assertTrue (((Ticket)test).getPrice()== ticket.getPrice());
        //assertEqu ( ((Ticket)test).getInTime().toString()== ticket.getInTime().toString());
        //assertTrue (  ((Ticket)test).getOutTime()== ticket.getOutTime());*/

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
