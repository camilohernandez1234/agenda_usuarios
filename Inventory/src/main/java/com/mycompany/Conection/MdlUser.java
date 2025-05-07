package com.mycompany.Conection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class MdlUser {

    public static boolean validarUsuario(String usuario, String password) {
        boolean valido = false;
        String sql = "SELECT * FROM usuario WHERE username = ? AND password = ?";

        Conection conexion = new Conection();
        Connection con = conexion.obtenerConexion();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, usuario);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                valido = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return valido;
    }

    public static ArrayList<MdlUser> obtenerUsuarios() {
        ArrayList<MdlUser> usuarios = new ArrayList<>();
        String sql = "SELECT id, username, nombre, email FROM usuario";

        Conection conexion = new Conection();
        Connection con = conexion.obtenerConexion();

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");

                MdlUser usuario = new MdlUser(id, username, nombre, email);
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return usuarios;
    }

 
    private int id;
    private String username;
    private String nombre;
    private String email;

    public MdlUser(int id, String username, String nombre, String email) {
        this.id = id;
        this.username = username;
        this.nombre = nombre;
        this.email = email;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getNombre() { return nombre; }
    public String getEmail() { return email; }
}
