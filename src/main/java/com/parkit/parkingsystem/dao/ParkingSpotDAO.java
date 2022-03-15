package com.parkit.parkingsystem.dao;

import com.parkit.parkingsystem.config.DataBaseConfig;
import com.parkit.parkingsystem.constants.DBConstants;
import com.parkit.parkingsystem.constants.ParkingType;
import com.parkit.parkingsystem.model.ParkingSpot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author : JULIEN BARONI
 * @version : 1.0
 * <p>
 * Cette classe permet de faire le lien entre la base de donnée et la gestion du parking.
 * <p>
 * Une attention particulière doit être apportée à la fermeture des Statements, resultsSets et connections.
 */
public class ParkingSpotDAO {
    private static final Logger logger = LogManager.getLogger("ParkingSpotDAO");

    public DataBaseConfig dataBaseConfig = new DataBaseConfig();

    /**
     * @param parkingType : correspond aux type de véhicule
     * @see DataBaseConfig
     * Pour le " if (rs.next())" --> Un result set doit être porté au conteneur suivant pour récupérer les données
     * d'une base
     */
    public int getNextAvailableSlot(ParkingType parkingType) throws SQLException, ClassNotFoundException {
        Connection con = null;
        int result = -1;
        con = dataBaseConfig.getConnection();
        PreparedStatement ps = con.prepareStatement(DBConstants.GET_NEXT_PARKING_SPOT);
        ps.setString(1, parkingType.toString());
        ResultSet rs = ps.executeQuery();
        try {
            if (rs.next()) {
                result = rs.getInt(1);
            }
        } catch (Exception ex) {
            logger.error("Error fetching next available slot", ex);
        } finally {
            dataBaseConfig.closeResultSet(rs);
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
        return result;
    }

    /**
     * @param parkingSpot : correspond aux attributs de la place de parking
     * @see DataBaseConfig
     * Un result set doit être porté au conteneur suivant pour récupérer les données d'une base
     */
    public boolean updateParking(ParkingSpot parkingSpot) throws SQLException, ClassNotFoundException {
        //update the availability fo that parking slot
        Connection con = null;
        con = dataBaseConfig.getConnection();
        PreparedStatement ps = con.prepareStatement(DBConstants.UPDATE_PARKING_SPOT);
        try {
            ps.setBoolean(1, parkingSpot.isAvailable());
            ps.setInt(2, parkingSpot.getId());
            int updateRowCount = ps.executeUpdate();
            return (updateRowCount == 1);
        } catch (Exception ex) {
            logger.error("Error updating parking info", ex);
            return false;
        } finally {
            dataBaseConfig.closePreparedStatement(ps);
            dataBaseConfig.closeConnection(con);
        }
    }

}
