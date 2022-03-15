package com.parkit.parkingsystem.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Properties;

/**
 * @author : JULIEN BARONI
 * @version : 1.0
 * @inheritedDoc DataBaseTestConfig
 * Cette classe permet d'initialiser la base de donnée utilisée pour la production, et de lancer la connection avec l'aide
 * de l'api JDBC.
 * <p>
 * Les paramètres de connection ont été externalisés dans un fichier .config pour plus de sécurité
 */
public class DataBaseConfig {

    private static final Logger logger = LogManager.getLogger("DataBaseConfig");

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        logger.info("Create DB connection");
        Properties props = new Properties();
        Class.forName(props.getProperty("jdbc.driver.class"));
        return DriverManager.getConnection(
                props.getProperty("jdbc.url.prod"),
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
