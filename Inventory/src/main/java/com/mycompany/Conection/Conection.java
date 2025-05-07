package com.mycompany.Conection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conection {
    private static final String URL = "jdbc:mysql://localhost:3306/inventory?serverTimezone=UTC";
    private static final String USER = "root";
    private static final String PASS = "";

    private Connection con;

 
    public Conection() {
        try {
            con = DriverManager.getConnection(URL, USER, PASS);
            System.out.println(" Conectado a la base de datos.");
        } catch (SQLException e) {
            System.out.println("️ Error al conectar a la base de datos.");
            e.printStackTrace();
        }
    }


    public Connection obtenerConexion() {
        return con;
    }

    
    }

