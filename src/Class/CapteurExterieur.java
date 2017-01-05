/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Class;

/**
 *
 * @author Quentin
 */
public class CapteurExterieur extends Capteur {

    public GPS getLocalisation() {
        return Localisation;
    }
    private GPS Localisation; // A recuopere dans un fichier
    
    public CapteurExterieur(String type,GPS localisation,String uniteDeMesure, String Identifant, Intervalle i, String date, float precision, float marge, Integer frequence) {
        super(type,uniteDeMesure, Identifant, i, date, precision, marge, frequence);
        this.Localisation = localisation;
    }
    
}
