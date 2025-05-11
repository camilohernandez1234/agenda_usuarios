package com.mycompany.Conection;

import com.mycompany.Conection.views.FrnInicio;
import com.mycompany.Conection.controllers.Ctrlinicio;
import java.sql.Connection;

public class Inventory {
    public static void main(String[] args) {
        
        Conection conection = new Conection(); 
        Connection conn = conection.obtenerConexion(); 

        if (conn != null) {
            System.out.println("conexion exitosa a la base de datos.");
        } else {
            System.out.println("Error al conectar con la base de datos.");
        }

    
        FrnInicio inicio = new FrnInicio();
        new Ctrlinicio(inicio);
        inicio.setVisible(true);
    }
}
