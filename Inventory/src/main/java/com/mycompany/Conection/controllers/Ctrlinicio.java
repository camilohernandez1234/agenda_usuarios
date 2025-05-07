package com.mycompany.Conection.controllers;

import com.mycompany.Conection.views.FrnInicio;
import com.mycompany.Conection.views.FrnLogin;

public class Ctrlinicio {
    public FrnInicio vista;

    public Ctrlinicio(FrnInicio vista) {
        this.vista = vista;
        iniciarEventos();
    }

    private void iniciarEventos() {
        vista.getIngresarBtn().addActionListener(e -> {
            vista.dispose(); 

            FrnLogin login = new FrnLogin();
            CtrlLogin ctrlLogin = new CtrlLogin(login); 
            login.setVisible(true);
        });
    }
}
