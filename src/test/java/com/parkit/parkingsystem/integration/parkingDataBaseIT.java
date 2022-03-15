package com.parkit.parkingsystem.integration;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.integration.service.DataBasePrepareService;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author : JULIEN BARONI
 * @version : 1.0
 * <p>
 * Cette classe permet de tester les échanges entre un utilisateur et la base de donnée
 * <p>
 */
@ExtendWith(MockitoExtension.class)
public class parkingDataBaseIT {

    private static final DataBaseTestConfig dataBaseTestConfig = new DataBaseTestConfig();
    private static ParkingSpotDAO parkingSpotDAO;
    private static TicketDAO ticketDAO;
    private static DataBasePrepareService dataBasePrepareService;

    @Mock
    private static InputReaderUtil inputReaderUtil;

    /**
     * Initialisation de la base de donnée, qui n'est pas mockée étant donné qu'elle est appelée directement dans
     * TicketDAO et ParkingSportDAO
     */
    @BeforeAll
    private static void setUp() {
        parkingSpotDAO = new ParkingSpotDAO();
        parkingSpotDAO.dataBaseConfig = dataBaseTestConfig;
        ticketDAO = new TicketDAO();
        ticketDAO.dataBaseConfig = dataBaseTestConfig;
        dataBasePrepareService = new DataBasePrepareService();
    }

    /**
     * Ré-initialisation la base de donnée après utilisation
     */
    @AfterAll
    private static void tearDown() {
        dataBasePrepareService.clearDataBaseEntries();
    }

    @BeforeEach
    private void setUpPerTest() throws Exception {
        when(inputReaderUtil.readSelection()).thenReturn(1);
        when(inputReaderUtil.readVehicleRegistrationNumber()).thenReturn("ABCDEF");
        dataBasePrepareService.clearDataBaseEntries();
    }

    @Test
    public void WhenACarIsComing_ThenTheDataBaseResponds() throws SQLException, ClassNotFoundException, IOException {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        assertThat(ticketDAO.getTicket("ABCDEF")).isNotNull();
        assertThat(parkingSpotDAO.updateParking(ticketDAO.getTicket("ABCDEF").getParkingSpot())).isTrue();
    }

    @Test
    public void WhenACarIsLeaving_ThenTheDataBaseResponds() throws SQLException, ClassNotFoundException, IOException {
        WhenACarIsComing_ThenTheDataBaseResponds();
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processExitingVehicle();
        assertThat(ticketDAO.getTicket("ABCDEF").getOutTime()).isNotNull();
        assertThat(ticketDAO.getTicket("ABCDEF").getPrice()).isNotZero();
    }

    @Test
    public void WhenACarIsComingBack_ThenItIsFlaggedAsRecurring() throws SQLException, ClassNotFoundException, IOException {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        //On simule le premier aller retour d'une voiture
        parkingService.processIncomingVehicle();
        parkingService.processExitingVehicle();
        //La voiture rentre ici dans le parking pour la deuxième fois
        parkingService.processIncomingVehicle();
        assertThat(ticketDAO.isTicketFromRecurrentUser("ABCDEF")).isTrue();
    }


    @Test
    public void WhenARecurringCarIsLeaving_ThenThePriceReductionIsApplied() throws SQLException, ClassNotFoundException, IOException {
        ParkingService parkingService = new ParkingService(inputReaderUtil, parkingSpotDAO, ticketDAO);
        parkingService.processIncomingVehicle();
        parkingService.processExitingVehicle();
        parkingService.processIncomingVehicle();

        /**
         *
         * @see processExitingVehicleWith24HStay
         * On utilise une fonction qui renvoie un OutTime 24h plus tard que le inTime, la valeur aurait pu être
         * n'importe laquelle tant qu'elle était supérieure à 30 min
         */
        parkingService.processExitingVehicleWith24HStay();
        assertThat(ticketDAO.getTicket("ABCDEF").getOutTime()).isNotNull();
        long inTimeTest = ticketDAO.getTicket("ABCDEF").getInTime().getTime();
        long outTimeTest = ticketDAO.getTicket("ABCDEF").getOutTime().getTime();
        int sec_in_millisec = 1000, min_in_sec = 60, hours_in_min = 60;
        double timeOfStay = (outTimeTest - inTimeTest) / (sec_in_millisec * min_in_sec * hours_in_min);
        double expectedResult = (timeOfStay * Fare.CAR_RATE_PER_HOUR) - (0.05 * (timeOfStay * Fare.CAR_RATE_PER_HOUR));
        assertEquals(expectedResult, ticketDAO.getTicket("ABCDEF").getPrice());
    }

}
