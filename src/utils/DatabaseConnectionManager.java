package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionManager {

    private final String url;
    private final Properties properties;

    public DatabaseConnectionManager() {
        this.url = "jdbc:mysql://wgudb.ucertify.com:3306/U05cx3";
        this.properties = new Properties();
        this.properties.setProperty("user", "U05cx3");
        this.properties.setProperty("password", "53688467011");
    }

    public PreparedStatement prepareStatement(String QUERY) throws Exception {
        Connection conn = this.getConnection();
        return conn.prepareStatement(QUERY);
    }

    public PreparedStatement prepareStatement(String QUERY, int id) throws Exception {
        Connection conn = this.getConnection();
        return conn.prepareStatement(QUERY, id);
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(this.url, this.properties);
    }

    public void closeConnection() throws SQLException {
        this.getConnection().close();
    }
    
}
