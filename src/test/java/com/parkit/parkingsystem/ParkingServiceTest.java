package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static com.parkit.parkingsystem.constants.ParkingType.BIKE;
import static com.parkit.parkingsystem.constants.ParkingType.CAR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ParkingServiceTest {

    private static DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingService parkingService;
    private static Ticket ticket;

    @Mock
    private static InputReaderUtil inputReaderUtil;
    @Mock
    private static ParkingSpotDAO parkingSpotDAO;
    @Mock
    private static TicketDAO ticketDAO;

    @BeforeEach
    private void setUpPerTest() {
        try {
            ParkingSpot parkingSpot = new ParkingSpot(1, CAR, false);
            ticket = new Ticket();
            ticket.setInTime(new Date(System.currentTimeMillis() - (60 * 60 * 1000)));
            ticket.setParkingSpot(parkingSpot);
            ticket.setVehicleRegNumber("ABCDEF");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to set up test mock objects");
        }
    }


    @Test
    public void getError_WhenVehicleTypeInput_IsIncorrect() {
        when(inputReaderUtil.readSelection()).thenReturn(0);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        try {
            parkingService.getVehicleType();
        } catch (Exception e) {
            Assertions.assertThat(e)
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("Entered input is invalid");
        }
    }

    @Test
    public void getNextParkingNumberIfAvailable_WhenASpotIsAvailable(){

        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        assertEquals(parkingService.getNextParkingNumberIfAvailable().isAvailable(), true);

    }

    @Test
    public void getError_With_getNextParkingNumberIfAvailable_WhenParkingIsFull(){
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(0);
        try {
            parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
            parkingService.getNextParkingNumberIfAvailable();
        } catch (IndexOutOfBoundsException e) {
            Assertions.assertThat(e)
                    .isInstanceOf(IndexOutOfBoundsException.class)
                    .hasMessage("Error fetching parking number from DB. Parking slots might be full");
        }
    }


    @Test
    public void processExitingVehicle_WithMoreThan24HStay(){
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        /*Date outTime = new Date();
        outTime.setTime(ticket.getInTime().getTime() + (25 * 60 * 60 * 1000));
        ticket.setOutTime(outTime);
        parkingService.processExitingVehicle();*/
        parkingService.processExitingVehicleWith25HStay();
        double duration = (ticket.getOutTime().getTime()) - (ticket.getInTime().getTime());
        Assertions.assertThat(duration).isEqualTo(25 * 60 * 60 * 1000);
        Assertions.assertThat(ticket.getPrice()).isEqualTo(25 * 1.5);
    }

    @Test
    public void When_processIncomingVehicle_thenATicketIsSaved(){
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(1);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        verify(ticketDAO, Mockito.times(1)).saveTicket(any(Ticket.class));

    }

    @Test
    public void When_processIncomingVehicle_andParkingIsFull_ThenGetError(){
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(parkingSpotDAO.getNextAvailableSlot(any(ParkingType.class))).thenReturn(0);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        try {
            parkingService.processIncomingVehicle();
        } catch (Exception e) {
            Assertions.assertThat(e)
                    .isInstanceOf(IndexOutOfBoundsException.class)
                    .hasMessage("Unable to process incoming vehicle");
        }
    }

    @Test
    public void When_processExitingVehicleTest_Then_ParkingIsUpdated(){
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        when(ticketDAO.getTicket(anyString())).thenReturn(ticket);
        when(ticketDAO.updateTicket(any(Ticket.class))).thenReturn(true);
        when(parkingSpotDAO.updateParking(any(ParkingSpot.class))).thenReturn(true);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        verify(parkingSpotDAO, Mockito.times(1)).updateParking(any(ParkingSpot.class));
    }

    @Test
    public void VehicleRegNumberResult() throws Exception {
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        assertEquals(parkingService.getVehicleRegNumber(), "ABCDEF");
    }

    @Test
    public void getVehicleType_ReturnsAVehicleType(){
        when(inputReaderUtil.readSelection()).thenReturn(1);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        assertEquals(parkingService.getVehicleType(), CAR);
    }

    @Test
    public void getVehicleType_ReturnsABikeType(){
        when(inputReaderUtil.readSelection()).thenReturn(2);
        parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        assertEquals(parkingService.getVehicleType(), BIKE);
    }


}
