package edu.exchanger.currencyexchanger.util;

import java.io.File;
import java.net.FileNameMap;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;


import java.io.File;
import java.net.FileNameMap;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Optional;

public class DataBaseUtil {
    public static Optional<Connection> getConnect() {
        try {
            Class.forName("org.sqlite.JDBC");
            URL resource = DataBaseUtil.class.getClassLoader().getResource("exchanger.db");

            if (resource != null) {
                String dataBasePath = "jdbc:sqlite:" + new File(resource.toURI()).getAbsolutePath();
                Connection connection = DriverManager.getConnection(dataBasePath);
                return Optional.of(connection);
            } else {
                System.out.println("Database file not found");
            }
        } catch (SQLException | ClassNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

}
