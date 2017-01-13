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
import Class.Localisation;
import Class.TypeCapteur;
import java.awt.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Quentin
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    /*Bug a regler apres -Caractere dans le filtrage
                          - Deux cateur avec le meme identifant
    */
    
    public static void main(String[] args) {
        // TODO code application logic here
        Set<Capteur> listeCapteur =  new HashSet<Capteur>();
        
        ArrayList<CapteurInterieur>listeCapteurInt  = new ArrayList<CapteurInterieur>();
        ArrayList<CapteurExterieur>listeCapteurExt  = new ArrayList<CapteurExterieur>();
        
        GPS posRad  = new GPS(50,60);
        GPS posHyg =  new GPS( -20,80);
        GPS posLum =  new GPS(-150,100);
        
        Intervalle interRad = new Intervalle(-10.0f,50.0f);
        Intervalle interHyg = new Intervalle(0,100.0f);
        Intervalle interLum = new Intervalle(0,1000.0f);
        
        CapteurInterieur radiateur =  new CapteurInterieur(TypeCapteur.Temperature,new Localisation("U2","2","208"),"Degres","Radiateur",interRad,"07/01/2017",0.1f,0.2f,60,10);
        CapteurInterieur hygrometre =  new CapteurInterieur(TypeCapteur.Humidité,new Localisation("U2","2","208"),"%","Hygrometre",interHyg,"07/01/2017",1.f,0,90,50);
        CapteurInterieur lumiere =  new CapteurInterieur(TypeCapteur.Luminosité,new Localisation("U3","2","AMPHI1"),"Lumen","Lumiere",interLum,"07/01/2017",0.01f,0.2f,60,20);
        
        CapteurExterieur radiateurExt = new CapteurExterieur(TypeCapteur.Temperature,posRad,"Degres","RadiateurExt",interRad,"07/01/2017",0.1f,0.2f,60,30);
        CapteurExterieur hygrometreExt =  new CapteurExterieur(TypeCapteur.Humidité,posHyg,"%","HygrometreExt",interHyg,"07/01/2017",1.f,0,90,50);
        CapteurExterieur lumiereExt =  new CapteurExterieur(TypeCapteur.Luminosité,posLum,"Lumen","LumiereExt",interLum,"07/01/2017",0.01f,0.2f,60,96);
        
        listeCapteur.add(radiateur);
        listeCapteur.add(hygrometre);
        listeCapteur.add(lumiere);
        
        listeCapteur.add(radiateurExt);
        listeCapteur.add(hygrometreExt);
        listeCapteur.add(lumiereExt);
        
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new InterfaceVisu(listeCapteur).setVisible(true);
            }
        });
    }
    
}
