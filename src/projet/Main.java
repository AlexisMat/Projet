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
public class Main {

    /**
     * @param args the command line arguments
     */
    
    
    public static void main(String[] args) {
        // TODO code application logic here
        ArrayList<CapteurInterieur>listeCapteurInt  = new ArrayList<CapteurInterieur>();
        ArrayList<CapteurExterieur>listeCapteurExt  = new ArrayList<CapteurExterieur>();
        
        GPS posRad  = new GPS(50,60);
        GPS posHyg =  new GPS( -20,80);
        GPS posLum =  new GPS(-150,100);
        
        Intervalle interRad = new Intervalle(-10.0f,50.0f);
        Intervalle interHyg = new Intervalle(0,100.0f);
        Intervalle interLum = new Intervalle(0,1000.0f);
        
        
        CapteurInterieur radiateur =  new CapteurInterieur("Temperature","U2/2/208","Degres","Radiateur",interRad,"07/01/2017",0.1f,0.2f,60);
        CapteurInterieur hygrometre =  new CapteurInterieur("Humidite","U2/2/208","%","Hygrometre",interHyg,"07/01/2017",1.f,0,90);
        CapteurInterieur lumiere =  new CapteurInterieur("Luminosite","U3/2/AMPHI1","Lumen","Lumiere",interLum,"07/01/2017",0.01f,0.2f,60);
        
        CapteurExterieur radiateurExt = new CapteurExterieur("Temperature",posRad,"Degres","RadiateurExt",interRad,"07/01/2017",0.1f,0.2f,60);
        CapteurExterieur hygrometreExt =  new CapteurExterieur("Humidite",posHyg,"%","HygrometreExt",interHyg,"07/01/2017",1.f,0,90);
        CapteurExterieur lumiereExt =  new CapteurExterieur("Luminosite",posLum,"Lumen","LumiereExt",interLum,"07/01/2017",0.01f,0.2f,60);
        
        listeCapteurInt.add(radiateur);
        listeCapteurInt.add(hygrometre);
        listeCapteurInt.add(lumiere);
        
        listeCapteurExt.add(radiateurExt);
        listeCapteurExt.add(hygrometreExt);
        listeCapteurExt.add(lumiereExt);
        
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfaceVisu(listeCapteurInt,listeCapteurExt).setVisible(true);
            }
        });
    }
    
}
