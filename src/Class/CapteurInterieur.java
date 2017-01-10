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
public class CapteurInterieur extends Capteur implements Comparable<CapteurInterieur>{

    /* Convention pour la localisation  Batiment/Etage/salle*/
    
    public String getLocalisation() {
        return localisation;
    }

    public void setLocalisation(String localisation) {
        this.localisation = localisation;
    }
    private String localisation;
    
    public CapteurInterieur(String type ,String localisation,String uniteDeMesure, String identifant, Intervalle i, String date, float precision, float marge, Integer frequence) {
        super(type,uniteDeMesure, identifant, i, date, precision, marge, frequence);
        this.localisation = localisation;
    }

    @Override
    public int compareTo(CapteurInterieur o) {
        return this.getIdentifant().compareTo(o.getIdentifant());
    }
    
}
