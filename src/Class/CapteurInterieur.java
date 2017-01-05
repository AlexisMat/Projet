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
public class CapteurInterieur extends Capteur {

    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }
    private String localisation;
    
    public CapteurInterieur(String type ,String localisation,String uniteDeMesure, String Identifant, Intervalle i, String date, float precision, float marge, Integer frequence) {
        super(type,uniteDeMesure, Identifant, i, date, precision, marge, frequence);
        this.localisation = localisation;
    }
    
}
