package com.parkit.parkingsystem;

import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.dao.ParkingSpotDAO;
import com.parkit.parkingsystem.dao.TicketDAO;
import com.parkit.parkingsystem.integration.config.DataBaseTestConfig;
import com.parkit.parkingsystem.model.ParkingSpot;
import com.parkit.parkingsystem.model.Ticket;
import com.parkit.parkingsystem.service.ParkingService;
import com.parkit.parkingsystem.util.InputReaderUtil;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Null;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.util.Date;
import java.util.NoSuchElementException;

import static com.parkit.parkingsystem.constants.ParkingType.BIKE;
import static com.parkit.parkingsystem.constants.ParkingType.CAR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InputReaderUtilTest {


    @BeforeEach
    private void setUpPerTest() {

    }
    @Test
    public void ReturnAnInt() {

        InputReaderUtil scan = new InputReaderUtil();
        String input = "1";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Assertions.assertThat(1).isEqualTo(scan.readSelection());

    }

    @Test
    public void GetError_WhenIsAskedToReturnAnIntegerInput_WithAStringInput() {
        InputReaderUtil scan = new InputReaderUtil();
        String input = "ABCDEF";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        try {
            scan.readSelection();
        } catch (NumberFormatException e) {
            Assertions.assertThat(e)
                    .isInstanceOf(NumberFormatException.class)
                    .hasMessage("Error while reading user input from Shell");

        }
    }

    @Test
    public void ReturnAString() {
        InputReaderUtil scan = new InputReaderUtil();
        String input = "ABCDEF";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        Assertions.assertThat("ABCDEF").isEqualTo(scan.readVehicleRegistrationNumber());
    }

    @Test
    public void GetError_WhenIsAskedToReturnAString_WithEmptyInput() {
        InputReaderUtil scan = new InputReaderUtil();
        String input = "";
        InputStream in = new ByteArrayInputStream(input.getBytes());
        System.setIn(in);
        try {
            scan.readVehicleRegistrationNumber();
        } catch (NoSuchElementException e) {
            Assertions.assertThat(e)
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("No line found");

        }

    }





}
