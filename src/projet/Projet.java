/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projet;

import Class.Capteur;
import Class.CapteurExterieur;
import Class.CapteurInterieur;
import Class.GPS;
import Class.Intervalle;
import java.awt.List;
import java.util.ArrayList;

/**
 *
 * @author Quentin
 */
public class Projet {

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        ArrayList<CapteurInterieur>listeCapteurInt  = new ArrayList<CapteurInterieur>();
        ArrayList<CapteurExterieur>listeCapteurExt  = new ArrayList<CapteurExterieur>();
        GPS posRad  = new GPS(50,60);
        Intervalle interRad = new Intervalle(-10.0f,50.0f);
        CapteurInterieur radiateur =  new CapteurInterieur("Temperature","U3 3eme 301","Degres","Radiateur",interRad,"07/01/2017",0.1f,0.2f,60);
        CapteurExterieur radiateurExt = new CapteurExterieur("Temperature",posRad,"Degres","Radiateur",interRad,"07/01/2017",0.1f,0.2f,60);
        listeCapteurInt.add(radiateur);
        listeCapteurExt.add(radiateurExt);
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfaceVisu(listeCapteurInt,listeCapteurExt).setVisible(true);
            }
        });
    }
    
}
