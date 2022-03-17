package com.parkit.parkingsystem.service;

import com.parkit.parkingsystem.constants.Fare;
import com.parkit.parkingsystem.model.Ticket;


 /**
 *  * @author : JULIEN BARONI
 *  * @version : 2.0
 *  * <p>
 *  * Cette classe permet de calculer le prix de sortie d'un ticket de parking en fonction des différentes règles
  *  métier.
 *  * <p>
 *  *
 *  */
public class FareCalculatorService {

    public void calculateFare(Ticket ticket) {
        if ((ticket.getOutTime() == null) || (ticket.getOutTime().before(ticket.getInTime()))) {
            throw new IllegalArgumentException("Out time provided is incorrect:" + ticket.getOutTime().toString());
        }
        //TODO #1 : Fix the code to make the unit tests pass
        /**
         * La correction apportée vient du format des variables qui sont utilisées pour enregistrer les temps
         * d'arrivée et de sortie. Initialement, ces variables étaient des integer qui tronquaient les éléments après
         * la virgule permettant d'avoir une durée non nulle.
         */
        double inHour = ticket.getInTime().getTime();
        double outHour = ticket.getOutTime().getTime();
        /**
        * ici on convertit les temps en heure afin de pouvoir les multiplier avec les tarifs horaires
        */
        double duration;
        final int sec_in_millisec = 1000, min_in_sec = 60, hours_in_min = 60;
        duration = (outHour - inHour) / (sec_in_millisec * min_in_sec * hours_in_min);
        //TODO STORY #1 : Free 30-min parking
        /**
         * D'après la règle métier, on force un prix à zéro si la durée dans le parking est inférieur à la moitié
         * d'une heure
         */
        if (duration < 0.5) {
            ticket.setPrice(0);

        } else {

            switch (ticket.getParkingSpot().getParkingType()) {
                case CAR: {
                    ticket.setPrice(duration * Fare.CAR_RATE_PER_HOUR);
                    break;
                }
                case BIKE: {
                    ticket.setPrice(duration * Fare.BIKE_RATE_PER_HOUR);
                    break;
                }
            }
        }
        //TODO STORY #2 : 5% discount for recurring users
        /**
         * @see com.parkit.parkingsystem.dao.TicketDAO.isTicketFromRecurrentUser
         * Une fonction supplémentaire a été implémentée pour déterminer si l'utilisateur est récurrent,
         * et applique la réduction si c'est le cas. Le retour de cette fonction est stockée dans un attribut
         * supplémentaire de la classe Ticket
         */
        if (ticket.getIsRecurring()) {
            ticket.setPrice(ticket.getPrice() - (0.05 * ticket.getPrice()));
        }

    }
}