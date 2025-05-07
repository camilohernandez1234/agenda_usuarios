package com.mycompany.Conection.controllers;

import com.mycompany.Conection.views.FrnLogin;
import com.mycompany.Conection.views.FrnDhasboard;
import com.mycompany.Conection.MdlUser;
import javax.swing.JOptionPane;

public class CtrlLogin {

    public FrnLogin vista;

    public CtrlLogin(FrnLogin vista) {
        this.vista = vista;
        iniciarEventos();
    }

    private void iniciarEventos() {
        vista.getLoginBtn().addActionListener(e -> {
            String usuario = vista.getUsuarioTxt().getText();
            String clave = new String(vista.getPasswordTxt().getPassword());

            if (MdlUser.validarUsuario(usuario, clave)) {
                JOptionPane.showMessageDialog(vista, "✅ Login exitoso");

                vista.dispose(); // Cerramos el login

                FrnDhasboard dashboard = new FrnDhasboard();
                CtrlDhasboard ctrl = new CtrlDhasboard(dashboard);
                dashboard.setVisible(true); // Mostramos el dashboard
            } else {
                JOptionPane.showMessageDialog(
                    vista,
                    "❌ Usuario o contraseña incorrectos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }
}
