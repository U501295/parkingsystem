package com.parkit.parkingsystem.integration.config;

import com.parkit.parkingsystem.config.DataBaseConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Properties;

/**
 * @author : JULIEN BARONI
 * @version : 1.0
 * <p>
 * Cette classe permet d'initialiser la base de donnée utilisée pour les tests, et de lancer la connection avec l'aide
 * de l'api JDBC.
 * <p>
 * Les paramètres de connection ont été externalisés dans un fichier .config pour plus de sécurité
 */

public class DataBaseTestConfig extends DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseTestConfig");

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Properties props = new Properties();
        logger.info("Create DB connection");
        Class.forName(props.getProperty("jdbc.driver.class"));
        return DriverManager.getConnection(
                props.getProperty("jdbc.url.test"),
                props.getProperty("jdbc.login"),
                props.getProperty("jdbc.password"));
    }

    public void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
                logger.info("Closing DB connection");
            } catch (SQLException e) {
                logger.error("Error while closing connection", e);
            }
        }
    }


    /**
     * Un prepared statement correspond au wrapping d'une instruction SQL dans du code JAVA,
     * dont on se sert pour exploiter la base de donnée depuis le script.
     *
     * @param ps
     */


    public void closePreparedStatement(PreparedStatement ps) {
        if (ps != null) {
            try {
                ps.close();
                logger.info("Closing Prepared Statement");
            } catch (SQLException e) {
                logger.error("Error while closing prepared statement", e);
            }
        }
    }


    /**
     * Un ResultSet correspond au retour d'une requête vers une base de donnée.
     * On se sert d'un result set pour enrichir notre script grâce aux output de la base
     * en réponse à nos requêtes.
     *
     * @param rs
     */
    public void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                logger.info("Closing Result Set");
            } catch (SQLException e) {
                logger.error("Error while closing result set", e);
            }
        }
    }
}
