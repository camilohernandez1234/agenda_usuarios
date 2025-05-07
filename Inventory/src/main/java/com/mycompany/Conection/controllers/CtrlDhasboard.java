package com.mycompany.Conection.controllers;

import com.mycompany.Conection.views.FrnDhasboard;

public class CtrlDhasboard {
    public FrnDhasboard vista;

    public CtrlDhasboard(FrnDhasboard vista) {
        this.vista = vista;
        this.vista.setVisible(true);
    }
}
