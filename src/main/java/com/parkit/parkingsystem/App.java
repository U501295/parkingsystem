package com.parkit.parkingsystem;

import com.parkit.parkingsystem.service.InteractiveShell;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author : JULIEN BARONI
 * @VERSION : 2.0
 * <p>
 * Cette application est un système de paiement de parking
 * automatisé appelé Park’it.
 * <p>
 * Elle se présente sous la forme d'une console
 * avec laquelle l'utilisateur peut interagir.
 * <p>
 * La classe main invoque une console Shell pour proposer les premières options
 * à l'utilisateur, affichées grâce à un logger.
 */
public class App {
    private static final Logger logger = LogManager.getLogger("App");

    public static void main(String args[]) {
        logger.info("Initializing Parking System");
        InteractiveShell.loadInterface();
    }
}
